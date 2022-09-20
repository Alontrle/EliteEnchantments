package com.tobiassteely.eliteenchants.ticket;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.API;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TicketRedeem implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        ItemStack tickets = e.getPlayer().getInventory().getItemInHand();
        if (tickets.getType().equals(Material.DOUBLE_PLANT)) {
            API api = new API();
            if (tickets.getItemMeta().getDisplayName().equalsIgnoreCase(api.cc("&4Ticket")) && tickets.getItemMeta().getLore() != null) {
                if(tickets.getItemMeta().getLore().get(0).equalsIgnoreCase(api.cc("&CHold it in your hand and"))) {
                    TicketPlayer ticketPlayer = EliteEnchants.getInstance().getTicketManager().getPlayer(e.getPlayer());
                    ticketPlayer.addTickets(tickets.getAmount());

                    api.sendMessage(e.getPlayer(), "You have deposited " + tickets.getAmount() + " ticket(s) into your account!");

                    e.getPlayer().getInventory().setItemInHand(null);
                }
            }
        }
    }
}
