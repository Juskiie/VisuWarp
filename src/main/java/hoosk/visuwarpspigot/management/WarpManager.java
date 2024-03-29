package hoosk.visuwarpspigot.management;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hoosk.visuwarpspigot.VisuWarpSpigot;
import hoosk.visuwarpspigot.util.Warp;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Contract;


/**
 * Manages adding, removing, and loading warps for VisuWarp.
 * @author Juskie (Casey)
 * @version 1.0.2
 * @since 1.0.0
 */
public class WarpManager implements Listener {
    private Map<String, Warp> warps = new HashMap<>();
    private final VisuWarpSpigot plugin;
    private static volatile WarpManager instance; // Any reference should be to the most recent warp manager stored in memory.

    /**
     * Initialise the warp manager for the plugin
     * @param plugin The plugin
     */
    public WarpManager(VisuWarpSpigot plugin) {
        this.plugin=plugin;
        loadWarps();
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    /**
     * Gets all warps stored in the warp manager
     * @return A map object containing all warps
     */
    public synchronized Map<String, Warp> getWarps() {
        return warps;
    }

    /**
     * Directly modify the warps stored in the warp manager. This does NOT save to the config!
     * @param warps The warps to set
     */
    public synchronized void setWarps(Map<String, Warp> warps) {
        this.warps = warps;
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
    public void removeWarp(String name) {
        warps.remove(name);
        saveWarps();
    }

    /**
     * Saves all warps currently in memory to config file property "warps"
     */
    public synchronized void saveWarps() {
        FileConfiguration config = plugin.getConfig();
        List<Map<String, Object>> serializedWarps = warps.values().stream().map(Warp::serialize).collect(Collectors.toList());
        config.set("warps", serializedWarps);
        plugin.saveConfig();
    }

    /**
     * Loads all warps saved in config file to memory.
     */
    public synchronized void loadWarps() {
        Bukkit.getLogger().info("[VisuWarp:WarpManager.java:loadWarps()] loading warps..");
        FileConfiguration config = plugin.getConfig();
        if (!config.contains("warps")) {
            Bukkit.getLogger().warning("[VisuWarp:WarpManager.java:loadWarps()] Plugin config doesn't contain any warps! (yet?)");
            return;
        }
        Bukkit.getLogger().info("[VisuWarp] Warps entry found! Loading now...");
        List<Map<?, ?>> serializedWarps = config.getMapList("warps");
        for (Map<?, ?> serializedWarp : serializedWarps) {
            Warp warp = Warp.deserialize((Map<String, Object>) serializedWarp); // Awful unchecked cast here. I'll fix later.
            warps.put(warp.getName(), warp);
        }

        Bukkit.getLogger().info("[VisuWarp:WarpManager.java:loadWarps()] Warps: " + warps.toString());
        Bukkit.getLogger().info("[VisuWarp:WarpManager.java:loadWarps()] Loaded warps (size): " + warps.size());
    }

    /**
     * Checks if class has been instantiated, and if it has, returns the singleton instance.
     * <p>
     * There should only be one Warp Manager
     * @return The singleton instance of this class
     */
    @SuppressWarnings("unused") // We do not care
    public static WarpManager getInstance() {
        if(instance==null){
            synchronized (WarpManager.class){
                if(instance==null)
                    instance=new WarpManager(VisuWarpSpigot.getInstance());
            }
        }
        return instance;
    }

    /**
     * Gets the parent plugin class
     * @return The parent plugin
     */
    @SuppressWarnings("unused")
    public VisuWarpSpigot getPlugin() {
        return this.plugin;
    }

}
