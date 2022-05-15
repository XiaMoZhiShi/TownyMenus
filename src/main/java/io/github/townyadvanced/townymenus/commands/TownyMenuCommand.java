package io.github.townyadvanced.townymenus.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import io.github.townyadvanced.townymenus.TownyMenus;
import io.github.townyadvanced.townymenus.gui.MenuInventory;
import io.github.townyadvanced.townymenus.gui.MenuItem;
import io.github.townyadvanced.townymenus.gui.action.ClickAction;
import io.github.townyadvanced.townymenus.menu.ResidentMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class TownyMenuCommand implements CommandExecutor, TabCompleter {
    private final TownyMenus plugin;

    public TownyMenuCommand(TownyMenus plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player))
            return true;

        Resident resident = TownyAPI.getInstance().getResident(player);
        boolean hasTown = resident != null && resident.hasTown();
        boolean hasNation = resident != null && resident.hasNation();

        MenuInventory.builder()
                .rows(3)
                .title(Component.text("Towny Menu"))
                .addItem(MenuItem.builder(Material.EMERALD)
                        .name(Component.text("Town Settings", NamedTextColor.GREEN))
                        .lore(hasTown ? Component.text("Click to view town commands!", NamedTextColor.GRAY) :
                                Component.text("✖ You are not a member of a town.", NamedTextColor.RED))
                        .slot(10)
                        .action(hasTown ? ClickAction.close() : ClickAction.EMPTY)
                        .build())
                .addItem(MenuItem.builder(Material.DIAMOND)
                        .name(Component.text("Nation Settings", NamedTextColor.AQUA))
                        .lore(hasNation ? Component.text("Click to view nation commands!", NamedTextColor.GRAY) :
                                Component.text("✖ You are not a member of a nation.", NamedTextColor.RED))
                        .slot(12)
                        .action(hasNation ? ClickAction.close() : ClickAction.EMPTY)
                        .build())
                .addItem(MenuItem.builder(Material.GRASS_BLOCK)
                        .name(Component.text("Plot Settings", NamedTextColor.DARK_GREEN))
                        .slot(14)
                        .action(ClickAction.close())
                        .build())
                .addItem(MenuItem.builder(Material.PLAYER_HEAD)
                        .skullOwner(player.getUniqueId())
                        .name(Component.text("Resident Settings", NamedTextColor.YELLOW))
                        .slot(16)
                        .action(ClickAction.openInventory(ResidentMenu.createResidentMenu(player)))
                        .build())
                .build()
                .open(player);

        return true;
    }
}
