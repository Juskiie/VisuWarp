package hoosk.visuwarpspigot.listeners;

import hoosk.visuwarpspigot.management.WarpManager;
import hoosk.visuwarpspigot.util.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Deprecated
public class WarpMenuListener implements Listener {
    private WarpManager warpManager;

    public WarpMenuListener(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        // Open the warp menu when a specific item is clicked. Replace "WARP_MENU_OPENER" with the name of your item.
        if (clickedItem.getType() == Material.GRASS) {
            Inventory warpMenu = Bukkit.createInventory(null, 9 * 3, "Warp menu");

            for (Warp warp : warpManager.getWarps().values()) {
                // We clone the warp's representation to avoid modifying the original item stack.
                ItemStack warpItem = warp.getRepresentation().clone();

                ItemMeta warpItemMeta = warpItem.getItemMeta();
                warpItemMeta.setDisplayName(warp.getName());
                warpItemMeta.setLore(warp.getLore());

                warpItem.setItemMeta(warpItemMeta);

                warpMenu.addItem(warpItem);
            }

            player.openInventory(warpMenu);
        }

        // Teleport the player to a warp's location when its representation is clicked in the warp menu.
        if (event.getView().getTitle().equals("Warp menu")) {
            event.setCancelled(true);

            String warpName = clickedItem.getItemMeta().getDisplayName();
            Warp warp = warpManager.getWarps().get(warpName);

            if (warp != null) {
                player.teleport(warp.getLocation());
            }
        }
    }
}