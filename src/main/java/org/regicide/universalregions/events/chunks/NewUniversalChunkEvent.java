package org.regicide.universalregions.events.chunks;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.regicide.universalregions.UniversalRegions;
import org.regicide.universalregions.object.UniversalChunk;
import org.regicide.universalregions.object.UniversalRegion;

/**
 * Called when {@link UniversalRegions} plugin tries to create new {@link UniversalChunk}.
 */
public final class NewUniversalChunkEvent extends Event implements Cancellable {

    private final UniversalChunk uChunk;
    private boolean isCancelled;

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    /**
     * @param uChunk The {@link UniversalRegion} that {@link Player} tries to create.
     */
    public NewUniversalChunkEvent(@NotNull final UniversalChunk uChunk) {
        UniversalRegions.instance().getLogger().info("Создан новый universal-region!");
        this.uChunk = uChunk;

        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public @NotNull UniversalChunk getUniversalChunk() {
        return uChunk;
    }
}
