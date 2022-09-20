package com.tobiassteely.eliteenchants.command;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.API;
import com.tobiassteely.eliteenchants.api.Log;
import com.tobiassteely.eliteenchants.ticket.TicketPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class RedeemCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("redeem")) {
            if (sender instanceof Player) {
                Player player = (Player)sender;
                TicketPlayer ticketPlayer = EliteEnchants.getInstance().getTicketManager().getPlayer(player);

                double amount = 0;
                API api = new API();
                for(int i = 0; i < player.getInventory().getContents().length; i++) {
                    ItemStack itemStack = player.getInventory().getContents()[i];
                    if (itemStack != null && itemStack.getItemMeta().getDisplayName() != null && itemStack.getItemMeta().getLore() != null && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(api.cc("&4Ticket"))) {
                        if (itemStack.getItemMeta().getLore().get(0).equalsIgnoreCase(api.cc("&CHold it in your hand and"))) {
                            amount += itemStack.getAmount();
                            player.getInventory().setItem(i, null);
                        }
                    }
                }
                ticketPlayer.addTickets(amount);

                api.sendMessage(player, "You have deposited " + amount + " ticket(s) into your account!");

            } else {
                Log.sendMessage(2, "This is a player only command.");
            }
        }
        return true;
    }


}
