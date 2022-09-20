package com.tobiassteely.eliteenchants;

import com.tobiassteely.eliteenchants.api.ClipPlaceHolder;
import com.tobiassteely.eliteenchants.api.config.ConfigManager;
import com.tobiassteely.eliteenchants.api.config.ConfigObject;
import com.tobiassteely.eliteenchants.command.*;
import com.tobiassteely.eliteenchants.enchant.EnchantAPI;
import com.tobiassteely.eliteenchants.enchant.EnchantGUI;
import com.tobiassteely.eliteenchants.enchant.EnchantManager;
import com.tobiassteely.eliteenchants.event.PlayerInteractHandler;
import com.tobiassteely.eliteenchants.ticket.TicketManager;
import com.tobiassteely.eliteenchants.ticket.TicketRedeem;
import com.tobiassteely.eliteenchants.voucher.VoucherManager;
import com.tobiassteely.eliteenchants.voucher.VoucherRedeem;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EliteEnchants extends JavaPlugin {

    private static EliteEnchants instance;

    public static EliteEnchants getInstance() {
        return instance;
    }

    private ConfigManager configManager;
    private TicketManager ticketManager;
    private EnchantAPI enchantAPI;
    private EnchantManager enchantManager;

    @Override
    public void onEnable() {
        instance = this;

        File players = new File(getDataFolder() + "/Players/");
        if (!players.exists()) {
            players.mkdirs();
        }

        File enchantments = new File(getDataFolder() + "/Enchantments/");
        if (!enchantments.exists()) {
            enchantments.mkdirs();
        }

        // Loading Config System
        configManager = new ConfigManager();
        ticketManager = new TicketManager();
        enchantAPI = new EnchantAPI();
        enchantManager = new EnchantManager();

        loadLanguage();

        getCommand("eliteenchants").setExecutor(new EliteEnchantsCommand());
        getCommand("tickets").setExecutor(new TicketsCommand());
        getCommand("enchant").setExecutor(new EnchantCommand());
        getCommand("withdraw").setExecutor(new WithdrawCommand());
        getCommand("ticketpay").setExecutor(new TicketPayCommand());
        getCommand("redeem").setExecutor(new RedeemCommand());
        getCommand("convertxp").setExecutor(new ConvertCommand());
        getCommand("disenchant").setExecutor(new DisenchantCommand());

        getServer().getPluginManager().registerEvents(new EnchantGUI(), this);
        getServer().getPluginManager().registerEvents(new VoucherRedeem(), this);
        getServer().getPluginManager().registerEvents(new TicketRedeem(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractHandler(), this);

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new ClipPlaceHolder(this).register();
        }

    }

    public void loadLanguage() {
        configManager.loadConfig("lang.yml");
        ConfigObject configObject = configManager.getConfig("lang.yml");

        int amount = 0;
        amount += configObject.loadDefault("header", "&8&m---------------------------------");
        amount += configObject.loadDefault("footer", "&8&m---------------------------------");

        if(amount > 0) {
            configObject.save();
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public EnchantManager getEnchantManager() {
        return enchantManager;
    }

    public TicketManager getTicketManager() {
        return ticketManager;
    }

    public EnchantAPI getEnchantAPI() {
        return enchantAPI;
    }

}
