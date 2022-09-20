package com.tobiassteely.eliteenchants.command;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.API;
import com.tobiassteely.eliteenchants.api.Log;
import com.tobiassteely.eliteenchants.ticket.TicketPlayer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class WithdrawCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("withdraw")) {
            if (sender instanceof Player) {
                if(args.length == 1) {
                    API api = new API();
                    try {
                        TicketPlayer ticketPlayer = EliteEnchants.getInstance().getTicketManager().getPlayer((Player)sender);
                        int amount = Integer.parseInt(args[0]);

                        if(amount < 1) {
                            api.sendMessage(sender, "&cValue must be positive.");
                            return true;
                        }

                        if (ticketPlayer.getTickets() >= amount) {

                            ItemStack itemStack = api.getItemStack("DOUBLE_PLANT", (short)0, "&4Ticket", Arrays.asList("&CHold it in your hand and", "&CRight-Click to redeem your tickets."), amount, null);
                            Player player = (Player)sender;

                            int slots = 0;
                            for(ItemStack inventorySlot : player.getInventory().getContents()) {
                                if(inventorySlot == null)
                                    slots++;
                            }

                            if(amount <= slots * 64) {
                                player.getInventory().addItem(itemStack);
                                api.sendMessage(sender, "You have withdrawn " + amount + " ticket(s), you now have "
                                        + ticketPlayer.removeTickets(amount) + " ticket(s).");
                            } else {
                                api.sendMessage(sender, "You do not have enough inventory space!");
                            }
                        } else {
                            api.sendMessage(sender, "You do not have enough tickets.");
                        }
                        return true;
                    } catch (Exception ignored) {}
                }
                new API().sendMessage(sender, "/withdraw <amount>");
            } else {
                Log.sendMessage(2, "This is a player only command.");
            }
        }
        return true;
    }


}
