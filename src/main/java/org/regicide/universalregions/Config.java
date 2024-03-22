package org.regicide.universalregions;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Configuration of the {@link UniversalRegions} plugin.
 */
public final class Config {
    private final String defaultLocalisation;
    private final boolean isLocalisationClientBased;
    private final String databaseLoadType;
    private final String databaseSaveType;
    private final List<String> regionWorlds;

    public Config() throws IOException, InvalidConfigurationException {
        File file;
        FileConfiguration fileConfig;

        file = new File(UniversalRegions.instance().getDataFolder() + File.separator + "settings",
                "configuration.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            UniversalRegions.instance().saveResource("settings"+ File.separator +"configuration.yml", false);
        }

        fileConfig = new YamlConfiguration();
        fileConfig.load(file);

        this.defaultLocalisation = fileConfig.getString("language.default-localisation");
        this.isLocalisationClientBased = fileConfig.getBoolean("language.client-based");

        this.databaseLoadType = fileConfig.getString("plugin.database.database-load");
        this.databaseSaveType = fileConfig.getString("plugin.database.database-save");
        this.regionWorlds = (List<String>) fileConfig.getList("plugin.region-worlds");
    }

    public String getDefaultLocalisation() {
        return defaultLocalisation;
    }

    public boolean isLocalisationClientBased() {
        return isLocalisationClientBased;
    }

    public String getDatabaseLoadType() {
        return databaseLoadType;
    }

    public String getDatabaseSaveType() {
        return databaseSaveType;
    }

    public List<String> getRegionWorlds() {
        return regionWorlds;
    }
}
