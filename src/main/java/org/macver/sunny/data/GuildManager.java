package org.macver.sunny.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.macver.sunny.data.type.GuildConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GuildManager {

    public GuildConfiguration getGuildConfiguration(@NotNull Guild guild) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File guildConfig = new File("indexes/" + guild.getId() + ".json");
        if (Files.exists(guildConfig.toPath())) {
            return mapper.readValue(guildConfig, new TypeReference<>() {
            });
        } else {
            saveGuildConfiguration(guild, new GuildConfiguration());
            return new GuildConfiguration();
        }

    }

    public void saveGuildConfiguration(@NotNull Guild guild, @NotNull GuildConfiguration configuration) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File("indexes/" + guild.getId() + ".json"), configuration);
    }

    public File getFile(@NotNull Guild guild) {
        return new File("indexes/" + guild.getId() + ".json");
    }
}
