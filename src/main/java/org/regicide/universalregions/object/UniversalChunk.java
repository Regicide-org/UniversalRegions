package org.regicide.universalregions.object;

import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.regicide.universalregions.UniversalRegions;
import org.regicide.universalregions.storage.UniversalRegionStorage;

import java.io.File;
import java.io.IOException;

/**
 * Represents a chunk.
 */
public class UniversalChunk {
    private final String ID;
    private final Chunk chunk;
    private UniversalRegion universalRegion;

    /**
     * Constructor.
     * @param chunk The {@link Chunk}.
     */
    public UniversalChunk(@NotNull final Chunk chunk) {
        this.chunk = chunk;
        this.ID = chunk.getWorld().getName() + "_" + chunk.getX() + "_" + chunk.getZ();
    }

    /**
     * @return The {@link Chunk}.
     */
    public Chunk getChunk() {
        return chunk;
    }

    /**
     * @return The identifier.
     */
    public String getID() {
        return ID;
    }

    public void setUniversalRegion(@NotNull final UniversalRegion universalRegion) {
        this.universalRegion = universalRegion;
    }

    public void setUniversalRegion(@NotNull final String ID) {
        this.universalRegion = UniversalRegionStorage.getRegion(ID);
    }

    public UniversalRegion getUniversalRegion() {
        return universalRegion;
    }

    public boolean isRegionPart() {
        return (universalRegion != null);
    }

    public void saveData() throws IOException {
        File file = new File(UniversalRegions.instance().getDataFolder() +
                File.separator + "data" + File.separator + "universal-chunks" +
                File.separator + this.chunk.getWorld().getName(), this.ID + ".yml");
        FileConfiguration config = new YamlConfiguration();

        config.set("ID", ID);
        if (this.isRegionPart())
            config.set("region-part", universalRegion.getID());

        config.save(file);
        file.createNewFile();
    }

    public void saveAsync() {

    }
}
