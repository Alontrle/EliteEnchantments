package com.tobiassteely.eliteenchants.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractHandler implements Listener {

    @EventHandler(priority= EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if(p.getInventory().getItemInHand().getType().equals(Material.DIAMOND_PICKAXE)) {
            if(p.isSneaking() && (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))) {
                if (!(event.getPlayer().getInventory().getItemInHand() == null || event.getPlayer().getInventory().getItemInHand().getType() == Material.AIR)) {
                    p.performCommand("enchant");
                }
            }
        }
    }

}
