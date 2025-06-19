package org.macver.sunny.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.macver.sunny.data.IndexManager;
import org.macver.sunny.data.type.Answer;
import org.macver.sunny.data.type.Index;
import org.macver.sunny.data.type.SlashCommand;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IndexCommand implements SlashCommand {

    IndexManager indexManager = new IndexManager();

    @Override
    public SlashCommandData getData() {
        return Commands.slash("index", "Manage indexes.")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_CHANNEL))
                .addSubcommandGroups(
                        new SubcommandGroupData("channel", "Link or unlink a channel to or from an index.")
                                .addSubcommands(
                                        new SubcommandData("link", "Link a channel to an index.")
                                                .addOptions(
                                                        new OptionData(OptionType.CHANNEL, "channel", "The channel to link.", true),
                                                        new OptionData(OptionType.STRING, "index", "The name of the index to link the channel to.", true, true)
                                                ),
                                        new SubcommandData("unlink", "Unlink a channel to an index.")
                                                .addOptions(
                                                        new OptionData(OptionType.CHANNEL, "channel", "The channel to link.", true),
                                                        new OptionData(OptionType.STRING, "index", "The name of the index to link the channel to.", true, true)
                                                ),
                                        new SubcommandData("list_channels", "List the channels that an index is linked to.")
                                                .addOptions(
                                                        new OptionData(OptionType.STRING, "index", "The name of the index to list the channels of.", true, true)
                                                ),
                                        new SubcommandData("list_indexes", "List the indexes that a channel is linked to.")
                                                .addOptions(
                                                        new OptionData(OptionType.CHANNEL, "channel", "The channel to list indexes of.", true)
                                                )
                                ),
                        new SubcommandGroupData("answer", "Manager answers in an index.")
                                .addSubcommands(
                                        new SubcommandData("list", "List all answers of an index.")
                                                .addOptions(
                                                        new OptionData(OptionType.STRING, "index", "The name of the index to list the answers of.", true, true)
                                                ),
                                        new SubcommandData("add", "Add an answer to an index.")
                                                .addOptions(
                                                        new OptionData(OptionType.STRING, "index", "The name of the index to add this answer to.", true, true),
                                                        new OptionData(OptionType.STRING, "title", "The title of the embed for this answer.", true, true),
                                                        new OptionData(OptionType.STRING, "content", "The content of the embed for this answer.", true),
                                                        new OptionData(OptionType.STRING, "url", "The URL of the related resource for this answer.", false),
                                                        new OptionData(OptionType.STRING, "query", "The query reference for this answer. A list of possible questions separated by semicolons.", false),
                                                        new OptionData(OptionType.NUMBER, "minimum_confidence", "The minimum confidence that Sunny must have to show this answer, between 0.0 and 0.1. Default 0.5.", false)
                                                ),
                                        new SubcommandData("remove", "Remove an answer from an index.")
                                                .addOptions(
                                                        new OptionData(OptionType.STRING, "index", "The name of the index to remove from.", true, true),
                                                        new OptionData(OptionType.STRING, "answer", "The title of the answer to remove.")
                                                ),
                                        new SubcommandData("edit", "Edit an answer.")
                                                .addOptions(
                                                        new OptionData(OptionType.STRING, "index", "The name of the index to edit.", true, true),
                                                        new OptionData(OptionType.STRING, "title", "The title of the answer to edit.", true, true),
                                                        new OptionData(OptionType.STRING, "new_title", "Change the title of this answer.", false),
                                                        new OptionData(OptionType.STRING, "new_content", "Change the content of this answer.", false),
                                                        new OptionData(OptionType.STRING, "new_url", "Change the URL of this answer.", false),
                                                        new OptionData(OptionType.STRING, "new_query", "Change the query string of this answer. A list of possible questions separated by semicolons.", false),
                                                        new OptionData(OptionType.NUMBER, "new_minimum_confidence", "Change the minimum confidence of this answer, between 0.0 and 1.0", false)
                                                ),
                                        new SubcommandData("info", "Get information about an answer.")
                                                .addOptions(
                                                        new OptionData(OptionType.STRING, "index", "The name of the index to edit.", true, true),
                                                        new OptionData(OptionType.STRING, "title", "The title of the answer to edit.", true, true)
                                                ),
                                        new SubcommandData("add_query", "Add questions to the query reference.")
                                                .addOptions(
                                                        new OptionData(OptionType.STRING, "index", "The name of the index to edit.", true, true),
                                                        new OptionData(OptionType.STRING, "title", "The title of the answer to edit.", true, true),
                                                        new OptionData(OptionType.STRING, "query", "A list of possible questions to add separated by semicolons.", false)
                                                )
                                )
                )
                .addSubcommands(
                        new SubcommandData("list", "List answer indexes."),
                        new SubcommandData("create", "Create a new answer index.")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "name", "The name of the index for moderator reference.", true),
                                        new OptionData(OptionType.CHANNEL, "channel", "A channel to link this index to.", false)
                                ),
                        new SubcommandData("delete", "Delete a answer index.")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "index", "The name of the index to delete.", true, true)
                                )
                );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {

        Guild guild = event.getGuild();
        assert guild != null;

        switch (event.getSubcommandGroup()) {
            case "channel" -> {
                switch (event.getSubcommandName()) {
                    case "link" -> {
                        GuildChannelUnion channel = event.getOption("channel", OptionMapping::getAsChannel);
                        Index index = getIndexAndHandleErrors(event, guild);
                        if (index == null) return;
                        if (channel == null) {
                            missingOption(event, "channel");
                            return;
                        }

                        try {
                            List<Index> indexList = indexManager.getIndexes(guild);
                            int i = getPositionOfIndex(indexList, index);
                            if (i == -1) {
                                event.reply("I know which index you're talking about but I can't find it in the list for some reason. Something has gone horribly wrong here, but it's probably not your fault. You may want to reach out for support.").setEphemeral(true).queue();
                                return;
                            }
                            index.channels.add(channel.getIdLong());
                            indexList.set(i, index);
                            indexManager.save(guild, indexList);
                            event.reply("Done! Answers from the " + index.name + " index will now appear in " + channel.getAsMention() + ".").setEphemeral(true).queue();
                        } catch (IOException e) {
                            ioError(event, e.getMessage());
                        }
                    }
                    case "unlink" -> {
                        GuildChannelUnion channel = event.getOption("channel", OptionMapping::getAsChannel);
                        Index index = getIndexAndHandleErrors(event, guild);
                        if (index == null) return;
                        if (channel == null) {
                            missingOption(event, "channel");
                            return;
                        }

                        try {
                            List<Index> indexList = indexManager.getIndexes(guild);
                            int i = getPositionOfIndex(indexList, index);
                            if (i == -1) {
                                event.reply("I know which index you're talking about but I can't find it in the list for some reason. Something has gone horribly wrong here, but it's probably not your fault. You may want to reach out for support.").setEphemeral(true).queue();
                                return;
                            }
                            boolean removed = index.channels.remove(channel.getIdLong());
                            if (removed) {
                                indexList.set(i, index);
                                indexManager.save(guild, indexList);
                                event.reply("Done! Answers from the " + index.name + " index will no longer appear in " + channel.getAsMention() + ".").setEphemeral(true).queue();
                            } else {
                                event.reply("Looks like that channel is already unlinked! That was easy.").setEphemeral(true).queue();
                            }
                        } catch (IOException e) {
                            ioError(event, e.getMessage());
                        }
                    }
                    case "list_channels" -> {
                        Index index = getIndexAndHandleErrors(event, guild);
                        if (index == null) return;

                        StringBuilder stringBuilder = new StringBuilder("The " + index.name + " index is linked to all these channels:");

                        for (Long channelId : index.channels) {
                            GuildChannel channel = event.getJDA().getGuildChannelById(channelId);
                            if (channel != null) stringBuilder.append("\n- ").append(channel.getAsMention());
                        }
                        String string = stringBuilder.toString();
                        if (string.equals("This channel is linked to all these indexes:")) {
                            event.reply( "The " + index.name + " index is not linked to any channels.").setEphemeral(true).queue();
                            return;
                        }
                        event.reply(stringBuilder.toString()).setEphemeral(true).queue();
                    }
                    case "list_indexes" -> {
                        GuildChannelUnion channel = event.getOption("channel", OptionMapping::getAsChannel);
                        if (channel == null) {
                            missingOption(event, "channel");
                            return;
                        }

                        StringBuilder stringBuilder = new StringBuilder("This channel is linked to all these indexes:");

                        try {
                            List<Index> indexes = indexManager.getIndexes(guild);
                            for (Index index : indexes) {
                                if (index.channels.contains(channel.getIdLong())) stringBuilder.append("\n- ").append(index.name);
                            }
                            String string = stringBuilder.toString();
                            if (string.equals("This channel is linked to all these indexes:")) {
                                event.reply( channel.getAsMention() + " is not linked to any indexes.").setEphemeral(true).queue();
                                return;
                            }
                            event.reply(string).setEphemeral(true).queue();
                        } catch (IOException e) {
                            ioError(event, e.getMessage());
                        }
                    }
                    case null, default -> doesNotExist(event);
                }
            }
            case "answer" -> {
                switch (event.getSubcommandName()) {
                    case "list" -> {
                        Index index = getIndexAndHandleErrors(event, guild);
                        if (index == null) return;

                        List<Answer> answers = index.answers;

                        if (answers == null || answers.isEmpty()) {
                            event.reply("The " + index.name + " index is empty right now.").setEphemeral(true).queue();
                            return;
                        }

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        for (Answer answer : answers) {
                            if (answer == null) continue;

                            StringBuilder stringBuilder = new StringBuilder();

                            String title = Objects.requireNonNullElse(answer.title, "null (maybe check the JSON?)");
                            String content = Objects.requireNonNullElse(answer.content, "null (maybe check the JSON?)");
                            String url = Objects.requireNonNullElse(answer.url, "No URL");
                            String queryRef = Objects.requireNonNullElse(Arrays.toString(answer.query.toArray()), "No query reference");

                            stringBuilder.append("**Content:**\n").append(content)
                                    .append("\n\n**URL:**\n").append(url)
                                    .append("\n\n**Query Reference:**\n").append(queryRef)
                                    .append("\n\n**Minimum Confidence:**\n").append(Math.round(answer.minimumConfidence * 100)).append("%");

                            embedBuilder.addField(title, stringBuilder.toString(), false);
                        }
                        if (embedBuilder.getFields().isEmpty()) {
                            event.reply("The " + index.name + " index is empty right now.").setEphemeral(true).queue();
                            return;
                        }
                        event.reply("Here are all the answers that are in the " + index.name + " index.").addEmbeds(embedBuilder.build()).setEphemeral(true).queue();
                    }
                    case "add" -> {
                        Index index = getIndexAndHandleErrors(event, guild);
                        if (index == null) return;

                        String title = event.getOption("title", OptionMapping::getAsString);
                        String content = event.getOption("content", OptionMapping::getAsString);
                        @Nullable String url = event.getOption("url", OptionMapping::getAsString);
                        @Nullable String query = event.getOption("query", OptionMapping::getAsString);
                        @Nullable Double minimumConfidence = event.getOption("minimum_confidence", OptionMapping::getAsDouble);

                        if (title == null) {
                            missingOption(event, "title");
                            return;
                        }
                        if (content == null) {
                            missingOption(event, "content");
                            return;
                        }

                        // Don't create an answer with the same name as another
                        for (Answer answer : index.answers) {
                            if (answer.title.equals(title)) {
                                event.reply("There's already an answer with that title. If there are two, I won't know which one you're talking about!").setEphemeral(true).queue();
                                return;
                            }
                        }

                        // Create answer
                        Answer answer = new Answer();
                        answer.title = title;
                        answer.content = content;
                        answer.url = url;
                        answer.query = query != null ? List.of(query.split(";")) : new ArrayList<>();
                        answer.minimumConfidence = Objects.requireNonNullElse(minimumConfidence, 0.2);

                        // Add to index
                        try {
                            List<Index> indexList = indexManager.getIndexes(guild);
                            int i = getPositionOfIndex(indexList, index);
                            if (i == -1) {
                                event.reply("I know which index you're talking about but I can't find it in the list for some reason. Something has gone horribly wrong here, but it's probably not your fault. You may want to reach out for support.").setEphemeral(true).queue();
                                return;
                            }
                            index.answers.add(answer);
                            indexList.set(i, index);
                            indexManager.save(guild, indexList);
                            event.reply("Done! Added this new answer to the " + index.name + " index.").addEmbeds(buildAnswerEmbed(answer).build()).setEphemeral(true).queue();
                        } catch (IOException e) {
                            ioError(event, e.getMessage());
                        }

                    }
                    case "remove" -> {
                        Index index = getIndexAndHandleErrors(event, guild);
                        if (index == null) return;
                        String title = event.getOption("title", OptionMapping::getAsString);
                        if (title == null) {
                            missingOption(event, "title");
                            return;
                        }

                        // Remove from index
                        try {
                            List<Index> indexList = indexManager.getIndexes(guild);
                            int i = getPositionOfIndex(indexList, index);
                            if (i == -1) {
                                event.reply("I know which index you're talking about but I can't find it in the list for some reason. Something has gone horribly wrong here, but it's probably not your fault. You may want to reach out for support.").setEphemeral(true).queue();
                                return;
                            }
                            boolean removed = index.answers.removeIf(answer -> answer.title.equals(title));
                            if (!removed) {
                                event.reply("I couldn't find an answer with the title " + title + " in the " + index.name + " index. Sorry!").setEphemeral(true).queue();
                                return;
                            }
                            indexList.set(i, index);
                            indexManager.save(guild, indexList);
                            event.reply("Done! That answer is no longer in the " + index.name + " index.").setEphemeral(true).queue();
                        } catch (IOException e) {
                            ioError(event, e.getMessage());
                        }
                    }
                    case "edit" -> {
                        Index index = getIndexAndHandleErrors(event, guild);
                        if (index == null) return;

                        String title = event.getOption("title", OptionMapping::getAsString);
                        @Nullable String newTitle = event.getOption("new_title", OptionMapping::getAsString);
                        @Nullable String newContent = event.getOption("new_content", OptionMapping::getAsString);
                        @Nullable String newUrl = event.getOption("new_url", OptionMapping::getAsString);
                        @Nullable String newQuery = event.getOption("new_query", OptionMapping::getAsString);
                        @Nullable Double newMinimumConfidence = event.getOption("new_minimum_confidence", OptionMapping::getAsDouble);

                        if (title == null) {
                            missingOption(event, "title");
                            return;
                        }

                        // If nothing is changed, don't go any further
                        if (newTitle == null && newContent == null && newUrl == null && newQuery == null && newMinimumConfidence == null) {
                            event.reply("You didn't specify what you want to change! You have to add the arguments that you want to change.").setEphemeral(true).queue();
                            return;
                        }

                        // Don't create an answer with the same name as another
                        for (Answer answer : index.answers) {
                            if (Objects.equals(answer.title, newTitle)) {
                                event.reply("There's already an answer with the title you want to set. If there are two, I won't know which one you're talking about!").setEphemeral(true).queue();
                                return;
                            }
                        }

                        // Edit answer on index
                        try {

                            List<Index> indexList = indexManager.getIndexes(guild);
                            int i = getPositionOfIndex(indexList, index);
                            if (i == -1) {
                                event.reply("I know which index you're talking about but I can't find it in the list for some reason. Something has gone horribly wrong here, but it's probably not your fault. You may want to reach out for support.").setEphemeral(true).queue();
                                return;
                            }
                            Answer editingAnswer = null;
                            for (Answer answer : index.answers) {
                                if (answer.title.equals(title)) {
                                    editingAnswer = answer;
                                    break;
                                }
                            }
                            if (editingAnswer == null) {
                                event.reply("I can't find an answer with the title \"" + title + "\". Maybe check that it's spelled correctly?").setEphemeral(true).queue();
                                return;
                            }

                            if (newTitle != null) editingAnswer.title = newTitle;
                            if (newContent != null) editingAnswer.content = newContent;
                            if (newUrl != null) editingAnswer.url = newUrl;
                            if (newQuery != null) editingAnswer.query = List.of(newQuery.split(";"));;
                            if (newMinimumConfidence != null) editingAnswer.minimumConfidence = newMinimumConfidence;

                            indexList.set(i, index);
                            indexManager.save(guild, indexList);
                            event.reply("Done! The answer was successfully edited.").addEmbeds(buildAnswerEmbed(editingAnswer).build()).setEphemeral(true).queue();
                        } catch (IOException e) {
                            ioError(event, e.getMessage());
                        }
                    }
                    case "info" -> {
                        Index index = getIndexAndHandleErrors(event, guild);
                        if (index == null) return;

                        String title = event.getOption("title", OptionMapping::getAsString);
                        if (title == null) {
                            missingOption(event, "title");
                            return;
                        }

                        Answer answer = null;
                        for (Answer anAnswer : index.answers) {
                            if (anAnswer.title.equals(title)) {
                                answer = anAnswer;
                                break;
                            }
                        }
                        if (answer == null) {
                            event.reply("I couldn't find an answer with that title.").setEphemeral(true).queue();
                            return;
                        }

                        event.reply("Here's a report of that answer!").addEmbeds(buildAnswerEmbed(answer).build()).setEphemeral(true).queue();
                    }
                    case "add_query" -> {
                        Index index = getIndexAndHandleErrors(event, guild);
                        if (index == null) return;

                        String title = event.getOption("title", OptionMapping::getAsString);
                        String query = event.getOption("query", OptionMapping::getAsString);

                        if (title == null) {
                            missingOption(event, "title");
                            return;
                        }
                        if (query == null) {
                            missingOption(event, "query");
                            return;
                        }

                        // If nothing is changed, don't go any further
                        if (query.isBlank()) {
                            event.reply("You didn't include any new queries, so I can't add anything.").setEphemeral(true).queue();
                            return;
                        }

                        // Edit answer on index
                        try {

                            List<Index> indexList = indexManager.getIndexes(guild);
                            int i = getPositionOfIndex(indexList, index);
                            if (i == -1) {
                                event.reply("I know which index you're talking about but I can't find it in the list for some reason. Something has gone horribly wrong here, but it's probably not your fault. You may want to reach out for support.").setEphemeral(true).queue();
                                return;
                            }
                            Answer editingAnswer = null;
                            for (Answer answer : index.answers) {
                                if (answer.title.equals(title)) {
                                    editingAnswer = answer;
                                    break;
                                }
                            }
                            if (editingAnswer == null) {
                                event.reply("I can't find an answer with the title \"" + title + "\". Maybe check that it's spelled correctly?").setEphemeral(true).queue();
                                return;
                            }

                            editingAnswer.query.addAll(List.of(query.split(";")));

                            indexList.set(i, index);
                            indexManager.save(guild, indexList);
                            event.reply("Done! The answer was successfully edited.").addEmbeds(buildAnswerEmbed(editingAnswer).build()).setEphemeral(true).queue();
                        } catch (IOException e) {
                            ioError(event, e.getMessage());
                        }
                    }
                    case null, default -> doesNotExist(event);
                }
            }
            case null -> {
                switch (event.getSubcommandName()) {
                    case "list" -> {
                        List<Index> indexes;
                        try {
                            indexes = indexManager.getIndexes(guild);
                        } catch (IOException e) {
                            ioError(event, e.getMessage());
                            return;
                        }
                        if (indexes == null || indexes.isEmpty()) {
                            event.reply("I don't have any indexes! You need to create one.").setEphemeral(true).queue();
                            return;
                        }
                        StringBuilder builder = new StringBuilder("Here are all the indexes I have.");
                        for (Index index : indexes) {
                            if (index == null) continue;
                            int answerCount = index.answers.size();
                            builder.append("\n- **").append(index.name).append("** with ")
                                    .append(answerCount).append(" answser");
                            if (answerCount != 1) builder.append("s");
                            builder.append(".");
                        }

                        event.reply(builder.toString()).setEphemeral(true).queue();
                    }
                    case "create" -> {
                        String name = event.getOption("name", OptionMapping::getAsString);
                        @Nullable GuildChannelUnion channel = event.getOption("channel", OptionMapping::getAsChannel);
                        if (name == null) {
                            missingOption(event, "name");
                        }

                        try {
                            List<Index> indexes = indexManager.getIndexes(guild);
                            for (Index index : indexes) {
                                if (index.name.equals(name)) {
                                    event.reply("There's already an index with that name. If there are two, I won't know which one you're talking about!").setEphemeral(true).queue();
                                    return;
                                }
                            }

                            Index newIndex = new Index();
                            newIndex.name = name;
                            newIndex.answers = new ArrayList<>();
                            newIndex.channels = new ArrayList<>();
                            if (channel != null) newIndex.channels.add(channel.getIdLong());

                            indexes.add(newIndex);
                            indexManager.save(guild, indexes);
                            
                            if (channel == null) event.reply("Sure thing! I've created a new index with the name \"" + name + "\"!").setEphemeral(true).queue();
                            else event.reply("Sure thing! I've created a new index with the name \"" + name + "\" and linked it to " + channel.getAsMention()).setEphemeral(true).queue();
                            
                        } catch (IOException e) {
                            ioError(event, e.getMessage());
                        }
                    }
                    case "delete" -> {
                        Index index = getIndexAndHandleErrors(event, guild);
                        if (index == null) return;

                        try {
                            List<Index> indexes = indexManager.getIndexes(guild);
                            boolean removed = indexes.removeIf(thisIndex -> thisIndex.name.equals(index.name));
                            if (!removed) {
                                event.reply("Well, something's gone very wrong here because I know which index you're talking about but I wasn't able to remove it for some reason. You might want to check the JSON because this should be impossible.").setEphemeral(true).queue();
                                return;
                            }
                            indexManager.save(guild, indexes);
                            event.reply("You got it. I have removed the " + index.name + " index and all of its answers.").setEphemeral(true).queue();
                        } catch (IOException e) {
                            ioError(event, e.getMessage());
                        }
                    }
                    case null, default -> doesNotExist(event);
                }
            }
            default -> doesNotExist(event);
        }
    }

    private static int getPositionOfIndex(@NotNull List<Index> indexList, Index index) {
        int i = -1;
        for (int j = 0; j < indexList.size(); j++) {
            if (indexList.get(j).name.equals(index.name)) {
                i = j;
                break;
            }
        }
        return i;
    }

    @Nullable
    private Index getIndexAndHandleErrors(SlashCommandInteractionEvent event, Guild guild) {
        Optional<Index> index;
        try {
            index = getIndex(event, guild);
        } catch (IOException e) {
            ioError(event, e.getMessage());
            return null;
        } catch (NullPointerException e) {
            missingOption(event, "index");
            return null;
        }
        if (index.isEmpty()) {
            indexNotFound(event);
            return null;
        }
        return index.get();
    }

    @NotNull
    private static EmbedBuilder buildAnswerEmbed(@NotNull Answer answer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        String title = Objects.requireNonNullElse(answer.title, "null (maybe check the JSON?)");
        embedBuilder.setTitle(title);

        String content = Objects.requireNonNullElse(answer.content, "null (maybe check the JSON?)");
        embedBuilder.addField("Content", content, false);

        String url = Objects.requireNonNullElse(answer.url, "No URL");
        embedBuilder.addField("URL", url, false);

        String queryRef = Arrays.toString(answer.query.toArray());
        embedBuilder.addField("Query Reference", queryRef, false);

        embedBuilder.addField("Minimum Confidence", Math.round(answer.minimumConfidence * 100) + "%", false);
        return embedBuilder;
    }

    private void indexNotFound(@NotNull SlashCommandInteractionEvent event) {
        event.reply("Can you double-check your spelling? I can't find an index with the name \"" + event.getOption("index", OptionMapping::getAsString) + "\".").setEphemeral(true).queue();
    }

    @NotNull
    private Optional<Index> getIndex(@NotNull SlashCommandInteractionEvent event, Guild guild) throws IOException {
        String indexName = event.getOption("index", OptionMapping::getAsString);
        if (indexName == null) throw new NullPointerException("Index option is null");
        return indexManager.getIndexes(guild).stream().filter(thisIndex -> thisIndex.name.equals(indexName)).findFirst();
    }

    @Override
    public void autoComplete(@NotNull CommandAutoCompleteInteractionEvent event) {

        Guild guild = event.getGuild();
        assert guild != null;

        if (event.getFocusedOption().getName().equals("index")) {

            Stream<String> indexNames;
            
            try {
                indexNames = indexManager.getIndexes(guild).stream().map(index -> index.name);
            } catch (IOException e) {
                return;
            }

            List<Command.Choice> options = indexNames
                    .filter(word -> word.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                    .map(word -> new Command.Choice(word, word)) // map the words to choices
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        } else if (event.getFocusedOption().getName().equals("title")) {

            String index = event.getOption("index", OptionMapping::getAsString);

            List<Index> indexes = null;
            try {
                indexes = indexManager.getIndexes(guild);
            } catch (IOException ignored) {}
            if (indexes == null) return;
            for (Index index1 : indexes) {
                if (index1.name.equals(index)) {
                    List<Answer> answers = index1.answers;
                    Stream<String> answerTitles = answers.stream().map(answer -> answer.title);
                    List<Command.Choice> options = answerTitles
                            .filter(word -> word.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                            .map(word -> new Command.Choice(word, word)) // map the words to choices
                            .toList();
                    event.replyChoices(options).queue();
                    return;
                }
            }


        }
    }

}
