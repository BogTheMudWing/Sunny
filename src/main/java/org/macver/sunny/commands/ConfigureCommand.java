package org.macver.sunny.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.macver.sunny.data.GuildManager;
import org.macver.sunny.data.type.SlashCommand;
import org.macver.sunny.data.type.GuildConfiguration;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigureCommand extends ListenerAdapter implements SlashCommand {

    GuildManager guildManager = new GuildManager();

    @Override
    public SlashCommandData getData() {
        return Commands.slash("configure", "Configure Sunny.")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addSubcommandGroups(
                        new SubcommandGroupData("no_correction_phrases", "Manage phrases that should not be autocorrected.")
                                .addSubcommands(
                                        new SubcommandData("list", "List all phrases with should not be autocorrected."),
                                        new SubcommandData("add", "Add a phrase which should be autocorrected.")
                                                .addOptions(
                                                        new OptionData(OptionType.STRING, "phrase", "The phrase to not autocorrect.", true)
                                                ),
                                        new SubcommandData("remove", "Remove a phrase which should not be autocorrected")
                                                .addOptions(
                                                        new OptionData(OptionType.STRING, "phrase", "The phrase to remove.", true, true)
                                                )
                                )
                )
                .addSubcommands(
                        new SubcommandData("erase", "Erase all data.")
                );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        assert guild != null;

        if (Objects.equals(event.getSubcommandGroup(), "no_correction_phrases")) {
            switch (event.getSubcommandName()) {
                case "list" -> {
                    List<String> noCorrectionPhrases;
                    try {
                        noCorrectionPhrases = guildManager.getGuildConfiguration(guild).noCorrectionPhrases;
                    } catch (IOException e) {
                        ioError(event, e.getMessage());
                        return;
                    }
                    if (noCorrectionPhrases == null || noCorrectionPhrases.isEmpty()) {
                        event.reply("You haven't given any phrases yet that you don't want me to correct.").setEphemeral(true).queue();
                        return;
                    }
                    StringBuilder stringBuilder = new StringBuilder("Here are all the phrases you have asked me not to correct:");
                    for (String phrase : noCorrectionPhrases) {
                        stringBuilder.append("\n- ").append(phrase);
                    }
                    event.reply(stringBuilder.toString()).setEphemeral(true).queue();
                }
                case "add" -> {
                    String phrase = event.getOption("phrase", OptionMapping::getAsString);
                    if (phrase == null) {
                        missingOption(event, "phrase");
                        return;
                    }
                    if (phrase.isBlank()) {
                        event.reply("Uh... sorry, but this is a blank string. I'm not even sure what I correct this to.").setEphemeral(true).queue();
                    }

                    GuildConfiguration guildConfiguration;
                    try {
                        guildConfiguration = guildManager.getGuildConfiguration(guild);
                        guildConfiguration.noCorrectionPhrases.add(phrase);
                        guildManager.saveGuildConfiguration(guild, guildConfiguration);
                    } catch (IOException e) {
                        ioError(event, e.getMessage());
                        return;
                    }

                    event.reply("Sounds good! I won't try to correct the phrase \"" + phrase + "\" anymore.").setEphemeral(true).queue();

                }
                case "remove" -> {
                    String phrase = event.getOption("phrase", OptionMapping::getAsString);
                    if (phrase == null) {
                        missingOption(event, "phrase");
                        return;
                    }
                    if (phrase.isBlank()) {
                        event.reply("Uh... sorry, but this is a blank string. I wasn't trying to change any of those anyway.").setEphemeral(true).queue();
                    }

                    GuildConfiguration guildConfiguration;
                    try {
                        guildConfiguration = guildManager.getGuildConfiguration(guild);
                        boolean remove = guildConfiguration.noCorrectionPhrases.remove(phrase);
                        if (!remove) {
                            event.reply("You're in luck! I don't have that phrase on my *don't-correct-these-phrases* list.").setEphemeral(true).queue();
                            return;
                        }
                        guildManager.saveGuildConfiguration(guild, guildConfiguration);
                    } catch (IOException e) {
                        ioError(event, e.getMessage());
                        return;
                    }

                    event.reply("Sure thing! I will try to autocorrect the phrase \"" + phrase + "\" just like everything else.").setEphemeral(true).queue();

                }
                case null, default -> doesNotExist(event);
            }
        } else {
            if (Objects.equals(event.getSubcommandName(), "erase")) {
                event.replyModal(Modal.create("erase", "Erase all data").addComponents(ActionRow.of(
                        TextInput.create("confirmation", "Type \"ERASE ALL DATA\" to confirm.", TextInputStyle.SHORT)
                        .setPlaceholder("ERASE ALL DATA")
                        .setMinLength("ERASE ALL DATA".length())
                        .setMaxLength("ERASE ALL DATA".length())
                        .build()
                )).build()).queue();
            }
        }
    }

    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
        if (event.getModalId().equals("erase")) {
            String confirmation = Objects.requireNonNull(event.getValue("confirmation")).getAsString();

            if (confirmation.equals("ERASE ALL DATA")) {
                GuildManager guildManager = new GuildManager();
                Guild guild = event.getGuild();
                assert guild != null;
                GuildConfiguration configuration;
                try {

                    configuration = guildManager.getGuildConfiguration(guild);
                } catch (IOException e) {
                    event.reply("Data was not erased because there was an I/O error: " + e.getMessage()).setEphemeral(true).queue();
                    return;
                }
                configuration.indexes = new ArrayList<>();;
                configuration.noCorrectionPhrases = new ArrayList<>();
                try {
                    guildManager.saveGuildConfiguration(guild, configuration);
                } catch (IOException e) {
                    event.reply("Data was not erased because there was an I/O error: " + e.getMessage()).setEphemeral(true).queue();
                    return;
                }

                event.reply("Successfully erased all guild data.").setEphemeral(true).queue();
            } else {
                event.reply("Data was not erased because confirmation did not match \"ERASE ALL DATA\".").setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {

    }
}
