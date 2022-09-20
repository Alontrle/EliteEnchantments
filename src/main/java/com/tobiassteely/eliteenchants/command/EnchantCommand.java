package com.tobiassteely.eliteenchants.command;

import com.tobiassteely.eliteenchants.api.API;
import com.tobiassteely.eliteenchants.api.Log;
import com.tobiassteely.eliteenchants.enchant.EnchantGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnchantCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("enchant") || label.equalsIgnoreCase("upgrade") || label.equalsIgnoreCase("enchantment")) {
            Player p;

            try {
                p = (Player)sender;
            } catch (ClassCastException ex) {
                p = null;
            }

            if (p == null) {
                Log.sendMessage(2, "This command is for players only.");
                return true;
            }

            EnchantGUI gui = new EnchantGUI();
            try {
                gui.openInventory(p);
            } catch (Exception ex) {
                new API().sendMessage(sender, "There are no valid enchantments for that item!");
                ex.printStackTrace();
            }
            return true;
        }
        return false;
    }

}
