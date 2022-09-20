package com.tobiassteely.eliteenchants.enchant;

//import com.tobiassteely.breakoutcore.PrisonCore;
import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.config.ConfigObject;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Enchant implements Listener {

    private String name;
    private String displayName;
    private List<String> description;
    private Material item;
    private int maxLevel;
    private double cost;
    private double increment;
    private String upgradeType;
    private String currencyType;
    private List<String> tools;

    private boolean vanilla = false;


    public Enchant(String name) {
        this.name = name;
        ConfigObject configObject = EliteEnchants.getInstance().getConfigManager().getConfig("enchantments.yml");

        int updated = 0;
        updated += configObject.loadDefault("enchantments." + name + ".enabled", true);
        updated += configObject.loadDefault("enchantments." + name + ".name", "&c" + name);
        updated += configObject.loadDefault("enchantments." + name + ".lore", Arrays.asList("&7A cool enchantment."));
        updated += configObject.loadDefault("enchantments." + name + ".item", "BOOK");
        updated += configObject.loadDefault("enchantments." + name + ".max-level", 10);
        updated += configObject.loadDefault("enchantments." + name + ".cost.base", 10);
        updated += configObject.loadDefault("enchantments." + name + ".cost.increment", 5);
        updated += configObject.loadDefault("enchantments." + name + ".cost.increase", "flat");
        updated += configObject.loadDefault("enchantments." + name + ".cost.currency", "tickets");
        updated += configObject.loadDefault("enchantments." + name + ".enabled-items", Arrays.asList("TOOLS"));

        if(updated > 0)
            configObject.save();

        this.displayName = configObject.getFileConfiguration().getString("enchantments." + name + ".name");
        this.description = configObject.getFileConfiguration().getStringList("enchantments." + name + ".lore");
        this.item = Material.getMaterial(configObject.getFileConfiguration().getString("enchantments." + name + ".item"));
        this.maxLevel = configObject.getFileConfiguration().getInt("enchantments." + name + ".max-level");
        this.cost = configObject.getFileConfiguration().getDouble("enchantments." + name + ".cost.base");
        this.increment = configObject.getFileConfiguration().getDouble("enchantments." + name + ".cost.increment");
        this.upgradeType = configObject.getFileConfiguration().getString("enchantments." + name + ".cost.increase");
        this.currencyType = configObject.getFileConfiguration().getString("enchantments." + name + ".cost.currency");
        this.tools = configObject.getFileConfiguration().getStringList("enchantments." + name + ".enabled-items");
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMaxLevel(Player player) {
        return maxLevel;
    }

    public int getCost(int level) {
        if (upgradeType.equalsIgnoreCase("none")) {
            return (int)Math.round(cost);
        } else if (upgradeType.equalsIgnoreCase("exponential")) {
            return (int)Math.round(cost + (Math.pow(level, increment)));
        } else if (upgradeType.equalsIgnoreCase("flat")) {
            return (int)Math.round(cost + (increment * level));
        } else if (upgradeType.equalsIgnoreCase("multiplier")) {
            return (int)Math.round(cost + (cost * level * increment));
        }
        return -1;
    }

    public double getIncrement() {
        return increment;
    }

    public List<String> getDescription() {
        return description;
    }

    public Material getItem() {
        return item;
    }

    public List<String> getTools() {
        return tools;
    }

    public boolean isVanilla() {
        return vanilla;
    }

    public void setVanilla(boolean vanilla) {
        this.vanilla = vanilla;
    }
}
