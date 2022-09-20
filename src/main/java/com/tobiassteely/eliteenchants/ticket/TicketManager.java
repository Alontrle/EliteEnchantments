package com.tobiassteely.eliteenchants.ticket;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TicketManager {

    private HashMap<String, TicketPlayer> ticketPlayerUUIDMap;

    public TicketManager() {
        ticketPlayerUUIDMap = new HashMap<>();
    }

    public void removePlayer(OfflinePlayer player) {
        ticketPlayerUUIDMap.remove(player.getUniqueId().toString());
    }

    public TicketPlayer getPlayer(OfflinePlayer player) {
        if(!ticketPlayerUUIDMap.containsKey(player.getUniqueId().toString())) {
            TicketPlayer ticketPlayer = new TicketPlayer(player);
            ticketPlayerUUIDMap.put(player.getUniqueId().toString(), ticketPlayer);
            return ticketPlayer;
        }
        return ticketPlayerUUIDMap.get(player.getUniqueId().toString());
    }

}
