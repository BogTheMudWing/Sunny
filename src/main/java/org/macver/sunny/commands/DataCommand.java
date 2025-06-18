package org.macver.sunny.commands;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;
import org.macver.sunny.data.GuildManager;
import org.macver.sunny.data.type.GuildConfiguration;
import org.macver.sunny.data.type.SlashCommand;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public class DataCommand implements SlashCommand {

    GuildManager guildManager = new GuildManager();

    @Override
    public SlashCommandData getData() {
        return Commands.slash("data", "Manage Sunny's raw data.")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addSubcommands(
                        new SubcommandData("export", "Export your guild's data as a JSON file."),
                        new SubcommandData("import", "Import guild data from a JSON file.")
                                .addOptions(
                                        new OptionData(OptionType.ATTACHMENT, "file", "The JSON file to import.", true)
                                )
                );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        assert guild != null;

        if (Objects.equals(event.getSubcommandName(), "export")) {
            event.reply("Here is your data!").addFiles(FileUpload.fromData(guildManager.getFile(guild))).setEphemeral(true).queue();
        } else if (Objects.equals(event.getSubcommandName(), "import")) {
            Message.Attachment file = event.getOption("file", OptionMapping::getAsAttachment);
            if (file == null) {
                missingOption(event, "file");
                return;
            }
            if (file.getContentType() != null && !file.getContentType().contains("application/json")) {
                event.reply("I'm not sure that's a JSON file. It needs to end in `.json`.").setEphemeral(true).queue();
                return;
            }

            // Downlaod the file
            URL url = null;
            try {
                url = URL.of(URI.create(file.getUrl()), null);
            } catch (MalformedURLException e) {
                event.reply("That's awkward... the URL that Discord gave me doesn't work. It's probably an issue on Discord's end, so maybe try again in a little bit. The error says \"" + e.getMessage() + "\" but I'm not sure what that means.").setEphemeral(true).queue();
                return;
            }
            BufferedInputStream bufferedInputStream;
            try {
                bufferedInputStream = new BufferedInputStream(url.openStream());
            } catch (IOException e) {
                event.reply("I think my internet might be spotty right now. Or it could be Discord. I'm not able to open the URL right now. Maybe try again in a little bit? The error says \"" + e.getMessage() + "\" but I'm not sure what that means.").setEphemeral(true).queue();
                return;
            }
            File importFile = new File(guild.getId() + "-import.json");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(importFile);
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while (true) {
                    try {
                        if ((bytesRead = bufferedInputStream.read(dataBuffer, 0, 1024)) == -1) break;
                    } catch (IOException e) {
                        event.reply("I think my internet might be spotty right now. Or it could be Discord. I'm not able to download the entire file from the URL right now. Maybe try again in a little bit? The error says \"" + e.getMessage() + "\" but I'm not sure what that means.").setEphemeral(true).queue();
                        return;
                    }
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                ioError(event, e.getMessage());
            }

            // Make sure it's valid
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.readValue(importFile, new TypeReference<GuildConfiguration>() {
                });
            } catch (StreamReadException e) {
                event.reply("Wait a minute... this isn't JSON at all! There's been a horrible mistake. This must be the wrong file. I need a JSON text file.").setEphemeral(true).queue();
                return;
            } catch (DatabindException e) {
                event.reply("Your JSON isn't structured properly. Here's the error:\n" + e.getMessage() + "\nYou might want to take a look at the documentation at https://sunny.macver.org/json.").setEphemeral(true).queue();
                return;
            } catch (IOException e) {
                ioError(event, e.getMessage());
            }

            try {
                Files.delete(guildManager.getFile(guild).toPath());
                Files.move(importFile.toPath(), guildManager.getFile(guild).toPath());
            } catch (IOException e) {
                event.reply("Hmm... I was able to download the file and verify that it is structured correctly, but for some reason I can't put it in its place. This is probably an error on my end, so maybe try again later? The error says \"" + e.getMessage() + "\"").setEphemeral(true).queue();
                return;
            }

            event.reply("I was successfully able to download, verify, and import your file! Thank you!").setEphemeral(true).queue();

        } else {
            doesNotExist(event);
        }
    }

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {

    }
}
