package org.regicide.universalregions.util;

import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.regicide.universalregions.UniversalRegions;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FileHelper {

    @SuppressWarnings("ConstantConditions")
    public static HashMap<String, String> getMapStrings(ConfigurationSection section) {
        HashMap<String, String> map = new HashMap<>();
        for (String k : section.getKeys(false)) {
            String v = section.getString(section.getString(k));
            map.put(k,v);
        }
        return map;
    }

    public static HashMap<String, Integer> getMapInt(ConfigurationSection section) {
        HashMap<String, Integer> map = new HashMap<>();
        for (String k : section.getKeys(false)) {
            int v = section.getInt(section.getString(k));
            map.put(k,v);
        }
        return map;
    }

    public static HashMap<String, Double> getMapDouble(ConfigurationSection section) {
        HashMap<String, Double> map = new HashMap<>();
        for (String k : section.getKeys(false)) {
            double v = section.getDouble(section.getString(k));
            map.put(k,v);
        }
        return map;
    }
    @SuppressWarnings("ConstantConditions")
    public static Chunk getChunkByID(@NotNull final Server server, @NotNull final String ID) {

        Matcher m = Pattern.compile("^(\\w*)_(-*\\d*[0-9])_(-*\\d*[0-9])$").matcher(ID);
        String worldName = null;
        int x = 0;
        int z = 0;
        while (m.find()) {
            worldName = m.group(1);
            x = Integer.parseInt(m.group(2));
            z = Integer.parseInt(m.group(3));
        }

        World w = server.getWorld(worldName);

        return w.getChunkAt(x,z);
    }

}
