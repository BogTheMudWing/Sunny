package org.macver.sunny;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.macver.sunny.commands.CommandManager;
import org.macver.sunny.commands.ConfigureCommand;
import org.macver.sunny.commands.DataCommand;
import org.macver.sunny.commands.IndexCommand;
import org.macver.sunny.data.type.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Sunny {

    public static AppConfiguration config;
    public static JDA jda;
    public static final Logger logger = LoggerFactory.getLogger(Sunny.class);

    public static void main(String[] args) throws InterruptedException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        File config = new File("config.json");
        if (!Files.exists(config.toPath())) {
            logger.error("config.json does not exist.");
            return;
        }
        Sunny.config = mapper.readValue(config, new TypeReference<>() {
        });

        // Note: It is important to register your ReadyListener before building
        jda = JDABuilder.createDefault(Sunny.config.botToken)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT) // enables explicit access to message.getContentDisplay()
                .addEventListeners(new QueryResponder())
                .build();

        // optionally block until JDA is ready
        jda.awaitReady();

        final CommandManager commandManager = new CommandManager(jda,
                new ConfigureCommand(),
                new DataCommand(),
                new IndexCommand()
        );
        jda.addEventListener(commandManager);

        logger.info("{} commands registered.", commandManager.getCommands().size());
    }
}