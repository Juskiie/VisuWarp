package hoosk.visuwarpspigot;

import hoosk.visuwarpspigot.commands.AddWarp;
import hoosk.visuwarpspigot.commands.VisuWarpMenu;
import hoosk.visuwarpspigot.management.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Properties;

public final class VisuWarpSpigot extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();
    final Properties properties = new Properties();
    boolean firstTimeLaunch = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("[VisuWarp] Starting up!");
        Bukkit.getPluginManager().registerEvents(this, this);
        config.options().copyDefaults(true);
        saveConfig();

        // Setup
        WarpManager warpManager = new WarpManager(this);

        // Load commands
        try {
            this.getCommand("addwarp").setExecutor(new AddWarp(this));
            this.getCommand("vwmenu").setExecutor(new VisuWarpMenu(this));
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("[VisuWarp] Shutting down..");
    }
}
