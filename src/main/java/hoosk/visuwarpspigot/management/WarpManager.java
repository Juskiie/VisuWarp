package hoosk.visuwarpspigot.management;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hoosk.visuwarpspigot.VisuWarpSpigot;
import hoosk.visuwarpspigot.util.Warp;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;


/**
 * Manages adding, removing, and loading warps for VisuWarp.
 */
public class WarpManager implements Listener {
    private Map<String, Warp> warps;
    private final VisuWarpSpigot plugin;

    /**
     * Initialise the warp manager for the plugin
     * @param plugin The plugin
     */
    public WarpManager(VisuWarpSpigot plugin) {
        this.plugin=plugin;
        this.warps = new HashMap<>();
        loadWarps();
        System.out.println(this);
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    public Map<String, Warp> getWarps() {
        return warps;
    }

    /**
     * Use to add a warp to the global warps list
     * @param warp The warp to add to warpmanager
     */
    public void addWarp(Warp warp) {
        warps.put(warp.getName(), warp);
        saveWarps();
    }

    /**
     * Use to remove a warp from the global warps list
     * @param name The name of the warp to remove
     */
    @Deprecated
    public void removeWarp(String name) {
        warps.remove(name);
        saveWarps();
    }

    /**
     * Saves all warps currently in memory to config file property "warps"
     */
    public void saveWarps() {
        FileConfiguration config = plugin.getConfig();
        config.set("warps", new ArrayList<>(warps.values()));
        plugin.saveConfig();
    }

    /**
     * Loads all warps saved in config file to memory.
     */
    public void loadWarps() {
        Bukkit.getLogger().info("[VisuWarp:WarpManager.java:loadWarps()] loading warps..");
        FileConfiguration config = plugin.getConfig();
        if (!config.contains("warps")) {
            Bukkit.getLogger().warning("[VisuWarp:WarpManager.java:loadWarps()] Plugin config doesn't contain any warps! (yet?)");
            return;
        }

        List<Map<?, ?>> serializedWarps = config.getMapList("warps");
        for (Map<?, ?> serializedWarp : serializedWarps) {
            Warp warp = new Warp((Map<String, Object>) serializedWarp); // Awful unchecked cast here. I'll fix later. TODO
            warps.put(warp.getName(), warp);
        }

        Bukkit.getLogger().info("[VisuWarp:WarpManager.java:loadWarps()] Warps: " + warps.toString());
        Bukkit.getLogger().info("[VisuWarp:WarpManager.java:loadWarps()] Loaded warps (size): " + warps.size());
    }

    public VisuWarpSpigot getPlugin() {
        return this.plugin;
    }

}
