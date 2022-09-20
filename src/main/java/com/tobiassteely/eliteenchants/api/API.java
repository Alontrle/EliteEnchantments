package com.tobiassteely.eliteenchants.api;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.config.ConfigManager;
import com.tobiassteely.eliteenchants.enchant.EnchantAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class API {

    public String cc(String m) {
        return ChatColor.translateAlternateColorCodes('&', m);
    }

    public String getHeader() {
        return cc(EliteEnchants.getInstance().getConfigManager().getConfig("lang.yml").getFileConfiguration().getString("header"));
    }

    public String getFooter() {
        return cc(EliteEnchants.getInstance().getConfigManager().getConfig("lang.yml").getFileConfiguration().getString("footer"));
    }

    public String formatBalance(Double balance, boolean precise) {
        return "$" + formatNumber(balance, precise);
    }

    public String formatNumber(Number number, boolean precise) {
        if (precise) {
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###,###.##");
            decimalFormat.setGroupingUsed(true);
            decimalFormat.setGroupingSize(3);
            return decimalFormat.format(number);
        } else {
            char[] suffix = {' ', 'K', 'M', 'B', 'T', 'P', 'E'};
            long numValue = number.longValue();
            int value = (int) Math.floor(Math.log10(numValue));
            int base = value / 3;
            if (value >= 3 && base < suffix.length) {
                return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
            } else {
                return new DecimalFormat("#,##0").format(numValue);
            }
        }
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(cc("&4Custom Enchants &8» &c" + message));
    }

    public void sendMessage(Player player, String message, boolean prefix) {
        if(prefix)
            player.sendMessage(cc("&4Custom Enchants &8» &c" + message));
        else
            player.sendMessage(cc(message));
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(cc("&4Custom Enchants &8» &c" + message));
    }

    public void sendMessage(CommandSender sender, String message, boolean prefix) {
        if(prefix)
            sender.sendMessage(cc("&4Custom Enchants &8» &c" + message));
        else
            sender.sendMessage(cc(message));
    }

    public ItemStack getItemStack(String material, short itemData, String name, List<String> lore, int amount, HashMap<String, Integer> enchantments) {
        API api = new API();
        ItemStack is = new ItemStack(Material.getMaterial(material), amount, itemData);
        ItemMeta itemMeta = is.getItemMeta();
        if (name != null)
            itemMeta.setDisplayName(api.cc(name));

        if (lore != null) {
            List<String> updatedLore = new ArrayList<>();
            for(String line : lore)
                updatedLore.add(api.cc(line));
            itemMeta.setLore(updatedLore);
        }

        is.setItemMeta(itemMeta);
        if(enchantments != null) {
            for (String key : enchantments.keySet()) {
                EliteEnchants.getInstance().getEnchantAPI().enchantItem(is, key, enchantments.get(key));
            }
        }
        return is;
    }

    public String parseStringArrayList(int start, ArrayList<String> stringArrayList) {
        StringBuilder parsedString = new StringBuilder();
        for(int i = start; i < stringArrayList.size(); i++) {
            if(i == stringArrayList.size() - 1)
                parsedString.append(stringArrayList.get(i));
            else
                parsedString.append(stringArrayList.get(i)).append(", ");
        }
        return parsedString.toString();
    }

    public String parseStringArray(int start, String[] stringArrayList) {
        StringBuilder parsedString = new StringBuilder();
        for(int i = start; i < stringArrayList.length; i++) {
            if(i == stringArrayList.length - 1)
                parsedString.append(stringArrayList[i]);
            else
                parsedString.append(stringArrayList[i]).append(", ");
        }
        return parsedString.toString();
    }

    public String parseStringArrayListNoDelimiter(int start, ArrayList<String> stringArrayList) {
        StringBuilder parsedString = new StringBuilder();
        for(int i = start; i < stringArrayList.size(); i++) {
            if(i == stringArrayList.size() - 1)
                parsedString.append(stringArrayList.get(i));
            else
                parsedString.append(stringArrayList.get(i)).append(" ");
        }
        return parsedString.toString();
    }

    public String parseStringArrayNoDelimiter(int start, String[] stringArrayList) {
        StringBuilder parsedString = new StringBuilder();
        for(int i = start; i < stringArrayList.length; i++) {
            if(i == stringArrayList.length - 1)
                parsedString.append(stringArrayList[i]);
            else
                parsedString.append(stringArrayList[i]).append(" ");
        }
        return parsedString.toString();
    }

}
