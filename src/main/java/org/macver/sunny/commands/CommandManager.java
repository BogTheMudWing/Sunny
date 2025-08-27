package org.macver.sunny.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.macver.sunny.Reporter;
import org.macver.sunny.Sunny;
import org.macver.sunny.data.type.SlashCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    private final List<SlashCommand> commands = new ArrayList<>();

    public CommandManager(JDA jda, List<SlashCommand> commands) {
        this.commands.addAll(commands);
    }

    public CommandManager(@NotNull JDA jda, SlashCommand... commands) {
        this.commands.addAll(List.of(commands));
        for (SlashCommand command : commands) {
            for (Guild guild : jda.getGuilds()) {
                guild.upsertCommand(command.getData()).queue();
            }
            if (command instanceof EventListener) jda.addEventListener(command);
        }
    }

    public void addCommandsToGuild(Guild guild) {
        for (SlashCommand command : commands) {
            guild.upsertCommand(command.getData()).queue();
        }
    }

    public List<SlashCommand> getCommands() {
        return commands;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (SlashCommand command : commands) {
            if (event.getName().equals(command.getData().getName())) {
                command.execute(event);
                return;
            }
        }
        event.reply("The command you requested—it doesn't exist in the code! How did you manage to do that?").setEphemeral(true).queue();
        Sunny.logger.error("Command {} was requested but doesn't have an executor.", event.getName());
        new Reporter().report("Command " + event.getName() + " was requested but doesn't have an executor.");
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        for (SlashCommand command : commands) {
            if (event.getName().equals(command.getData().getName())) {
                command.autoComplete(event);
                return;
            }
        }
        Sunny.logger.error("Command {} autocompletion was requested but doesn't have an executor.", event.getName());
        new Reporter().report("Command " + event.getName() + " autocompletion was requested but doesn't have an executor.");
    }

}
