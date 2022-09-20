package com.tobiassteely.eliteenchants.ticket;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.config.ConfigObject;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TicketPlayer {

    private ConfigObject config;
    private OfflinePlayer player;

    public TicketPlayer(OfflinePlayer player) {
        this.player = player;
        this.config = EliteEnchants.getInstance().getConfigManager().getConfig("Players/" + player.getUniqueId() + ".yml");
        if (config.loadDefault("tickets", 0) > 0) {
            config.save();
        }
    }

    public double addTickets(double amount) {
        double tickets = config.getFileConfiguration().getDouble("tickets");
        tickets += amount;
        config.getFileConfiguration().set("tickets", tickets);
        config.save();
        return tickets;
    }

    public double removeTickets(double amount) {
        double tickets = config.getFileConfiguration().getDouble("tickets");
        tickets -= amount;
        if(tickets < 0)
            tickets = 0;
        config.getFileConfiguration().set("tickets", tickets);
        config.save();
        return tickets;
    }

    public double setTickets(double amount) {
        if(amount < 0)
            amount = 0;
        config.getFileConfiguration().set("tickets", amount);
        config.save();
        return amount;
    }

    public double getTickets() {
        return config.getFileConfiguration().getDouble("tickets");
    }

}
