package hoosk.visuwarpspigot.commands;

import hoosk.visuwarpspigot.VisuWarpSpigot;
import hoosk.visuwarpspigot.management.WarpManager;
import hoosk.visuwarpspigot.util.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Command which facilitates the addition of warps to the warp manager's global warps list.
 * @author Juskie (Casey)
 * @version 1.0.2
 * @since 1.0.0
 */
public class AddWarp implements CommandExecutor, Listener {
    private final WarpManager warpManager;
    private final VisuWarpSpigot plugin;

    /**
     * Sole constructor. Instantiates this command.
     * @param plugin The parent plugin
     * @param warpManager The warp manager
     */
    public AddWarp(VisuWarpSpigot plugin, WarpManager warpManager) {
        this.plugin = plugin;
        this.warpManager = warpManager;
    }

    /**
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return boolean true/false if command was successful
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if(!player.hasPermission("visuwarp.add")) {
            sender.sendMessage("Sorry, you don't have permission to use this command. Contact an administrator if you believe this to be an error.");
        }

        if (args.length < 2) {
            player.sendMessage("You need to specify a warp name and description.");
            return true;
        }

        String name = args[0];
        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        if(!warpManager.getWarps().isEmpty()) {
            if (warpManager.getWarps().containsKey(name)) {
                player.sendMessage("A warp with that name already exists.");
                return true;
            }
        }

        ItemStack representation = player.getInventory().getItemInMainHand();
        if (representation.getType() == Material.AIR) {
            player.sendMessage("You need to be holding an item to use as the warp's representation.");
            return true;
        }

        Location location = player.getLocation();

        Bukkit.getLogger().info(Arrays.toString(args));

        Map<String, Object> warpMap = new HashMap<>();

        warpMap.put("name", name);
        // warpMap.put("location", location);
        warpMap.put("world", location.getWorld().getName());
        warpMap.put("x", location.getX());
        warpMap.put("y", location.getY());
        warpMap.put("z", location.getZ());
        warpMap.put("yaw", location.getYaw());
        warpMap.put("pitch", location.getPitch());
        warpMap.put("representation", representation);
        warpMap.put("lore", Arrays.asList(description.split("\\n")));

        // Warp warp = new Warp(warpMap);
        Warp warp = Warp.deserialize(warpMap);

        warpManager.addWarp(warp);

        player.sendMessage("Warp " + name + " added successfully!");

        return true;
    }

    /**
     * Gets the parent plugin.
     * @return Parent plugin object.
     */
    public VisuWarpSpigot getPlugin() {
        return plugin;
    }
}
