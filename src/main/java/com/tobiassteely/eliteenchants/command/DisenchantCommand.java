package com.tobiassteely.eliteenchants.command;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.enchant.Enchant;
import com.tobiassteely.eliteenchants.enchant.EnchantmentType;
import com.tobiassteely.eliteenchants.voucher.VoucherManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class DisenchantCommand implements CommandExecutor {

    private static Map<Enchant, Integer> maxLimit = new HashMap<>();

    public static int getLimit(Enchant enchant) {
        return maxLimit.getOrDefault(enchant, -1);
    }

    public static void setLimit(Enchant enchant, int limit) {
        maxLimit.put(enchant, limit);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }

        Player p = (Player) sender;

        if(args.length == 2) {
            Enchant enchant = EliteEnchants.getInstance().getEnchantManager().getEnchantByName(args[0]);
            if(enchant == null) {
                msg(p, "&cInvalid enchantment!");
                return true;
            }

            int level;
            try {
                level = Integer.parseInt(args[1]);
            } catch (NullPointerException ex) {
                msg(p, "&cThat is not a valid number!");
                return true;
            }

            if(level < 1) {
                msg(p, "&cThe value must be positive!");
                return true;
            }

            int playerLevel = EliteEnchants.getInstance().getEnchantAPI().getLevel(p, enchant, EnchantmentType.HAND);
            if(playerLevel < level) {
                msg(p, "&cYou do not have a high enough level of that enchant!");
                return true;
            }

            int max = getLimit(enchant);
            if((max != -1) && max < level) {
                msg(p, "That would exceed the max limit for that enchantment!");
                return true;
            }

            EliteEnchants.getInstance().getEnchantAPI().enchantItem(p.getItemInHand(), enchant.getName(), playerLevel - level);
            msg(p, "&aYou have removed that enchantment from that item!");
            if(enchant.isVanilla()) {
                VoucherManager.giveVoucher(enchant, level, p);
                msg(p, "&aYou have received a voucher for this vanilla enchantment!");
            } else {
                int tickets = 0;
                for(int i = 0; i < level; i++) {
                    tickets += enchant.getCost(playerLevel - level);
                }
                DecimalFormat df = new DecimalFormat("###,###");
                msg(p, "&aYou have received " + df.format(tickets) + " tickets for this custom enchantment!");
                EliteEnchants.getInstance().getTicketManager().getPlayer(p).addTickets(tickets);
            }
        } else {
            msg(p, "&b/disenchant (enchantment) (level)");
        }
        return true;
    }

    private void msg(Player player, String msg) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

}
