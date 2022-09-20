package com.tobiassteely.eliteenchants.enchant;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.API;
import com.tobiassteely.eliteenchants.api.Log;
import com.tobiassteely.eliteenchants.api.config.ConfigObject;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantAPI {

    private ConfigObject config;
    private API api;
    private String prefix;

    public EnchantAPI() {
        this.config = EliteEnchants.getInstance().getConfigManager().getConfig("enchantments.yml");
        this.api = new API();
        this.prefix = config.getFileConfiguration().getString("settings.enchantment-prefix");
    }

    public int getLevel(Player p, Enchant e, EnchantmentType type) {
        try {
            ItemStack item = p.getInventory().getItemInHand();

            if(type.equals(EnchantmentType.HELMET)) {
                item = p.getInventory().getHelmet();
            } else if(type.equals(EnchantmentType.CHESTPLATE)) {
                item = p.getInventory().getChestplate();
            } else if(type.equals(EnchantmentType.LEGGINGS)) {
                item = p.getInventory().getLeggings();
            } else if(type.equals(EnchantmentType.BOOTS)) {
                item = p.getInventory().getBoots();
            }


            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = itemMeta.getLore();
            if (lore.toString().contains(e.getName()))
                for (String str : lore)
                    if (str.contains(e.getName())) {
                        String line = str.substring(str.indexOf(e.getName()) + e.getName().length()).replaceAll("[^0-9]", "");
                        if(!line.isEmpty()) {
                            int level = Integer.parseInt(line);
                            int max = e.getMaxLevel(p);

                            if (level > max) {
                                return max;
                            }

                            return level;
                        }
                    }
        } catch (NullPointerException ex) {
        }
        return 0;
    }

    public int getLevel(ItemStack item, String e) {

        if(!item.hasItemMeta()) return 0;

        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();

        try {
            if (lore.toString().contains(e))
                for (String str : lore)
                    if (str.contains(e))
                        return Integer.parseInt(str.substring(str.indexOf(e) + e.length()).replaceAll("[^0-9]", ""));
        } catch (NullPointerException ex) {
        }
        return 0;
    }

    public Map<Enchant, Integer> getEnchants(Player player, EnchantmentType type) {
        Map<Enchant, Integer> enchants = new HashMap<>();
        for(Enchant enchant : EliteEnchants.getInstance().getEnchantManager().getEnchants()) {
            int level = getLevel(player, enchant, type);
            if(level > 0) enchants.put(enchant, level);
        }
        return enchants;
    }

    public boolean enchantItemIncr(ItemStack item, String e, int incr) {
        return enchantItem(item, e, getLevel(item, e) + incr);
    }

    public boolean enchantItem(ItemStack item, String e, int level) {
        if(EliteEnchants.getInstance().getEnchantManager().getEnchantByName(e) != null) {
            ItemMeta itemMeta = item.getItemMeta();

            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            List<String> lore = itemMeta.getLore();
            if (lore == null)
                lore = new ArrayList<>();

            if (lore.toString().contains(e)) {
                for (int i = 0; i < lore.size(); i++) {
                    if (lore.get(i).contains(e)) {
                        if(level == 0) {
                            lore.remove(i);
                            break;
                        } else {
                            lore.set(i, api.cc(prefix + e + " " + level));
                            break;
                        }
                    }
                }
            } else {
                if(level > 0)
                    lore.add(api.cc(prefix + e + " " + level));
            }

            itemMeta.setLore(lore);

            Enchantment enchantment = Enchantment.getByName(e);
            e = e.replace(" ", "").replace("_", "");
            if (enchantment != null) {
                itemMeta.addEnchant(enchantment, level, true);
            } else if(e.equalsIgnoreCase("Fortune")) {
                itemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, level, true);
            } else if(e.equalsIgnoreCase("Looting")) {
                itemMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, level, true);
            } else if(e.equalsIgnoreCase("Power")) {
                itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, level, true);
            } else if(e.equalsIgnoreCase("Flame")) {
                itemMeta.addEnchant(Enchantment.ARROW_FIRE, level, true);
            } else if(e.equalsIgnoreCase("Infinity")) {
                itemMeta.addEnchant(Enchantment.ARROW_INFINITE, level, true);
            } else if(e.equalsIgnoreCase("Punch")) {
                itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, level, true);
            } else if(e.equalsIgnoreCase("Sharpness")) {
                itemMeta.addEnchant(Enchantment.DAMAGE_ALL, level, true);
            } else if(e.equalsIgnoreCase("Smite")) {
                itemMeta.addEnchant(Enchantment.DAMAGE_ARTHROPODS, level, true);
            } else if(e.equalsIgnoreCase("BaneOfAthropods")) {
                itemMeta.addEnchant(Enchantment.DAMAGE_UNDEAD, level, true);
            } else if(e.equalsIgnoreCase("DepthStrider")) {
                itemMeta.addEnchant(Enchantment.DEPTH_STRIDER, level, true);
            } else if(e.equalsIgnoreCase("Efficiency")) {
                itemMeta.addEnchant(Enchantment.DIG_SPEED, level, true);
            } else if(e.equalsIgnoreCase("Unbreaking")) {
                itemMeta.addEnchant(Enchantment.DURABILITY, level, true);
            } else if(e.equalsIgnoreCase("FireAspect")) {
                itemMeta.addEnchant(Enchantment.FIRE_ASPECT, level, true);
            } else if(e.equalsIgnoreCase("Knockback")) {
                itemMeta.addEnchant(Enchantment.KNOCKBACK, level, true);
            } else if(e.equalsIgnoreCase("Luck")) {
                itemMeta.addEnchant(Enchantment.LUCK, level, true);
            } else if(e.equalsIgnoreCase("Lure")) {
                itemMeta.addEnchant(Enchantment.LURE, level, true);
            } else if(e.equalsIgnoreCase("Respiration")) {
                itemMeta.addEnchant(Enchantment.OXYGEN, level, true);
            } else if(e.equalsIgnoreCase("Protection")) {
                itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, level, true);
            } else if(e.equalsIgnoreCase("BlastProtection")) {
                itemMeta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, level, true);
            } else if(e.equalsIgnoreCase("FallProtection")) {
                itemMeta.addEnchant(Enchantment.PROTECTION_FALL, level, true);
            } else if(e.equalsIgnoreCase("FireProtection")) {
                itemMeta.addEnchant(Enchantment.PROTECTION_FIRE, level, true);
            } else if(e.equalsIgnoreCase("ProjectileProtection")) {
                itemMeta.addEnchant(Enchantment.PROTECTION_PROJECTILE, level, true);
            } else if(e.equalsIgnoreCase("SilkTouch")) {
                itemMeta.addEnchant(Enchantment.SILK_TOUCH, level, true);
            } else if(e.equalsIgnoreCase("Thorns")) {
                itemMeta.addEnchant(Enchantment.THORNS, level, true);
            } else if(e.equalsIgnoreCase("AquaAffinity")) {
                itemMeta.addEnchant(Enchantment.WATER_WORKER, level, true);
            }

            item.setItemMeta(itemMeta);
            return true;
        }
        return false;
    }

    public boolean enchantItemIncrSafe(ItemStack item, String e, int incr, Player player) {
        return enchantItemSafe(item, e, getLevel(item, e) + incr, player);
    }

    public boolean enchantItemSafe(ItemStack item, String e, int level, Player player) {
        Enchant enchant = EliteEnchants.getInstance().getEnchantManager().getEnchantByName(e);
        if(enchant != null) {

            if(level > enchant.getMaxLevel(player))
                return false;

            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            List<String> lore = itemMeta.getLore();

            if (lore == null)
                lore = new ArrayList<>();

            if (lore.toString().contains(e)) {
                for (int i = 0; i < lore.size(); i++) {
                    if (lore.get(i).contains(e)) {
                        lore.set(i, api.cc(prefix + e + " " + level));
                    }
                }
            } else {
                lore.add(api.cc(prefix + e + " " + level));
            }

            itemMeta.setLore(lore);

            Enchantment enchantment = Enchantment.getByName(e);
            e = e.replace(" ", "").replace("_", "");
            if (enchantment != null) {
                itemMeta.addEnchant(enchantment, level, true);
            }
            if(e.equalsIgnoreCase("Fortune")) {
                itemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, level, true);
            } else if(e.equalsIgnoreCase("Looting")) {
                itemMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, level, true);
            } else if(e.equalsIgnoreCase("Power")) {
                itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, level, true);
            } else if(e.equalsIgnoreCase("Flame")) {
                itemMeta.addEnchant(Enchantment.ARROW_FIRE, level, true);
            } else if(e.equalsIgnoreCase("Infinity")) {
                itemMeta.addEnchant(Enchantment.ARROW_INFINITE, level, true);
            } else if(e.equalsIgnoreCase("Punch")) {
                itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, level, true);
            } else if(e.equalsIgnoreCase("Sharpness")) {
                itemMeta.addEnchant(Enchantment.DAMAGE_ALL, level, true);
            } else if(e.equalsIgnoreCase("Smite")) {
                itemMeta.addEnchant(Enchantment.DAMAGE_ARTHROPODS, level, true);
            } else if(e.equalsIgnoreCase("BaneOfAthropods")) {
                itemMeta.addEnchant(Enchantment.DAMAGE_UNDEAD, level, true);
            } else if(e.equalsIgnoreCase("DepthStrider")) {
                itemMeta.addEnchant(Enchantment.DEPTH_STRIDER, level, true);
            } else if(e.equalsIgnoreCase("Efficiency")) {
                itemMeta.addEnchant(Enchantment.DIG_SPEED, level, true);
            } else if(e.equalsIgnoreCase("Unbreaking")) {
                itemMeta.addEnchant(Enchantment.DURABILITY, level, true);
            } else if(e.equalsIgnoreCase("FireAspect")) {
                itemMeta.addEnchant(Enchantment.FIRE_ASPECT, level, true);
            } else if(e.equalsIgnoreCase("Knockback")) {
                itemMeta.addEnchant(Enchantment.KNOCKBACK, level, true);
            } else if(e.equalsIgnoreCase("Luck")) {
                itemMeta.addEnchant(Enchantment.LUCK, level, true);
            } else if(e.equalsIgnoreCase("Lure")) {
                itemMeta.addEnchant(Enchantment.LURE, level, true);
            } else if(e.equalsIgnoreCase("Respiration")) {
                itemMeta.addEnchant(Enchantment.OXYGEN, level, true);
            } else if(e.equalsIgnoreCase("Protection")) {
                itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, level, true);
            } else if(e.equalsIgnoreCase("BlastProtection")) {
                itemMeta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, level, true);
            } else if(e.equalsIgnoreCase("FallProtection")) {
                itemMeta.addEnchant(Enchantment.PROTECTION_FALL, level, true);
            } else if(e.equalsIgnoreCase("FireProtection")) {
                itemMeta.addEnchant(Enchantment.PROTECTION_FIRE, level, true);
            } else if(e.equalsIgnoreCase("ProjectileProtection")) {
                itemMeta.addEnchant(Enchantment.PROTECTION_PROJECTILE, level, true);
            } else if(e.equalsIgnoreCase("SilkTouch")) {
                itemMeta.addEnchant(Enchantment.SILK_TOUCH, level, true);
            } else if(e.equalsIgnoreCase("Thorns")) {
                itemMeta.addEnchant(Enchantment.THORNS, level, true);
            } else if(e.equalsIgnoreCase("AquaAffinity")) {
                itemMeta.addEnchant(Enchantment.WATER_WORKER, level, true);
            }

            item.setItemMeta(itemMeta);
            return true;
        }
        return false;
    }

}
