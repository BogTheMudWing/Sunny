package org.macver.sunny.data;

import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.macver.sunny.data.type.GuildConfiguration;
import org.macver.sunny.data.type.Index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndexManager {

    private final GuildManager guildManager = new GuildManager();

    public List<Index> getIndexes(@NotNull Guild guild) throws IOException {
        List<Index> indexes = guildManager.getGuildConfiguration(guild).indexes;
        if (indexes == null) return new ArrayList<>();
        return indexes;
    }

    public void save(Guild guild, List<Index> indexList) throws IOException {
        GuildConfiguration guildConfiguration = guildManager.getGuildConfiguration(guild);
        guildConfiguration.indexes = indexList;
        guildManager.saveGuildConfiguration(guild, guildConfiguration);
    }
}
