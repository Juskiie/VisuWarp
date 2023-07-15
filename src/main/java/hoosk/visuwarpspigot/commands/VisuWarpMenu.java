package hoosk.visuwarpspigot.commands;

import hoosk.visuwarpspigot.VisuWarpSpigot;
import hoosk.visuwarpspigot.management.WarpManager;
import hoosk.visuwarpspigot.util.Warp;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class VisuWarpMenu implements CommandExecutor, Listener, TabCompleter {
    private final WarpManager warpManager;
    private final VisuWarpSpigot plugin;

    public VisuWarpMenu(VisuWarpSpigot plugin, WarpManager warpManager) {
        this.plugin = plugin;
        this.warpManager = warpManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return Command success
     */
    @Override
    @SuppressWarnings("NullableProblems")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("open")) {
            openMenu(player);
            return true;
        }
        else if (args[0].equalsIgnoreCase("list")) {
            warpManager.getWarps().forEach((name, warp) -> player.sendMessage(name));
            return true;
        }
        else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                player.sendMessage("You need to specify a warp name to remove.");
                return true;
            }
            String name = args[1];
            if(warpManager.getWarps().containsKey(name)) {
                warpManager.removeWarp(name);
                player.sendMessage("Warp " + name + " removed successfully!");
            } else {
                player.sendMessage("No warp found with name: " + name);
            }
            return true;
        }
        else if (args[0].equalsIgnoreCase("version")) {
            player.sendMessage("Plugin Version: " + plugin.getDescription().getVersion());
            return true;
        }
        else {
            player.sendMessage("Invalid argument. Usage: " + command.getUsage());
            return true;
        }
    }

    /**
     *
     * @param player Player who triggered the event
     */
    public void openMenu(Player player) {
        Inventory warpMenu = Bukkit.createInventory(player, 9 * 3, "Warp Menu");

        for (Warp warp : warpManager.getWarps().values()) {
            ItemStack warpItem = warp.getRepresentation().clone();

            ItemMeta warpItemMeta = warpItem.getItemMeta();
            if (warpItemMeta != null) {
                warpItemMeta.setDisplayName(warp.getName());
            }
            if (warpItemMeta != null) {
                warpItemMeta.setLore(warp.getLore());
            }

            warpItem.setItemMeta(warpItemMeta);

            warpMenu.addItem(warpItem);
        }

        Bukkit.getLogger().info(warpMenu.toString());

        player.openInventory(warpMenu);
    }

    /**
     * Handles when the player clicks an item in the warp menu.
     * @param event The item being clicked in the inventory
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals("Warp Menu")){
            Bukkit.getLogger().info("[VisuWarp:VisuWarpMenu.java:onInventoryClick] Wrong inventory");
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        // Bukkit.getLogger().info("[VisuWarp] Item slot clicked: " + slot + " (" + player + ")");

        List<Warp> warps = new ArrayList<>(warpManager.getWarps().values());

        if(slot < 0 || slot >= warps.size()) {
            // Invalid slot number, might happen when the player clicks outside the inventory.
            event.setCancelled(true);
            return;
        }

        Warp clickedWarp = warps.get(slot);

        if(clickedWarp != null && clickedWarp.getLocation() != null) {
            player.teleport(clickedWarp.getLocation());
            event.setCancelled(true);
        }
        // Cancel the event so that the item isn't actually taken by the player.
        event.setCancelled(true);
    }

    /**
     *
     * @param sender Source of the command.  For players tab-completing a
     *     command inside of a command block, this will be the player, not
     *     the command block.
     * @param command Command which was executed
     * @param alias Alias of the command which was used
     * @param args The arguments passed to the command, including final
     *     partial argument to be completed
     * @return Tab complete options
     */
    @Override
    @SuppressWarnings("NullableProblems")
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        if (args.length == 1) { // Autocomplete for the first argument
            String partialCommand = args[0].toLowerCase();

            List<String> completions = new ArrayList<>();
            List<String> commands = Arrays.asList("open", "list", "remove", "version");

            // If partialCommand is empty, suggest all commands. Otherwise, only suggest commands that start with partialCommand.
            if (partialCommand.isEmpty()) {
                completions = commands;
            } else {
                for (String cmd : commands) {
                    if (cmd.startsWith(partialCommand)) {
                        completions.add(cmd);
                    }
                }
            }

            return completions;
        }

        if (args.length == 2) { // Autocomplete for the second argument
            if (args[0].equalsIgnoreCase("remove")) { // If first argument is "remove"
                String partialCommand = args[1].toLowerCase();

                List<String> completions = new ArrayList<>();
                Set<String> warps = warpManager.getWarps().keySet();

                if (partialCommand.isEmpty()) {
                    completions = new ArrayList<>(warps);
                } else {
                    for (String warp : warps) {
                        if (warp.toLowerCase().startsWith(partialCommand)) {
                            completions.add(warp);
                        }
                    }
                }

                return completions;
            }
        }

        return null; // Return null for Bukkit to fall back to the default behaviour
    }
}
