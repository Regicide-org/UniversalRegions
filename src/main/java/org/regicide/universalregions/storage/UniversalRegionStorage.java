package org.regicide.universalregions.storage;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.regicide.universalregions.UniversalRegions;
import org.regicide.universalregions.exceptions.AlreadyExistingElementException;
import org.regicide.universalregions.object.UniversalChunk;
import org.regicide.universalregions.object.UniversalRegion;
import org.regicide.universalregions.util.FileHelper;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Store universal regions.
 */
public final class UniversalRegionStorage {
    private final static Map<String, UniversalRegion> UNIVERSAL_REGIONS_MAP = new HashMap<>();

    /**
     * Adds region to the universal region map.
     * @param universalRegion The region.
     * @throws AlreadyExistingElementException If region with such ID already in universal region map.
     */
    public static void addRegion(@NotNull final UniversalRegion universalRegion) throws AlreadyExistingElementException {
        if (UNIVERSAL_REGIONS_MAP.containsKey(universalRegion.getID()))
            throw new AlreadyExistingElementException();
        UNIVERSAL_REGIONS_MAP.put(universalRegion.getID(), universalRegion);
    }

    /**
     * Removes region from the universal region map.
     * @param ID The identifier of the region.
     * @throws NoSuchElementException If region with specified ID not in universal region map.
     */
    public static void removeRegion(@NotNull final String ID) throws NoSuchElementException {
        if (!(UNIVERSAL_REGIONS_MAP.containsKey(ID)))
            throw new NoSuchElementException();
        UNIVERSAL_REGIONS_MAP.remove(ID);
    }

    /**
     * Removes region from the universal region map.
     * @param universalRegion The region.
     * @throws NoSuchElementException If universal region map doesn't contain specified region.
     */
    public static void removeRegion(@NotNull final UniversalRegion universalRegion) throws NoSuchElementException {
        String ID = universalRegion.getID();
        if (!(UNIVERSAL_REGIONS_MAP.containsKey(ID)))
            throw new NoSuchElementException();
        UNIVERSAL_REGIONS_MAP.remove(ID);
    }

    /**
     * @param ID The identifier of the universal region.
     * @return The universal region or Null if there is no universal region with specified ID.
     * @throws NoSuchElementException If universal region map doesn't contain region with specified ID.
     */
    public static UniversalRegion getRegion(@NotNull final String ID) throws NoSuchElementException {
        if (!(UNIVERSAL_REGIONS_MAP.containsKey(ID)))
            throw new NoSuchElementException();
        return UNIVERSAL_REGIONS_MAP.get(ID);
    }

    /**
     * @param ID The identifier of the universal region.
     * @return True if universal region map contains region with specified ID and False if not.
     */
    public static boolean hasRegion(@NotNull final String ID) {
        return UNIVERSAL_REGIONS_MAP.containsKey(ID);
    }

    public static void loadUniversalRegions() throws NullPointerException, IllegalArgumentException, IOException, InvalidConfigurationException {
        UNIVERSAL_REGIONS_MAP.clear();

        String loadingType = UniversalRegions.config().getDatabaseLoadType();
        switch (loadingType) {
            case "flatfile": {

                File regionsDir = new File(UniversalRegions.instance().getDataFolder() + File.separator + "data" + File.separator + "universal-regions");
                File[] worldsOfRegions = regionsDir.listFiles();
                if (worldsOfRegions == null)
                    throw new NullPointerException();
                for (File worldDir : worldsOfRegions) {
                    File[] regionsArray = worldDir.listFiles();
                    if (regionsArray == null)
                        throw new NullPointerException();
                    for (File regFile : regionsArray)
                        flatFileLoadRegion(regFile);
                }
                break;
            }
            default: throw new IllegalArgumentException();
        }
    }
    @SuppressWarnings("ConstantConditions")
    private static void flatFileLoadRegion(File file) throws IOException, NullPointerException, InvalidConfigurationException {
        FileConfiguration fileConfig = new YamlConfiguration();
        fileConfig.load(file);

        String ID;
        World world;
        HashMap<String, String> names;
        HashMap<String, String> stringKeys;
        HashMap<String, Integer> intKeys;
        HashMap<String, Double> doubleKeys;
        HashSet<UniversalChunk> chunks = new HashSet<>();

        ID = fileConfig.getString("ID");
        world = UniversalRegions.instance().getServer().getWorld(fileConfig.getString("world"));
        names = FileHelper.getMapStrings(fileConfig.getConfigurationSection("locale-names"));
        stringKeys = FileHelper.getMapStrings(fileConfig.getConfigurationSection("keys-string"));
        intKeys = FileHelper.getMapInt(fileConfig.getConfigurationSection("keys-integer"));
        doubleKeys = FileHelper.getMapDouble(fileConfig.getConfigurationSection("keys-double"));
        List<String> chunksIDs = fileConfig.getStringList("chunks");

        UniversalRegion uRegion = new UniversalRegion(ID, world);

        uRegion.setNames(names);
        uRegion.setStringKeys(stringKeys);
        uRegion.setIntegerKeys(intKeys);
        uRegion.setDoubleKeys(doubleKeys);
        uRegion.setChunks(chunks);
        chunksIDs.forEach(chunkID -> {
            Chunk ch = FileHelper.getChunkByID(UniversalRegions.instance().getServer(), chunkID);
            try {
                UniversalChunkStorage.loadUniversalChunk(chunkID, ch, uRegion);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        });
        UNIVERSAL_REGIONS_MAP.put(ID, uRegion);
    }
}
