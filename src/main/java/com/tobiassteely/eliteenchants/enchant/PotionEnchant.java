package com.tobiassteely.eliteenchants.enchant;

import com.tobiassteely.eliteenchants.EliteEnchants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class PotionEnchant extends Enchant {

    private ArrayList<String> healthBoosted;

    public PotionEnchant(String name, PotionEffectType potionEffectType) {
        super(name);
        this.healthBoosted = new ArrayList<>();

        schedule(EnchantmentType.HAND, potionEffectType);
        schedule(EnchantmentType.HELMET, potionEffectType);
        schedule(EnchantmentType.CHESTPLATE, potionEffectType);
        schedule(EnchantmentType.LEGGINGS, potionEffectType);
        schedule(EnchantmentType.BOOTS, potionEffectType);
    }

    private void schedule(EnchantmentType type, PotionEffectType potionEffectType) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(EliteEnchants.getInstance(), () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                int level = EliteEnchants.getInstance().getEnchantAPI().getLevel(player, this, type);
                if(level > 0) {
                    if(potionEffectType.equals(PotionEffectType.NIGHT_VISION)) {
                        player.addPotionEffect(new PotionEffect(potionEffectType, 300, level), true);
                    } else if(potionEffectType.equals(PotionEffectType.HEALTH_BOOST)) {
                        player.setMaxHealth(20 + (level * 4));
                        if(!healthBoosted.contains(getName()))
                            healthBoosted.add(getName());
                    } else {
                        player.addPotionEffect(new PotionEffect(potionEffectType, 60, level), true);
                    }
                } else {
                    if(healthBoosted.contains(getName())) {
                        if(player.getHealth() > 20) {
                            player.setHealth(20);
                        }
                        player.setMaxHealth(20);
                        healthBoosted.remove(getName());
                    }
                }
            }
        }, 0, 20);
    }

}
