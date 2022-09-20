package com.tobiassteely.eliteenchants.command;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.API;
import com.tobiassteely.eliteenchants.api.Log;
import com.tobiassteely.eliteenchants.ticket.TicketPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;

public class TicketsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("tickets") || label.equalsIgnoreCase("ticket")) {
            if (sender instanceof Player) {
                if(args.length == 1 && sender.hasPermission("eliteenchants.balance.others")) {
                    Player player = Bukkit.getPlayer(args[0]);
                    OfflinePlayer offlinePlayer = null;
                    if(player == null) {
                        offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                        player = offlinePlayer.getPlayer();
                    }

                    if(player == null) {
                        new API().sendMessage(sender, "That is not a valid player.");
                        return true;
                    }

                    TicketPlayer ticketPlayer = EliteEnchants.getInstance().getTicketManager().getPlayer(player);

                    double tickets = ticketPlayer.getTickets();
                    if(tickets == 1) {
                        new API().sendMessage(((Player)sender), args[0] + " has " + (int)tickets + " ticket.");
                    } else {
                        new API().sendMessage(((Player)sender), args[0] + " has " + (int)tickets + " tickets.");
                    }

                    if(offlinePlayer == null) {
                        EliteEnchants.getInstance().getTicketManager().removePlayer(player);
                    }
                } else {
                    Player player = (Player) sender;

                    TicketPlayer ticketPlayer = EliteEnchants.getInstance().getTicketManager().getPlayer(player);

                    double tickets = ticketPlayer.getTickets();
                    if(tickets == 1) {
                        new API().sendMessage(player, "You have " + (int)tickets + " ticket.");
                    } else {
                        new API().sendMessage(player, "You have " + (int)tickets + " tickets.");
                    }

                }
            } else {
                if(args.length == 1) {
                    Player player = Bukkit.getPlayer(args[0]);
                    OfflinePlayer offlinePlayer = null;
                    if(player == null) {
                        offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                        player = offlinePlayer.getPlayer();
                    }

                    TicketPlayer ticketPlayer = EliteEnchants.getInstance().getTicketManager().getPlayer(player);

                    double tickets = ticketPlayer.getTickets();
                    if(tickets == 1) {
                        Log.sendMessage(0, args[0] + " has " + (int)tickets + " ticket.");
                    } else {
                        Log.sendMessage(0, args[0] + " has " + (int)tickets + " tickets.");
                    }

                    if(offlinePlayer == null) {
                        EliteEnchants.getInstance().getTicketManager().removePlayer(player);
                    }
                } else {
                    Log.sendMessage(2, "Console does not have any tickets. Try \"tickets <player>\"");
                }
            }
        }
        return true;
    }

}