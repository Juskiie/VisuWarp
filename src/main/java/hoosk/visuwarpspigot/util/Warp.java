package hoosk.visuwarpspigot.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.sql.Array;
import java.util.*;

/**
 * Holds information about warp for VisuWarp: name, location, item representation, lore.
 */
public class Warp implements ConfigurationSerializable {
    private String name;
    private Location location;
    private ItemStack representation;
    private List<String> lore = new ArrayList<>();

    /**
     *
     * @param map The config file map data for the warp.
     */
    public Warp(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.location = (Location) map.get("location");
        this.representation = (ItemStack) map.get("representation");
        Object loreObject = map.get("lore");
        if (loreObject instanceof List) {
            this.lore = (List<String>) loreObject;
        } else if (loreObject instanceof String[]) {
            this.lore = Arrays.asList((String[]) loreObject);
        } else {
            this.lore = new ArrayList<>();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ItemStack getRepresentation() {
        return representation;
    }

    public void setRepresentation(ItemStack representation) {
        this.representation = representation;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("location", location);
        map.put("representation", representation);
        map.put("lore", lore);
        return map;
    }

    @Override
    public String toString() {
        return ("Warp name: " + this.getName() + "\n"
                + "Location: " + this.getLocation() + "\n"
                + "Representation: " + this.getRepresentation() + "\n"
                + "lore: " + this.getLore());
    }
}
