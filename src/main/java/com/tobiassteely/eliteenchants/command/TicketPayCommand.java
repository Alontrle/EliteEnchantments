package com.tobiassteely.eliteenchants.command;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.API;
import com.tobiassteely.eliteenchants.api.Log;
import com.tobiassteely.eliteenchants.ticket.TicketPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketPayCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("ticketpay")) {
            if (sender.equals(Bukkit.getServer().getConsoleSender())) {
                Log.sendMessage(2, "This command is for players only.");
                return true;
            }

            Player player = (Player) sender;

            API api = new API();
            if(args.length == 2) {
                Player receiver = Bukkit.getPlayer(args[0]);
                if(receiver != null) {
                    double tickets = Double.parseDouble(args[1]);
                    if(tickets > 0) {
                        TicketPlayer ticketSender = EliteEnchants.getInstance().getTicketManager().getPlayer(player);
                        TicketPlayer ticketReceiver = EliteEnchants.getInstance().getTicketManager().getPlayer(receiver);

                        if (ticketSender.getTickets() - tickets >= 0) {
                            ticketSender.removeTickets(tickets);
                            ticketReceiver.addTickets(tickets);
                            api.sendMessage(player, "You have sent " + tickets + " ticket(s) to " + receiver.getName() + ".");
                            api.sendMessage(receiver, "You have received " + tickets + " ticket(s) from " + player.getName() + ".");
                        }
                    } else {
                        api.sendMessage(player, "The amount must be positive!");
                    }
                    return true;
                } else {
                    api.sendMessage(sender, "That player is not online!");
                    return true;
                }
            }
            api.sendMessage(sender, "/ticketpay <player> <amount>");
        }
        return true;
    }
}
