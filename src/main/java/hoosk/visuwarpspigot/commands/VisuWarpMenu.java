package hoosk.visuwarpspigot.commands;

import hoosk.visuwarpspigot.VisuWarpSpigot;
import hoosk.visuwarpspigot.management.WarpManager;
import hoosk.visuwarpspigot.util.Warp;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class VisuWarpMenu implements CommandExecutor, Listener {
    private final WarpManager warpManager;
    private final VisuWarpSpigot plugin;

    public VisuWarpMenu(VisuWarpSpigot plugin) {
        this.plugin = plugin;
        this.warpManager = new WarpManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        Inventory warpMenu = Bukkit.createInventory(player, 9 * 3, "Warp Menu");

        for (Warp warp : warpManager.getWarps().values()) {
            ItemStack warpItem = warp.getRepresentation().clone();

            ItemMeta warpItemMeta = warpItem.getItemMeta();
            warpItemMeta.setDisplayName(warp.getName());
            warpItemMeta.setLore(warp.getLore());

            warpItem.setItemMeta(warpItemMeta);

            warpMenu.addItem(warpItem);
        }

        player.openInventory(warpMenu);

        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals("Warp Menu")){
            return;
        }

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        Bukkit.getLogger().info("[VisuWarp] Item slot clicked: " + slot + " (" + player + ")");

        List<Warp> warps = new ArrayList<>(warpManager.getWarps().values());

        if(slot < 0 || slot >= warps.size()) {
            // Invalid slot number, might happen when the player clicks outside of the inventory.
            return;
        }

        Warp clickedWarp = warps.get(slot);

        if(clickedWarp != null && clickedWarp.getLocation() != null) {
            player.teleport(clickedWarp.getLocation());
        }
        // Cancel the event so that the item isn't actually taken by the player.
        event.setCancelled(true);
    }
}
