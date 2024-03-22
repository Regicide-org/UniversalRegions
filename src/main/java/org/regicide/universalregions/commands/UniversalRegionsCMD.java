package org.regicide.universalregions.commands;


import dev.jorel.commandapi.annotations.*;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.regicide.universalregions.UniversalRegions;

/**
 * UniversalRegions command – main plugin command.
 */
@Command("universalregions")
@Alias({"uregions", "regions", "uregs"})
public final class UniversalRegionsCMD {

    @Default
    @Permission("universalregions.command.universalregions")
    public static void regicideui(@NotNull final CommandSender sender) {
        sender.sendMessage("--- UniversalRegions help ---");
        sender.sendMessage("/universalregions – Show this help.");
        sender.sendMessage("/universalregions reload – Reloads the plugin.");
    }

    @Subcommand("reload")
    @Permission("universalregions.command.reload")
    public static void reload(@NotNull final CommandSender sender){
        UniversalRegions.instance().reload();
        sender.sendMessage("Plugin was successfully reload!");
    }

}
