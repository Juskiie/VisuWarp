package hoosk.visuwarpspigot;

import hoosk.visuwarpspigot.commands.AddWarp;
import hoosk.visuwarpspigot.commands.VisuWarpMenu;
import hoosk.visuwarpspigot.management.WarpManager;
import hoosk.visuwarpspigot.util.Warp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * The main plugin class, initialises commands and event handlers.
 * @author Juskie (Casey)
 * @version 1.0.2
 * @since 1.0.0
 */
public final class VisuWarpSpigot extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();

    /**
     * Default plugin startup behaviour.
     * Starts logging information and loading commands/handlers.
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("[VisuWarp] Starting up!");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + " __     ___         __        __               ");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + " \\ \\   / (_)___ _   \\ \\      / /_ _ _ __ _ __  ");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "  \\ \\ / /| / __| | | \\ \\ /\\ / / _` | '__| '_ \\ ");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "   \\ V / | \\__ \\ |_| |\\ V  V / (_| | |  | |_) |");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "    \\_/  |_|___/\\__,_| \\_/\\_/ \\__,_|_|  | .__/ ");
        ConfigurationSerialization.registerClass(Warp.class);
        Bukkit.getPluginManager().registerEvents(this, this);
        config.options().copyDefaults(true);
        saveConfig();

        // Setup
        WarpManager warpManager = new WarpManager(this);

        // Load commands
        try {
            Objects.requireNonNull(this.getCommand("addwarp")).setExecutor(new AddWarp(this, warpManager));
            Objects.requireNonNull(this.getCommand("vwmenu")).setExecutor(new VisuWarpMenu(this, warpManager));
            Objects.requireNonNull(this.getCommand("vwmenu")).setTabCompleter(new VisuWarpMenu(this, warpManager));
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    /**
     * Executed when plugin is stopped/disabled.
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("[VisuWarp] Shutting down..");
    }
}
