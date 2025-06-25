package org.macver.sunny;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.jetbrains.annotations.NotNull;
import org.macver.sunny.data.GuildManager;
import org.macver.sunny.data.IndexManager;
import org.macver.sunny.data.UserCooldownManager;
import org.macver.sunny.data.type.Answer;
import org.macver.sunny.data.type.GuildConfiguration;
import org.macver.sunny.data.type.Index;
import org.macver.sunny.data.type.SearchResult;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class QueryResponder implements EventListener {

    final UserCooldownManager cooldownManager = new UserCooldownManager();
    final SpellChecker spellChecker = new SpellChecker();
    final GuildManager guildManager = new GuildManager();
    final double mentionedPickiness = 0.2;

    final List<String> mentionNotFoundReplies = List.of(
            "Hello there! I tried searching for your query, but I couldn't find anything even remotely close. Sorry about that! Check your spelling and maybe try a different query? You may also try waiting for a human to come help you!",
            "Hey there! I looked around for your query, but came up empty—nothing even close. Sorry about that! Maybe double-check your spelling or try rephrasing it? A human might pop in to help soon too!",
            "Hi! I searched far and wide, but couldn’t find anything that matches your request. Sorry! You could try adjusting your query a bit, or hang tight—someone human may assist shortly.",
            "Hello! I gave it my best shot, but there’s nothing that matches your query. My bad! Could you try a different wording or check for typos? A real person might be along soon to help out.",
            "Hey! I searched everywhere, but came up with nothing that fits. Sorry about that! Maybe try a different search or wait a moment—someone might jump in to help you.",
            "Hi there! I looked into it, but didn’t find anything close to your query. Apologies! Double-check the spelling or try a new phrase? A human might assist you soon too!",
            "Hello! I tried finding something based on your query, but no luck—nothing even close. Sorry! Maybe tweak your wording a bit? You can also wait for human help.",
            "Hey there! I did a search for your query but didn’t turn up anything useful. Sorry about that! You might want to reword it or check the spelling—and someone human could be on their way to help too.",
            "Hi! I gave it a go, but couldn’t find anything that matches your search. My apologies! Try changing the wording, or feel free to wait for a human to assist.",
            "Hey! I looked into your query, but couldn’t find anything even remotely similar. Oops! Maybe try a different phrase—or hang tight and wait for a human to lend a hand.",
            "Hello there! I checked around but didn’t find anything useful based on your query. So sorry! Could you try again with a different phrase? A human might be along shortly to help you out."
    );

    final List<String> mentionOnlyReplies = List.of(
            "Hi there! Pleased to meet you! My name is %s and I'm here to help answer any questions you might have. I try to find a result for every message, but if I don't find anything for you, you can @mention me with your question and I'll show you the closest result I have. I'm glad to have you here in %2s!",
            "Hi and welcome! I’m %s, happy to help however I can. I always try to find something useful when you send a message—but if I don’t, just @mention me with your question and I’ll give it another go. Great to have you here in %2s!",
            "Hello there! So nice to meet you! I’m %s, your helpful assistant. I’ll do my best to answer anything you throw my way. Didn’t get what you needed? Just @mention me, and I’ll dig up the closest match. Welcome to %2s—we’re happy to have you!",
            "Hey! Great to see you! I’m %s, and I’m here to help with whatever you need. I try to find helpful answers to everything. If I miss, just @mention me and I’ll try again with the best match I’ve got. Glad you’ve joined us in %2s!",
            "Hi there! Happy to have you here! I’m %s, ready to help you out. I’ll search for answers to any message you send, and if I don’t quite get it right, just @mention me and I’ll give you the closest match I’ve got. Welcome to %2s!",
            "Hello and welcome! I’m %s, and I'm excited to help however I can. I’ll always try to respond with something useful, but if I come up short, just tag me with your question. I’ll do my best! So glad you’re in %2s!",
            "Hey there! I’m %s, here to lend a hand! I try to answer every message with something helpful. If I don’t quite get it right, just @mention me and I’ll show you what I’ve got. It’s awesome having you here in %2s!",
            "Hi! Great to meet you! I’m %s, and I’m here to help however I can. I’ll do my best to answer your questions. If I miss, feel free to @mention me so I can try again with my closest match. Welcome aboard in %2s!",
            "Hello! I’m %s, your friendly assistant here to help out. I do my best to answer everything you send my way. If I don’t find what you’re looking for, just @mention me and I’ll try again. So happy you’re part of %2s!",
            "Hey, nice to meet you! I’m %s, and I’m here to help you find answers. I’ll give every message a shot, but if I can’t find anything right away, just @mention me and I’ll try again with the best match I’ve got. Welcome to %2s!"
    );

    final List<String> foundResultReplies = List.of(
            "Hi, %s. I found something that might help you. If not, a knowledgeable human will come to assist!",
            "Hey, %s! I found something that might be useful. If it’s not quite right, don’t worry—a helpful human will be with you soon!",
            "Hi there, %s! I dug up something that could help. If it doesn’t hit the mark, a smart human will step in to assist shortly!",
            "Hello, %s! I think this might help you out. If not, hang tight—someone human will be along soon to lend a hand!",
            "Hey %s, I’ve got something that might answer your question. If it doesn’t do the trick, a knowledgeable human will be here soon to help!",
            "Hi, %s! I found something that could be helpful. If it’s not what you need, a real live human will be joining in soon!",
            "Hello there, %s! I pulled up something that might work for you. If it misses the mark, a human expert will be here shortly to help out!",
            "Hey, %s! I think this might point you in the right direction. If it doesn’t, don’t worry—someone human will jump in soon!",
            "Hiya, %s! I came up with something that could help. If it’s not quite what you're after, a helpful human will be by soon!",
            "Hi, %s. Here’s something I think could help. If it’s not a perfect fit, a knowledgeable human will assist you shortly!",
            "Hey there, %s! I found a result that might be helpful. If not, a smart human should be along soon to help you out!"
    );

    final List<String> mentionFoundResultReplies = List.of(
            "Hello there! I tried searching for your query and this is the closest thing I have. If it's not what you're looking for, go ahead and let me know by pressing the **Not helpful** button. You can try rewording it to be more specific or you can wait for a human to help you.",
            "Hi there! I searched around and this is the closest match I could find. If it’s not quite right, just hit the **Not helpful** button. You can also try rewording your query or hang tight for a human to help!",
            "Hey! I looked into your question and this is the best result I found. If it’s off the mark, feel free to press **Not helpful**. You can try being a bit more specific, or wait for a human to assist!",
            "Hello! I gave it my best shot and this is the closest thing I came up with. If it’s not what you were after, tap **Not helpful**, Try rephrasing your query, or wait for a friendly human to jump in!",
            "Hi there! I tried to find the best match for your query—this is what I’ve got. If it doesn’t quite help, go ahead and press **Not helpful**. Try to reword your question. A human can also step in if needed!",
            "Hey there! I searched for your question and this was the closest result I could find. If it’s not quite right, click **Not helpful** and try asking in a different way. A human will be along if you need more help!",
            "Hello again! I looked into your query and this is the nearest match I found. If it’s not helpful, just press the **Not helpful** button and tweak your wording a bit. A human may join in shortly!",
            "Hi! I gave your question a search and this is the closest answer I’ve got. If it’s not helpful, let me know with the **Not helpful** button and rephrase your question. A human is nearby if you need more support!",
            "Hey, thanks for your question! I found something that might be close. If it’s not what you need, press **Not helpful**. You can try rewording it, or hang tight—someone human might jump in soon!",
            "Hi there! I searched based on your query and this is the best result I’ve got. If it doesn’t quite hit the mark, use the **Not helpful** button and rephrase your question. A human can help if needed!",
            "Hello! I tried to find a match for your question, and this is what I came up with. Not quite right? Click **Not helpful** and give it another shot with different wording. A real person might chime in shortly!"
    );

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if (genericEvent instanceof MessageReceivedEvent event) {

            // Don't process bot messages
            if (event.getAuthor().isBot()) {
                Sunny.logger.info("Ignoring bot message.");
                return;
            }

            // Ignore DMs
            if (!event.getChannelType().isGuild()) {
                Sunny.logger.info("Ignoring private message.");
                return;
            }

            // Don't process if channel isn't linked to any index
            List<Index> indexes;
            try {
                indexes = new IndexManager().getIndexes(event.getGuild());
                Sunny.logger.info("Retrieved {} indexes for guild.", indexes.size());
            } catch (IOException e) {
                Sunny.logger.warn("Failed to retrieve indexes: {}\n{}", e.getMessage(), Arrays.toString(e.getStackTrace()));
                new Reporter().report( "Failed to retrieve indexes: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                return;
            }
            boolean channelValid = false;
            for (Index index : indexes) {
                Sunny.logger.info("Checking index {} for matching channel.", indexes.indexOf(index));
                if (index.channels.contains(event.getChannel().getIdLong())) {
                    Sunny.logger.info("Found matching channel.");
                    channelValid = true;
                    break;
                }
            }
            if (!channelValid) {
                Sunny.logger.info("Did not find matching channel.");
                return;
            }

            String contentRaw = event.getMessage().getContentRaw();

            // Check if mentioned
            boolean mentioned = false;
            String selfMention = event.getJDA().getSelfUser().getAsMention();
            if (contentRaw.contains(selfMention)) {
                if (contentRaw.replaceAll(selfMention, "").isBlank()) {
                    Sunny.logger.info("Query contains only mention.");
                    // If message contains only a mention, send an introduction
                    Member selfMember = event.getGuild().getMember(event.getJDA().getSelfUser());
                    assert selfMember != null;

                    String reply = mentionOnlyReplies.get(new Random().nextInt(mentionOnlyReplies.size()));

                    event.getMessage().reply(String.format(reply, selfMember.getEffectiveName(), event.getGuild().getName())).queue();
                    return;
                } else {
                    Sunny.logger.info("Query contains mention.");
                    // If message is mention and other text, note it
                    mentioned = true;
                }
            }

            // If not mentioned and the user is on cooldown, do not process the message
            if (!mentioned && cooldownManager.isUserOnCoolDown(event.getAuthor())) {
                Sunny.logger.info("Ignoring user on cooldown.");
                return;
            }

            // Get the query
            String query = event.getMessage().getContentDisplay();
            // Remove mention if present
            if (mentioned) {
                query = query.replaceAll("@" + event.getGuild().getSelfMember().getEffectiveName(), "").strip();
            }

            // Correct the spelling
            String correctedQuery;
            try {
                GuildConfiguration guildConfiguration = guildManager.getGuildConfiguration(event.getGuild());
                correctedQuery = spellChecker.correctSpelling(query, guildConfiguration.noCorrectionPhrases);
                Sunny.logger.info("Corrected spelling.");
            } catch (IOException e) {
                Sunny.logger.warn("Spell correction failed: {}\n{}", e.getMessage(), Arrays.toString(e.getStackTrace()));
                new Reporter().report( "Failed to retrieve indexes: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                correctedQuery = query;
            }

            // Search for an answer
            SearchResult answerResult;
            try {
                answerResult = new Searcher().findRelevantDocument(event.getGuild(), event.getChannel(), correctedQuery);
                Sunny.logger.info("Got a possible answer.");
            } catch (IOException e) {
                Sunny.logger.warn("Failed to search for an answer: {}\n{}", e.getMessage(), Arrays.toString(e.getStackTrace()));
                new Reporter().report( "Failed to search for an answer: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                return;
            }

            Map<Double, Answer> answers = answerResult.answers;

            Answer topAnswer = null;
            double highestAnswerConfidence = 0;

            if (answers != null && !answers.isEmpty()) {
                // If there are possible answers, find the best one
                for (Double answerConfidence : answers.keySet()) {
                    Answer answer = answers.get(answerConfidence);

                    // Set threshold
                    double confidenceThreshold = answer.minimumConfidence;
                    if (mentioned) confidenceThreshold = mentionedPickiness;

                    // If this is the highest-confidence answer, and it passes thresholds, set it as best
                    if (answerConfidence > confidenceThreshold && answerConfidence > highestAnswerConfidence) {
                        topAnswer = answer;
                        highestAnswerConfidence = answerConfidence;
                    }
                }
            }

            if (mentioned && topAnswer == null) {
                // Reply if mentioned but no result
                Sunny.logger.info("Result not satisfactory. Replying because mentioned.");
                String reply = mentionNotFoundReplies.get(new Random().nextInt(mentionNotFoundReplies.size()));
                event.getMessage().reply(reply).queue();
                return;
            } else if (!mentioned && topAnswer == null) {
                // Don't reply if not mentioned and no result
                Sunny.logger.info("Result not satisfactory. Quietly giving up.");
                return;
            }

            // Otherwise, reply!
            String confidencePercent = String.valueOf(Math.round( highestAnswerConfidence * 100)).concat("%");
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.getHSBColor(56,83,88)).setTitle(topAnswer.title).setDescription(topAnswer.content);
            if (topAnswer.url != null) embedBuilder.setUrl(topAnswer.url);
            ActionRow actionRow = ActionRow.of(Button.success("answer", "Mark as answer"), Button.secondary("unhelpful", "Not helpful"));

            // Change reply if mentioned
            String reply = String.format(foundResultReplies.get(new Random().nextInt(foundResultReplies.size())), event.getAuthor().getAsMention());
            if (mentioned) reply = String.format(mentionFoundResultReplies.get(new Random().nextInt(mentionFoundResultReplies.size())), event.getAuthor().getAsMention());

            MessageCreateAction messageCreateAction = event.getMessage().reply(reply  + "\n-# This result is a " + confidencePercent + " match. I interpreted your query as \"" + String.join("\n-#", correctedQuery.split("\n")) + "\".")
                    .addEmbeds(embedBuilder.build()).addComponents(actionRow);

            // Add link button if URL exists
            if (topAnswer.url != null) actionRow.getComponents().add(Button.link(topAnswer.url, "Open resource"));

            messageCreateAction.queue();

            // Put this user on cooldown
            cooldownManager.markInteraction(event.getAuthor());
        } else if (genericEvent instanceof ButtonInteractionEvent event) {

            if (Objects.equals(event.getButton().getId(), "unhelpful")) {
                Message message = event.getInteraction().getMessage();
                String title = message.getEmbeds().getFirst().getTitle();
                event.editMessage("**" + title + "** was marked as not helpful for this query by " + event.getUser().getAsMention() + ".").setEmbeds(new ArrayList<>()).setComponents(new ArrayList<>()).queue();
            } else if (Objects.equals(event.getButton().getId(), "answer")) {
                Message message = event.getInteraction().getMessage();

                // Keep resource link if it exists
                List<LayoutComponent> newComponents = new ArrayList<>();
                Button lastButton = message.getButtons().getLast();
                if (lastButton.getStyle().equals(ButtonStyle.LINK)) newComponents.add(ActionRow.of(lastButton));


                event.editMessage("This response was marked as the answer by " + event.getUser().getAsMention() + ".").setComponents(newComponents).queue();
            }

        }


    }
}
