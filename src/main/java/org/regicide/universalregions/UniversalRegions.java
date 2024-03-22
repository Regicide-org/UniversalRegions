package org.regicide.universalregions;

import dev.jorel.commandapi.CommandAPI;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.regicide.universalregions.commands.UniversalRegionCMD;
import org.regicide.universalregions.commands.UniversalRegionsCMD;
import org.regicide.universalregions.object.UniversalRegion;
import org.regicide.universalregions.storage.UniversalRegionStorage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Main plugin's class.
 */
public final class UniversalRegions extends JavaPlugin {

    private static UniversalRegions instance;
    private static Config config;

    /**
     * Plugin startup logic.
     */
    @Override
    public void onEnable() {
        getLogger().info("");
        instance = this;

        // Config
        if (!configLoad()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Configuration was successfully loaded!");

        // Localization
        getLogger().info("");
        try {
            if (!localizationLoad()) {
                this.getServer().getPluginManager().disablePlugin(this);
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getLogger().info("Localization was successfully loaded!");

        // Data
        getLogger().info("");
        if (!loadData()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Data was successfully loaded!");

        //getLogger().info("Listeners successfully loaded!");

        // Commands
        getLogger().info("");
        new UniversalRegionCMD();
        getLogger().info("Commands successfully loaded!");
    }

    /**
     * Plugin disable logic.
     */
    @Override
    public void onDisable() {
    }

    /**
     * Plugin reload logic.
     */
    public void reload() {
        // Config
        if (!configLoad()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Configuration was successfully reloaded!");

        // Localization
        getLogger().info("");
        try {
            if (!localizationLoad()) {
                this.getServer().getPluginManager().disablePlugin(this);
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getLogger().info("Localization was successfully reloaded!");

        getLogger().info("");
        if (!loadData()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Data was successfully reloaded!");

        // Commands
        getLogger().info("");
        new UniversalRegionCMD();
        getLogger().info("Commands successfully reloaded!");
    }

    /**
     * @return The instance of the plugin.
     */
    public static UniversalRegions instance() {
        return instance;
    }

    public static Config config() { return config; }

    private boolean configLoad() {
        try {
            config = new Config();
        } catch (IOException | InvalidConfigurationException e) {
            UniversalRegions.instance().getLogger().severe("Critical error when creating the configuration file! Details below:");
            e.printStackTrace();
            UniversalRegions.instance().getLogger().severe("UniversalRegions will not work without configuration. Shutdown.");
            return false;
        }
        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean localizationLoad() throws IOException {
        File localizationDir = new File(this.getDataFolder(), "localization");
        File referenceDir = new File(localizationDir.getPath(), "reference");
        File overrideDir = new File(localizationDir, "override");

        localizationDir.mkdir();
        referenceDir.mkdir();
        overrideDir.mkdir();


        CodeSource src = this.getClass().getProtectionDomain().getCodeSource();
        URL jar = src.getLocation();
        ZipInputStream zip = new ZipInputStream(jar.openStream());
        while (true) {
            ZipEntry e = zip.getNextEntry();
            if (e == null)
                break;
            String fileFullName = e.getName();
            System.out.println(fileFullName);
            if ((fileFullName.startsWith("localization/reference/")) && fileFullName.endsWith(".properties")) {
                if (!new File(fileFullName).exists())
                    this.saveResource(fileFullName, false);
            }
        }

        File[] referenceLocalizations = referenceDir.listFiles();
        if (referenceLocalizations != null) {
            for (File locFile : referenceLocalizations) {
                String localeCode = locFile.getName().substring(0, locFile.getName().indexOf('.'));
                TranslationRegistry reg = TranslationRegistry.create(Key.key("unversalregions:" + localeCode));
                ResourceBundle bundle = ResourceBundle.getBundle("localization.reference." + localeCode, Locale.forLanguageTag(localeCode), UTF8ResourceBundleControl.get());
                reg.registerAll(Locale.forLanguageTag(localeCode), bundle, true);
                GlobalTranslator.translator().addSource(reg);
            }
        } else
            return false;

        return true;
        /*


        File[] overrideLocalizations = overrideDir.listFiles();
        if (overrideLocalizations != null) {
            for (File locFile : overrideLocalizations) {
                String localeCode = locFile.getName().substring(0, locFile.getName().indexOf('.'));
                TranslationRegistry reg = TranslationRegistry.create(Key.key("unversalregions:" + localeCode));

                ///idk

                ResourceBundle bundle = ResourceBundle.getBundle("localization.override." + localeCode, Locale.forLanguageTag(localeCode), UTF8ResourceBundleControl.get());
                reg.registerAll(Locale.forLanguageTag(localeCode), bundle, true);
                GlobalTranslator.translator().addSource(reg);
            }
        }
        return true;
         */
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean loadData() {
        File dataDir = new File(this.getDataFolder(), "data");
        File uRegDir = new File(dataDir.getPath(), "universal-regions");
        File uChunksDir = new File(dataDir.getPath(), "universal-chunks");

        dataDir.mkdir();
        uRegDir.mkdir();
        uChunksDir.mkdir();

        // universal chunks
        /*try {
            UniversalChunkStorage.LoadUniversalChunks();
        } catch (NullPointerException ignored) {
            // getLogger().info("There's no any universals chunks to load. If it's bug check your data folder.");
        } catch (IllegalArgumentException e) {
            getLogger().severe("The \"database-load\" setting contains an invalid value. Go to configuration.yml and change it to valid!");
            getLogger().severe("UniversalRegions will be disabled!");
            return false;
        }*/

        // universal regions & chunks
        // NOTE: The regions are loaded first, and then each region loads all universal chunks it contains.
        try {
            UniversalRegionStorage.loadUniversalRegions();
        } catch (NullPointerException ignored) {
            // getLogger().info("There's no any universals regions to load. If it's bug check your data folder.");
        } catch (IllegalArgumentException | IOException | InvalidConfigurationException e) {
            getLogger().severe("The \"database-load\" setting contains an invalid value. Go to configuration.yml and change it to valid!");
            getLogger().severe("UniversalRegions will be disabled!");
            return false;
        }

        return true;
    }
}
