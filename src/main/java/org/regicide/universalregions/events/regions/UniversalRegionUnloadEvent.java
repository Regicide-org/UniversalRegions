package org.regicide.universalregions.events.regions;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.regicide.universalregions.UniversalRegions;
import org.regicide.universalregions.object.UniversalRegion;

/**
 * Called when a {@link UniversalRegions} plugin tries to unload {@link UniversalRegion}.
 */
public final class UniversalRegionUnloadEvent extends Event implements Cancellable {

    private final UniversalRegion uRegion;
    private boolean isCancelled;

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    /**
     * @param uRegion The {@link UniversalRegion} that {@link UniversalRegions} plugin tries to unload.
     */
    public UniversalRegionUnloadEvent(@NotNull final UniversalRegion uRegion) {
        UniversalRegions.instance().getLogger().info("Создан новый universal-region!");
        this.uRegion = uRegion;

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

    public @NotNull UniversalRegion getUniversalRegion() {
        return uRegion;
    }

}
