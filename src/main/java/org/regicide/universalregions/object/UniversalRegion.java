package org.regicide.universalregions.object;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.regicide.universalregions.UniversalRegions;
import org.regicide.universalregions.exceptions.AlreadyExistingElementException;
import org.regicide.universalregions.exceptions.ImpossibleElementException;
import org.regicide.universalregions.exceptions.OverlayElementException;
import org.regicide.universalregions.storage.UniversalRegionStorage;
import org.regicide.universalregions.util.LocaleSet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Represents a region. Like, all this plugin about it...
 */
public class UniversalRegion {
    private final String ID;
    private final World world;
    private Map<String, String> names;

    private Map<String, String> StringKeys;
    private Map<String, Integer> IntegerKeys;
    private Map<String, Double> DoubleKeys;

    private HashSet<UniversalChunk> chunks;

    /**
     * Constructor.
     *
     * @param ID The Identifier of the {@link UniversalRegion}.
     * @param name The default name of the {@link UniversalRegion}.
     * @param uChunk The {@link UniversalChunk} in which the new {@link UniversalRegion} is created.
     * @throws AlreadyExistingElementException If {@link UniversalRegion} with such ID already exists.
     * @throws OverlayElementException If you are trying to create a {@link UniversalRegion} on the territory of a {@link Chunk} that already belongs to another region.
     */
    @SuppressWarnings("ConstantConditions")
    public UniversalRegion(@NotNull final String ID, @NotNull final String name, @NotNull final UniversalChunk uChunk)
            throws AlreadyExistingElementException, OverlayElementException, ImpossibleElementException, IOException {

        World w = uChunk.getChunk().getWorld();

        if (!UniversalRegions.config().getRegionWorlds().contains(w.getName()))
            throw new ImpossibleElementException();

        if (uChunk.isRegionPart())
            throw new OverlayElementException();

        if (UniversalRegionStorage.hasRegion(ID))
            throw new AlreadyExistingElementException();

        this.ID = ID;
        this.world = w;

        chunks = new HashSet<>();
        chunks.add(uChunk);
        uChunk.setUniversalRegion(this);

        this.names = new HashMap<>();
        names.put("default", name);

        StringKeys = new HashMap<>();
        IntegerKeys = new HashMap<>();
        DoubleKeys = new HashMap<>();
    }

    public UniversalRegion(@NotNull final String ID, @NotNull final World world) {
        this.ID = ID;
        this.world = world;
    }

    /**
     * @return The identifier of the region.
     */
    public String getID() {
        return ID;
    }

    /**
     * Adds new localised name for a universal region.
     * @param localeCode The locale code.
     * @param name The name of the {@link UniversalRegion}.
     * @throws NoSuchElementException If you specified un non-existent locale code in game.
     * @throws AlreadyExistingElementException If specified locale code already has a localized name defined.
     */
    public void addName(@NotNull final String localeCode, @NotNull final String name)
            throws NoSuchElementException, AlreadyExistingElementException {
        if (!names.containsKey(localeCode))
            throw new NoSuchElementException();

        if (!LocaleSet.hasLocaleCode(localeCode))
            throw new AlreadyExistingElementException();

        names.put(localeCode, name);
    }

    /**
     * Removes localised name from localised names map by locale code.
     * @param localeCode The locale code.
     * @throws NoSuchElementException If a localized name is not defined for a specified locale language code.
     */
    public void removeName(@NotNull final String localeCode) throws NoSuchElementException {
        if (!names.containsKey(localeCode))
            throw new NoSuchElementException();
        else names.remove(localeCode);
    }

    /**
     * Changes localised name for universal region by locale code.
     * @param localeCode The locale code.
     * @param newName The new name for the universal region.
     * @throws NoSuchElementException If a localized name is not defined for a specified locale language code.
     */
    public void changeName(@NotNull final String localeCode, @NotNull final String newName) throws NoSuchElementException {
        if (!names.containsKey(localeCode))
            throw new NoSuchElementException();
        else names.put(localeCode, newName);
    }

    public void setChunks(@NotNull final HashSet<UniversalChunk> chunks) {
        this.chunks = chunks;
    }

    public void setDoubleKeys(@NotNull final Map<String, Double> doubleKeys) {
        DoubleKeys = doubleKeys;
    }

    public void setIntegerKeys(@NotNull final Map<String, Integer> integerKeys) {
        IntegerKeys = integerKeys;
    }

    public void setNames(@NotNull final Map<String, String> names) {
        this.names = names;
    }

    public void setStringKeys(@NotNull final Map<String, String> stringKeys) {
        StringKeys = stringKeys;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void saveData() throws IOException {
        File file = new File(UniversalRegions.instance().getDataFolder() +
                File.separator + "data" + File.separator + "universal-regions" +
                File.separator + world.getName(), this.ID + ".yml");
        FileConfiguration config = new YamlConfiguration();

        config.set("ID", ID);
        config.set("world", world.getName());
        config.set("locale-names", names);
        ArrayList<String> chunksID = new ArrayList<>();
        chunks.forEach(chunk -> chunksID.add(chunk.getID()));
        config.set("chunks", chunksID);

        config.set("keys-string", this.StringKeys);
        config.set("keys-integer", this.IntegerKeys);
        config.set("keys-double", this.DoubleKeys);

        config.save(file);
        file.createNewFile();
    }

    public void saveAsync() {

    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        Field[] fields = this.getClass().getFields();
        for (Field f : fields) {
            try {
                s.append(f.getName()).append(": ").append(f.get(f));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return s.toString();
    }
}
