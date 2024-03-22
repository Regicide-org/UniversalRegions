package org.regicide.universalregions.events.chunks;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.regicide.universalregions.UniversalRegions;
import org.regicide.universalregions.object.UniversalChunk;

/**
 * Called when {@link UniversalRegions} plugin tries to load {@link UniversalChunk}.
 */
public final class UniversalChunkLoadEvent extends Event implements Cancellable {

    private final UniversalChunk uChunk;
    private boolean isCancelled;

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    /**
     * @param uChunk The {@link UniversalChunk} that {@link UniversalChunk} tries to load.
     */
    public UniversalChunkLoadEvent(@NotNull final UniversalChunk uChunk) {
        UniversalRegions.instance().getLogger().info("Загружен новый universal-region!");
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
