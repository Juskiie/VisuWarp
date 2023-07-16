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
 * @author Juskie (Casey)
 * @version 1.0.2
 * @since 1.0.0
 */
public class Warp implements ConfigurationSerializable {
    private String name;
    private Location location;
    private ItemStack representation;
    private List<String> lore = new ArrayList<>();

    /**
     * Constructor for creating warps.
     * @param name Name of the warp
     * @param location The coordinates, world, and viewing angle
     * @param representation The item to display when rendering this warp
     * @param lore The description of the warp
     */
    public Warp(String name, Location location, ItemStack representation, List<String> lore) {
        this.name = name;
        this.location = location;
        this.representation = representation;
        this.lore = lore;
    }

    /**
     * Gets the name of a warp
     * @return The name of the warp
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of a warp
     * @param name The name to set the warp to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the location of a warp
     * @return The warp location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location of a warp
     * @param location The new warp location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the item representing the warp
     * @return The ItemStack representing the warp
     */
    public ItemStack getRepresentation() {
        return representation;
    }

    /**
     * Sets the item that represents a warp
     * @param representation Item to set
     */
    public void setRepresentation(ItemStack representation) {
        this.representation = representation;
    }

    /**
     * Gets the warps description
     * @return The description of the warp
     */
    public List<String> getLore() {
        return lore;
    }

    /**
     * Sets the description of a warp
     * @param lore The new description for the warp
     */
    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    /**
     * Creates a Map representation of this class.
     * <p>
     * This class must provide a method to restore this class, as defined in
     * the {@link ConfigurationSerializable} interface javadocs.
     *
     * @return Map containing the current state of this class
     */
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

    /**
     * Converts a serialized map object back into a Warp
     * @param map The serialized warp
     * @return The deserialized warp
     */
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

    /**
     * Override of the toString() method to pretty print warp data.
     * @return Formatted string containing warp information
     */
    @Override
    public String toString() {
        return ("Warp name: " + this.getName() + "\n"
                + "Location: " + this.getLocation() + "\n"
                + "Representation: " + this.getRepresentation() + "\n"
                + "lore: " + this.getLore());
    }
}
