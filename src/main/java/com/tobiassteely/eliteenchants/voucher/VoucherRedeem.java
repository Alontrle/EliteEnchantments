package com.tobiassteely.eliteenchants.voucher;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.API;
import com.tobiassteely.eliteenchants.command.DisenchantCommand;
import com.tobiassteely.eliteenchants.enchant.Enchant;
import com.tobiassteely.eliteenchants.enchant.EnchantmentType;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VoucherRedeem implements Listener {

    private Map<UUID, VoucherCache> voucherCache = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        ItemStack voucher = e.getPlayer().getInventory().getItemInHand();
        if(voucherCache.containsKey(e.getPlayer().getUniqueId())) {
            VoucherCache cache = voucherCache.get(e.getPlayer().getUniqueId());

            ItemStack item = e.getPlayer().getItemInHand();

            ArrayList<Enchant> enchantments = EliteEnchants.getInstance().getEnchantManager().getEnchantsByTool(item.getType().toString());
            if(!enchantments.contains(cache.getEnchant())) {
                msg(e.getPlayer(), "&cThat enchantment is not valid for that item!");
                voucherCache.remove(e.getPlayer().getUniqueId());
                VoucherManager.giveVoucher(cache.getEnchant(), cache.getLevels(), e.getPlayer());
                return;
            }

            // If they exceed the overall max for the enchantment
            int level = EliteEnchants.getInstance().getEnchantAPI().getLevel(e.getPlayer(), cache.getEnchant(), EnchantmentType.HAND);
            if(level + cache.getLevels() > cache.getEnchant().getMaxLevel(e.getPlayer())) {
                msg(e.getPlayer(), "&cThat would put the enchantment level above the maximum allowed!");
                voucherCache.remove(e.getPlayer().getUniqueId());
                VoucherManager.giveVoucher(cache.getEnchant(), cache.getLevels(), e.getPlayer());
                return;
            }

            // If they exceed the max system for vouchers
            int limit = DisenchantCommand.getLimit(cache.getEnchant());
            if(limit != -1 && level + cache.getLevels() > limit) {
                msg(e.getPlayer(), "&cThat would put the enchantment level above the maximum allowed!");
                voucherCache.remove(e.getPlayer().getUniqueId());
                VoucherManager.giveVoucher(cache.getEnchant(), cache.getLevels(), e.getPlayer());
                return;
            }

            msg(e.getPlayer(), "&aYou have successfully enchanted that item!");

            voucherCache.remove(e.getPlayer().getUniqueId());

            EliteEnchants.getInstance().getEnchantAPI().enchantItemIncrSafe(item, cache.getEnchant().getName(), cache.getLevels(), e.getPlayer());
            return;
        }
        if(voucher.getType().equals(Material.BOOK)) {

            NBTItem item = new NBTItem(voucher);
            if(item.hasKey("ee-voucher-enchant")) {
                String enchantmentName = item.getString("ee-voucher-enchant");
                int level = item.getInteger("ee-voucher-level");

                Enchant enchant = EliteEnchants.getInstance().getEnchantManager().getEnchantByName(enchantmentName);

                voucherCache.put(e.getPlayer().getUniqueId(), new VoucherCache(enchant, level, e.getPlayer().getUniqueId()));

                if(e.getPlayer().getItemInHand().getAmount() > 1)
                    e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
                else
                    e.getPlayer().setItemInHand(null);

                msg(e.getPlayer(), "&aYou have redeemed an enchantment voucher, please right-click holding the tool you would like to enchant.");
            }
        }
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent e) {
        VoucherCache cache = voucherCache.get(e.getPlayer().getUniqueId());

        if(cache == null) return;

        VoucherManager.giveVoucher(cache.getEnchant(), cache.getLevels(), e.getPlayer());
    }

    private void msg(Player player, String msg) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

}
