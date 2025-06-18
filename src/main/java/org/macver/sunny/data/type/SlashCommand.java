package org.macver.sunny.data.type;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

public interface SlashCommand {

    SlashCommandData getData();

    void execute(SlashCommandInteractionEvent event);

    void autoComplete(CommandAutoCompleteInteractionEvent event);

    default void doesNotExist(@NotNull SlashCommandInteractionEvent event) {
        event.reply("The command you requested—it doesn't exist in the code! How did you manage to do that?").setEphemeral(true).queue();
    }

    default void missingOption(@NotNull SlashCommandInteractionEvent event, @NotNull String optionName) {
        event.reply("Sorry... I can do that, but to execute that command, you need to include the " + optionName + " option. I don't even think you should have been able to *not* include it!").setEphemeral(true).queue();
    }

    default void ioError(@NotNull SlashCommandInteractionEvent event, String message) {
        event.reply("I tried to execute the command but there was a problem when I was accessing my files. It's my fault, not yours! The error says \"" + message + "\".").setEphemeral(true).queue();
    }

}
