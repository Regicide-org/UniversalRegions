package org.regicide.universalregions.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.annotations.*;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.regicide.universalregions.UniversalRegions;
import org.regicide.universalregions.events.chunks.NewUniversalChunkEvent;
import org.regicide.universalregions.events.regions.NewUniversalRegionEvent;
import org.regicide.universalregions.exceptions.AlreadyExistingElementException;
import org.regicide.universalregions.exceptions.ImpossibleElementException;
import org.regicide.universalregions.exceptions.OverlayElementException;
import org.regicide.universalregions.object.UniversalChunk;
import org.regicide.universalregions.storage.UniversalChunkStorage;
import org.regicide.universalregions.storage.UniversalRegionStorage;
import org.regicide.universalregions.object.UniversalRegion;

import java.io.IOException;

/**
 * UniversalRegion command – main region command.
 */
public final class UniversalRegionCMD {
    /**
     * Register /universalregions command and all subcommands
     */
    public UniversalRegionCMD() {
        new CommandAPICommand("universalregions")
                .withPermission("universalregion.command.universalregion")
                .withoutPermission("universalregion.command.universalregion")
                .executes((sender, args) -> {
                    if (sender instanceof Player)
                        ((Player) sender).sendMessage("S");
                    else sender.sendMessage("You do not have sufficient rights to use this command.");
                })
                .executes((sender, args) -> {
                    sender.sendMessage("--- UniversalRegion help ---");
                    sender.sendMessage("/universalregion – Show this help.");
                    sender.sendMessage("/universalregion new <ID> <default_name> – Creates new region.");
                    sender.sendMessage("/universalregion remove <ID> – Removes region.");
                    sender.sendMessage("/universalregion add name <ID> <locale_code> <new_name> – Adds new localised name.");
                    sender.sendMessage("/universalregion add chunk <ID> – Adds chunk where you stay to the region with specified ID.");
                    sender.sendMessage("/universalregion remove name <ID> <locale_code> – Removes name linked with locale code with specified ID.");
                    sender.sendMessage("/universalregion remove chunk <ID> – Adds chunk where you stay to the region with specified ID.");
                })
                .register();
    }
}
/*
@Command("universalregion")
@Alias({"uregion", "region", "ureg"})
public final class UniversalRegionCMD {

    @Default
    @Permission("universalregion.command.universalregion")
    public static void universalRegion(@NotNull final CommandSender sender) {
        sender.sendMessage("--- UniversalRegion help ---");
        sender.sendMessage("/universalregion – Show this help.");
        sender.sendMessage("/universalregion new <ID> <default_name> – Creates new region.");
        sender.sendMessage("/universalregion remove <ID> – Removes region.");
        sender.sendMessage("/universalregion add name <ID> <locale_code> <new_name> – Adds new localised name.");
        sender.sendMessage("/universalregion add chunk <ID> – Adds chunk where you stay to the region with specified ID.");
        sender.sendMessage("/universalregion remove name <ID> <locale_code> – Removes name linked with locale code with specified ID.");
        sender.sendMessage("/universalregion remove chunk <ID> – Adds chunk where you stay to the region with specified ID.");
    }

    @Subcommand({"create", "new"})
    @Permission("universalregions.command.create")
    public static void create(@NotNull final CommandSender sender, @NotNull @AStringArgument String ID, @NotNull @AStringArgument String defaultName) {
        if (!(sender instanceof Player)) {
            UniversalRegions.instance().getLogger().info("Only players can create regions!");
            return;
        }
        Player p = (Player) sender;
        try {
            UniversalChunk uChunk;
            if (UniversalChunkStorage.isUniversalChunk(p.getChunk()))
                uChunk = UniversalChunkStorage.getUniversalChunk(p.getChunk());
            else {
                uChunk = new UniversalChunk(p.getChunk());
                NewUniversalChunkEvent event = new NewUniversalChunkEvent(uChunk);
                if (event.isCancelled())
                    return;
                UniversalChunkStorage.add(uChunk);
            }

            UniversalRegion uRegion = new UniversalRegion(ID, defaultName, uChunk);
            NewUniversalRegionEvent event = new NewUniversalRegionEvent(p, uRegion);
            if (event.isCancelled())
                return;

            UniversalRegionStorage.addRegion(uRegion);
            uChunk.setUniversalRegion(uRegion);

            uRegion.saveData();
            uChunk.saveData();

        } catch (AlreadyExistingElementException e) {
            p.sendMessage("You cannot, it's already exists, kys!");
            return;
        } catch (OverlayElementException e) {
            p.sendMessage("You cannot, there is another region, kys!");
            return;
        } catch (ImpossibleElementException e) {
            p.sendMessage("You cannot, this world unable, kys!");
            return;
        } catch (IOException e) {
            p.sendMessage("You cannot, file just won't to save, kys!");
            return;
        }

        UniversalRegions.instance().getLogger().info(p.getName() + " ["+ p.getUniqueId() +"] create new universal region with ID \""+ ID +"\".");
        p.sendMessage("Universal region with ID \"" + ID + "\" was successfully created!");
    }

}
*/