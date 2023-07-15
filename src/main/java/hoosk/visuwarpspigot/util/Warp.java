package hoosk.visuwarpspigot.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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

    public Warp(String name, Location location, ItemStack representation, List<String> lore) {
        this.name = name;
        this.location = location;
        this.representation = representation;
        this.lore = lore;
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
        map.put("name", this.name);
        map.put("world", this.location.getWorld().getName());
        map.put("x", this.location.getX());
        map.put("y", this.location.getY());
        map.put("z", this.location.getZ());
        map.put("yaw", this.location.getYaw());
        map.put("pitch", this.location.getPitch());
        map.put("representation", this.representation);
        map.put("lore", this.lore);
        return map;
    }

    public static Warp deserialize(Map<String, Object> map) {
        World world = Bukkit.getWorld((String) map.get("world"));
        if (world == null) {
            throw new IllegalArgumentException("unknown world");
        }
        Location location = new Location(world,
                (double) map.get("x"),
                (double) map.get("y"),
                (double) map.get("z"),
                ((Number) map.get("yaw")).floatValue(),
                ((Number) map.get("pitch")).floatValue());
        ItemStack representation = (ItemStack) map.get("representation");
        List<String> lore = (List<String>) map.get("lore");
        return new Warp((String) map.get("name"), location, representation, lore);
    }

    @Override
    public String toString() {
        return ("Warp name: " + this.getName() + "\n"
                + "Location: " + this.getLocation() + "\n"
                + "Representation: " + this.getRepresentation() + "\n"
                + "lore: " + this.getLore());
    }
}
