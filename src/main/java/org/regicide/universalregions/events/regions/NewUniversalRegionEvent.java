package org.regicide.universalregions.events.regions;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.regicide.universalregions.UniversalRegions;
import org.regicide.universalregions.object.UniversalRegion;

/**
 * Called when a {@link Player} tries to create new {@link UniversalRegion}.
 */
public final class NewUniversalRegionEvent extends Event implements Cancellable {

    private final Player player;
    private final UniversalRegion uRegion;
    private boolean isCancelled;

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    /**
     * @param player The {@link Player} who tries to create a new {@link UniversalRegion}.
     * @param uRegion The {@link UniversalRegion} that {@link Player} tries to create.
     */
    public NewUniversalRegionEvent(@NotNull final Player player, @NotNull final UniversalRegion uRegion) {
        UniversalRegions.instance().getLogger().info("Создан новый universal-region!");
        this.player = player;
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

    public UniversalRegion getUniversalRegion() {
        return uRegion;
    }

    public @NotNull Player getPlayer() {
        return player;
    }
}
