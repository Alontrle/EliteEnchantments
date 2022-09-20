package com.tobiassteely.eliteenchants.command;

//import com.tobiassteely.breakoutcore.PrisonCore;
//import com.tobiassteely.breakoutcore.managers.ChatManager;
//import com.tobiassteely.breakoutcore.managers.Experience;
//import com.tobiassteely.breakoutcore.objects.PrisonPlayer;
import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.API;
import com.tobiassteely.eliteenchants.ticket.TicketPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConvertCommand implements CommandExecutor {

    public ConvertCommand() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("convertxp")) {
            if (sender instanceof Player) {
                Player player = (Player)sender;
                TicketPlayer tp = EliteEnchants.getInstance().getTicketManager().getPlayer(player);
                if (args.length > 0) {
                    try {
                        int tickets = Integer.parseInt(args[0]);

                        if(tickets < 1) {
                            throw new Exception("Tickets below 1");
                        }

                        if(tickets > 100000) {
                            throw new Exception("Going to cause issues");
                        }

                        int neededXP = tickets * 5;

                        if(getTotalExperience(player) >= neededXP) {
                                tp.setTickets(tp.getTickets() + tickets);
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp give " + player.getName() + " -" + neededXP);
                                new API().sendMessage(sender, "You have converted " + neededXP + " XP into " + tickets + " Tickets(s).", true);
                        } else {
                            new API().sendMessage(sender, "You do not have enough XP!", true);
                        }
                    } catch (Exception ex) {
                        new API().sendMessage(sender, "That is not a valid amount! Valid amounts are 1 to 100000.", true);
                    }
                } else {
                    new API().sendMessage(sender, "&CXP to Ticket Conversion", true);
                    new API().sendMessage(sender, " &fThis takes 5 XP per Ticket.", false);
                    new API().sendMessage(sender, " &f/convert <amount>", false);
                }
            } else {
                sender.sendMessage("This is a player only command.");
            }
            return true;
        }

        return false;
    }

    public int getTotalExperience(Player Who) {
        int level = Who.getLevel();
        float progress = Who.getExp();
        int totalExp = 0;
        for (int n = 1; n < level + 1; n++) totalExp = (n >= 16) ? ((n >= 31) ? totalExp + 112 + (n - 31) * 9 : totalExp + 37 + (n - 16) * 5) : totalExp + 7 + (n - 1) * 2;
        progress = (level >= 15 ) ? ((level >= 30) ? progress * (112 + (level - 30) * 9) : progress * (37 + (level - 15) * 5)) : progress * (7 + level * 2);
        totalExp = totalExp + Math.round(progress);
        return totalExp;
    }

}
