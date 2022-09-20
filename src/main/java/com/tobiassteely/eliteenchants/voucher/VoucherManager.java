package com.tobiassteely.eliteenchants.voucher;

import com.tobiassteely.eliteenchants.api.API;
import com.tobiassteely.eliteenchants.enchant.Enchant;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class VoucherManager {

    public static ItemStack getEnchantVoucher(Enchant enchant, int amount) {
        ItemStack item = new API().getItemStack("BOOK", (short)0, "&4" + enchant.getName() + " Voucher", Arrays.asList("&7+" + amount + " Levels", "", "&7Right click with the voucher", "&7select your desired tool right.", "&7click again to redeem."), 1, null);

        NBTItem nbti = new NBTItem(item);

        nbti.setInteger("ee-voucher-level", amount);
        nbti.setString("ee-voucher-enchant", enchant.getName());

        return nbti.getItem();
    }

    public static void giveVoucher(Enchant enchant, int amount, Player player) {
        ItemStack item = getEnchantVoucher(enchant, amount);
        if(player.getInventory().addItem(item).size() > 0) {
            player.getWorld().dropItem(player.getLocation(), item);
        }
    }

}
