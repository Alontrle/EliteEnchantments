package com.tobiassteely.eliteenchants.enchant;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.API;
import com.tobiassteely.eliteenchants.api.Log;
import com.tobiassteely.eliteenchants.ticket.TicketManager;
import com.tobiassteely.eliteenchants.ticket.TicketPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.xml.crypto.Data;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EnchantGUI implements InventoryHolder, Listener {

    // Create a new inventory, with "this" owner for comparison with other inventories, a size of nine, called example
    private Inventory inv;
    private API api;

    private DecimalFormat df = new DecimalFormat("###,###,###");

    public EnchantGUI() {
        this.api = new API();
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    // You can call this whenever you want to put the items in
    public void initializeItems(Player player) throws Exception {
        ArrayList<Enchant> enchantments = EliteEnchants.getInstance().getEnchantManager().getEnchantsByTool(player.getPlayer().getInventory().getItemInHand().getType().toString());

        if(enchantments.size() == 0)
            throw new Exception("No enchantments");

        int slots = 36 + ((int)((enchantments.size() - 1) / 7.0) * 9);

        if(slots > 54) slots = 54;

        this.inv = Bukkit.createInventory(this, slots, api.cc("&8Enchantment Menu"));

        for(int i = 0; i < 9; i++) {
            if(i == 3 || i == 4 || i == 5) continue;
            inv.setItem(i, api.getItemStack("STAINED_GLASS_PANE", (short)15, "", Arrays.asList(""), 1, null));
        }
        inv.setItem(9, api.getItemStack("STAINED_GLASS_PANE", (short)15, "", Arrays.asList(""), 1, null));
        inv.setItem(17, api.getItemStack("STAINED_GLASS_PANE", (short)15, "", Arrays.asList(""), 1, null));

        for(int i = slots - 9; i < slots; i++) {
            inv.setItem(i, api.getItemStack("STAINED_GLASS_PANE", (short)15, "", Arrays.asList(""), 1, null));
        }

        inv.setItem(slots - 10, api.getItemStack("STAINED_GLASS_PANE", (short)15, "", Arrays.asList(""), 1, null));
        inv.setItem(slots - 18, api.getItemStack("STAINED_GLASS_PANE", (short)15, "", Arrays.asList(""), 1, null));

        double tickets = EliteEnchants.getInstance().getTicketManager().getPlayer(player).getTickets();
        DecimalFormat df = new DecimalFormat("###,###");
        inv.setItem(4, api.getItemStack("SIGN", (short)0, "&4Enchantment Center", Arrays.asList("&CYou have " + df.format(tickets) + " ticket(s)."), 1, null));

        int slot = 10;
        for(int i = 0; i < enchantments.size(); i++) {
            Enchant enchant = enchantments.get(i);
            ItemStack itemStack = createGuiItem(enchant, player);
            inv.setItem(slot, itemStack);
            slot++;

            if(i != 0 && (i + 1) % 7 == 0) {
                slot += 2;
            }
        }
    }

    // You can open the inventory with this
    public void openInventory(Player player) throws Exception {
        initializeItems(player);
        player.getPlayer().openInventory(inv);
    }

    // Nice little method to create a gui item with a custom name, and description
    private ItemStack createGuiItem(Enchant enchant, Player player) {

        ArrayList<String> lore = new ArrayList<>(Arrays.asList(api.cc("&7Click to upgrade " + enchant.getName()), api.cc("&7for " + df.format((int) getCost(player, enchant)) + " tickets."), ""));

        for(String line : enchant.getDescription())
            lore.add(api.cc(line));

        lore.add("");

        DecimalFormat df = new DecimalFormat("###,###");
        lore.add(api.cc("&7Current Level: " + df.format(EliteEnchants.getInstance().getEnchantAPI().getLevel(player, enchant, EnchantmentType.HAND))));
        lore.add(api.cc("&7Max Level: " + df.format(enchant.getMaxLevel(player))));

        ItemStack item;
        if(enchant.getItem() != null) {
            item = new ItemStack(enchant.getItem());
        } else {
            item = new ItemStack(Material.BARRIER);
        }
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(api.cc(enchant.getDisplayName()));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    // Check for clicks on items

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getClickedInventory().getTitle() != null) {
            if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(api.cc("&8Enchantment Menu"))) {
                e.setCancelled(true);

                Player player = (Player)e.getWhoClicked();
                ItemStack clickedItem = e.getCurrentItem();

                // verify current item is not null
                if (clickedItem == null || clickedItem.getType() == Material.AIR)
                    return;

                Enchant enchant = EliteEnchants.getInstance().getEnchantManager().getEnchantByDisplayName(clickedItem.getItemMeta().getDisplayName());
                if (enchant != null) {
                    verifyPurchase(player, enchant);
                }
            } else if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(api.cc("&8Confirm your purchase!"))) {
                e.setCancelled(true);

                Player player = (Player)e.getWhoClicked();
                ItemStack clickedItem = e.getCurrentItem();

                // verify current item is not null
                if (clickedItem == null || clickedItem.getType() == Material.AIR)
                    return;

                if(clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(api.cc("&a1 Level"))) {
                    Enchant enchant = EliteEnchants.getInstance().getEnchantManager().getEnchantByName(clickedItem.getItemMeta().getLore().get(0).split(api.cc("&7Click to confirm your "))[1].split(" upgrade!")[0]);
                    purchaseEnchant(player, enchant, 1);
                    verifyPurchase(player, enchant);
                } else if(clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(api.cc("&a10 Levels"))) {
                    Enchant enchant = EliteEnchants.getInstance().getEnchantManager().getEnchantByName(clickedItem.getItemMeta().getLore().get(0).split(api.cc("&7Click to confirm your "))[1].split(" upgrade!")[0]);
                    purchaseEnchant(player, enchant, 10);
                    verifyPurchase(player, enchant);
                }  else if(clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(api.cc("&a50 Levels"))) {
                    Enchant enchant = EliteEnchants.getInstance().getEnchantManager().getEnchantByName(clickedItem.getItemMeta().getLore().get(0).split(api.cc("&7Click to confirm your "))[1].split(" upgrade!")[0]);
                    purchaseEnchant(player, enchant, 50);
                    verifyPurchase(player, enchant);
                } else if(clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(api.cc("&a100 Levels"))) {
                    Enchant enchant = EliteEnchants.getInstance().getEnchantManager().getEnchantByName(clickedItem.getItemMeta().getLore().get(0).split(api.cc("&7Click to confirm your "))[1].split(" upgrade!")[0]);
                    purchaseEnchant(player, enchant, 100);
                    verifyPurchase(player, enchant);
                } else if(clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(api.cc("&aMax Levels"))) {
                    Enchant enchant = EliteEnchants.getInstance().getEnchantManager().getEnchantByName(clickedItem.getItemMeta().getLore().get(0).split(api.cc("&7Click to confirm your "))[1].split(" upgrade!")[0]);

                    int max = enchant.getMaxLevel(player);
                    int current = EliteEnchants.getInstance().getEnchantAPI().getLevel(player, enchant, EnchantmentType.HAND);
                    int lvls = max - current;

                    purchaseEnchant(player, enchant, lvls);
                    verifyPurchase(player, enchant);
                } else if(clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(api.cc("&cGo Back"))) {
                    try {
                        openInventory(player);
                    } catch (Exception ignored) {}
                }
            } else if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(api.cc("&8Error!"))) {
                e.setCancelled(true);
            }
        }
    }

    private void verifyPurchase(Player player, Enchant enchant) {
        this.inv = Bukkit.createInventory(this, 27, api.cc("&8Confirm your purchase!"));

        TicketPlayer ticketPlayer = EliteEnchants.getInstance().getTicketManager().getPlayer(player);

        int max = enchant.getMaxLevel(player);
        int current = EliteEnchants.getInstance().getEnchantAPI().getLevel(player, enchant, EnchantmentType.HAND);

        if(current + 1 > max)
            inv.setItem(11, api.getItemStack("BARRIER", (short)1, "&c1 Level", new ArrayList<>(Arrays.asList("&7This would exceed the max", "&7level allowed for this item.")), 1, null));
        else
            inv.setItem(11, api.getItemStack("STAINED_GLASS_PANE", (short)13, "&a1 Level", new ArrayList<>(Arrays.asList("&7Click to confirm your " + enchant.getName() + " upgrade!", "&7Costs " + df.format(getCost(player, enchant, 1)) + " tickets.")), 1, null));

        if(current + 10 > max)
            inv.setItem(12, api.getItemStack("BARRIER", (short)1, "&c10 Levels", new ArrayList<>(Arrays.asList("&7This would exceed the max", "&7level allowed for this item.")), 1, null));
        else
            inv.setItem(12, api.getItemStack("STAINED_GLASS_PANE", (short)5, "&a10 Levels", new ArrayList<>(Arrays.asList("&7Click to confirm your " + enchant.getName() + " upgrade!", "&7Costs " + df.format(getCost(player, enchant, 10)) + " tickets.")), 1, null));

        if(current + 50 > max)
            inv.setItem(13, api.getItemStack("BARRIER", (short)1, "&c50 Levels", new ArrayList<>(Arrays.asList("&7This would exceed the max", "&7level allowed for this item.")), 1, null));
        else
            inv.setItem(13, api.getItemStack("STAINED_GLASS_PANE", (short)4, "&a50 Levels", new ArrayList<>(Arrays.asList("&7Click to confirm your " + enchant.getName() + " upgrade!", "&7Costs " + df.format(getCost(player, enchant, 50)) + " tickets.")), 1, null));

        if(current + 100 > max)
            inv.setItem(14, api.getItemStack("BARRIER", (short)1, "&c100 Levels", new ArrayList<>(Arrays.asList("&7This would exceed the max", "&7level allowed for this item.")), 1, null));
        else
            inv.setItem(14, api.getItemStack("STAINED_GLASS_PANE", (short)1, "&a100 Levels", new ArrayList<>(Arrays.asList("&7Click to confirm your " + enchant.getName() + " upgrade!", "&7Costs " + df.format(getCost(player, enchant, 100)) + " tickets.")), 1, null));

        int lvls = max - current;

        if(lvls > 0) {
            inv.setItem(15, api.getItemStack("STAINED_GLASS_PANE", (short) 14, "&aMax Levels", new ArrayList<>(Arrays.asList("&7Click to confirm your " + enchant.getName() + " upgrade!", "&7Costs " + df.format(getCost(player, enchant, lvls)) + " tickets.")), 1, null));
        } else {
            inv.setItem(15, api.getItemStack("BARRIER", (short)0, "&cAlready maxed", Collections.singletonList("&7There are no more upgrades available at the moment."), 1, null));
        }

        inv.setItem(22, api.getItemStack("BARRIER", (short)0, "&cGo Back", Collections.singletonList("&7Click to go back to the menu."), 1, null));
        // String material, short itemData, String name, List<String> lore, int amount, HashMap<String, Integer> enchantments
//        inv.setItem(11, api.getItemStack("WOOL", (short)5, "&aAccept", new ArrayList<>(Arrays.asList("&7Click to confirm your " + enchant.getName() + " upgrade!", "&7Costs " + getCost(player, enchant)+ " ticket(s).")), 1, null));
//        inv.setItem(13, api.getItemStack(enchant.getItem().toString(), (short)0, enchant.getDisplayName(), new ArrayList<>(Arrays.asList("&7Do you want to upgrade this", "&7for " + ticketPlayer.getTickets() + " ticket(s).")), 1, null));
//        inv.setItem(15, api.getItemStack("WOOL", (short)14, "&cDeny", new ArrayList<>(Collections.singletonList("&7Click to deny the upgrade!")), 1, null));

        player.getPlayer().openInventory(inv);
    }

    private void purchaseError(Player player, String reason) {
        api.sendMessage(player, reason);
        this.inv = Bukkit.createInventory(this, 27, api.cc("&8Error!"));

        for(int i = 0; i < 27; i++) {
            inv.setItem(i, api.getItemStack("WOOL", (short)14, "&4ERROR!", new ArrayList<>(Collections.singletonList("&c" + reason)), 1, null));
        }
        inv.setItem(13, api.getItemStack("SIGN", (short)0, "&4ERROR!", new ArrayList<>(Collections.singletonList("&c" + reason)), 1, null));
        player.getPlayer().openInventory(inv);

        EliteEnchants.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(EliteEnchants.getInstance(), () -> {
            if(player.getPlayer().getOpenInventory().getTitle().equalsIgnoreCase(api.cc("&8Error!"))) {
                try {
                    openInventory(player);
                } catch (Exception ignored) {}
            }
        }, 60L);
    }

    private boolean purchaseEnchant(Player player, Enchant e, int levels) {
        double cost = getCost(player, e, levels);
        TicketPlayer ticketPlayer = EliteEnchants.getInstance().getTicketManager().getPlayer(player);

        if (ticketPlayer.getTickets() >= cost) {
            if (EliteEnchants.getInstance().getEnchantAPI().getLevel(player, e, EnchantmentType.HAND) < e.getMaxLevel(player)) {
                ticketPlayer.removeTickets(cost);
                ItemStack item = player.getPlayer().getInventory().getItemInHand();
                EliteEnchants.getInstance().getEnchantAPI().enchantItemIncr(item, e.getName(), levels);
                player.getPlayer().getInventory().setItemInHand(item);
                api.sendMessage(player, "You have enchanted your tool with " + e.getName() + ".");
                return true;
            } else
                purchaseError(player, "You are already the max level of this enchantment.");
        } else {
            purchaseError(player, "You do not have enough tickets.");
        }
        return false;
    }

    public double getCost(Player player, Enchant e) {
        return e.getCost(EliteEnchants.getInstance().getEnchantAPI().getLevel(player, e, EnchantmentType.HAND));
    }

    public double getCost(Player player, Enchant e, int levels) {
        double cost = 0;
        for(int i = 0; i < levels; i++) {
            cost += e.getCost(EliteEnchants.getInstance().getEnchantAPI().getLevel(player, e, EnchantmentType.HAND) + i);
        }
        return cost;
    }
}
