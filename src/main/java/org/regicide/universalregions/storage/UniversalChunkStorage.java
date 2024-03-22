package org.regicide.universalregions.storage;

import org.bukkit.Chunk;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.regicide.universalregions.UniversalRegions;
import org.regicide.universalregions.events.chunks.UniversalChunkLoadEvent;
import org.regicide.universalregions.exceptions.AlreadyExistingElementException;
import org.regicide.universalregions.object.UniversalChunk;
import org.regicide.universalregions.object.UniversalRegion;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Represents the universal chunks that universal region contains.
 */
public final class UniversalChunkStorage {

    private final static Map<String, UniversalChunk> UNIVERSAL_CHUNK_MAP = new HashMap<>();

    /**
     * Add new chunk to universal chunk map.
     * @param universalChunk The {@link UniversalChunk}.
     * @throws AlreadyExistingElementException If {@link UniversalChunk} already contains in universal chunk map.
     */
    public static void add(@NotNull final UniversalChunk universalChunk) throws AlreadyExistingElementException {
        if (UNIVERSAL_CHUNK_MAP.containsKey(universalChunk.getID()))
                throw new AlreadyExistingElementException();
        UNIVERSAL_CHUNK_MAP.put(universalChunk.getID(), universalChunk);
    }

    /**
     * Removes {@link Chunk} from the universal chunk map.
     * @param universalChunk The {@link Chunk}.
     * @throws NoSuchElementException If {@link UniversalChunk} map doesn't contain specified key.
     */
    public static void remove(@NotNull final UniversalChunk universalChunk) throws NoSuchElementException {
        if (!(UNIVERSAL_CHUNK_MAP.containsKey(universalChunk.getID())))
            throw new NoSuchElementException();
        UNIVERSAL_CHUNK_MAP.remove(universalChunk.getID());
    }

    /**
     * Remove {@link Chunk} from the universal chunk map.
     * @param ID The identifier of the {@link Chunk}
     * @throws NoSuchElementException If {@link UniversalChunk} map doesn't contain specified key.
     */
    public static void remove(@NotNull final String ID) throws NoSuchElementException {
        if (!(UNIVERSAL_CHUNK_MAP.containsKey(ID)))
            throw new NoSuchElementException();
        UNIVERSAL_CHUNK_MAP.remove(ID);
    }

    /**
     * @param chunk The default {@link Chunk}.
     * @return The {@link UniversalChunk}.
     * @throws NoSuchElementException If universal chunk map doesn't contain {@link UniversalChunk}.
     */
    public static UniversalChunk getUniversalChunk(@NotNull final Chunk chunk) throws NoSuchElementException {
        String ID = chunk.getWorld().getName() + "_" + chunk.getX() + "_" + chunk.getZ();
        if (!(UNIVERSAL_CHUNK_MAP.containsKey(ID)))
            throw new NoSuchElementException();
        return UNIVERSAL_CHUNK_MAP.get(ID);
    }

    /**
     * @param ID The identifier of the {@link UniversalChunk}.
     * @return The {@link UniversalChunk}.
     * @throws NoSuchElementException If universal chunk map doesn't contain {@link UniversalChunk} with specified ID.
     */
    public static UniversalChunk getUniversalChunk(@NotNull final String ID) throws NoSuchElementException {
        if (!(UNIVERSAL_CHUNK_MAP.containsKey(ID)))
            throw new NoSuchElementException();
        return UNIVERSAL_CHUNK_MAP.get(ID);
    }

    /**
     * @param chunk The default {@link Chunk}.
     * @return The identifier form for the {@link UniversalChunk}.
     */
    public static String getID(@NotNull final Chunk chunk) {
       return chunk.getWorld().getName() + "_" + chunk.getX() + "_" + chunk.getZ();
    }

    public static boolean isUniversalChunk(@NotNull final Chunk chunk) {
        return UNIVERSAL_CHUNK_MAP.containsKey(getID(chunk));
    }

    public static void loadUniversalChunk(@NotNull final String ID, @NotNull final Chunk chunk, @NotNull final UniversalRegion uReg) throws NullPointerException, IllegalArgumentException, IOException, InvalidConfigurationException {
        String loadingType = UniversalRegions.config().getDatabaseLoadType();
        switch (loadingType) {
            case "flatfile": {
                File chunkFile = new File(UniversalRegions.instance().getDataFolder() +
                        File.separator + "data" + File.separator + "universal-chunks" + File.separator +
                        chunk.getWorld().getName() + File.separator + ID + ".yml");

                FileConfiguration fileConfig = new YamlConfiguration();
                fileConfig.load(chunkFile);

                UniversalChunk uChunk = new UniversalChunk(chunk);
                uChunk.setUniversalRegion(uReg);

                UniversalChunkLoadEvent event = new UniversalChunkLoadEvent(uChunk);
                if (event.isCancelled())
                    return;

                UNIVERSAL_CHUNK_MAP.put(uChunk.getID(), uChunk);
                break;
            }
            default: throw new IllegalArgumentException();
        }
    }
}
