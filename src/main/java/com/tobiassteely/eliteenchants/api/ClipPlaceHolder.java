/*
 * =-------------------------------------=
 * = Copyright (c) AndrewAubury 2017 =
 * =  https://www.AndrewAubury.me   =
 * =-------------------------------------=
 */

package com.tobiassteely.eliteenchants.api;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.ticket.TicketPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * Created by Andrew on 12/12/2017.
 */
public class ClipPlaceHolder extends PlaceholderExpansion {

    private EliteEnchants ourPlugin;

    public ClipPlaceHolder(EliteEnchants ourPlugin) {
        this.ourPlugin = ourPlugin;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return ourPlugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier(){
        return "eliteenchants";
    }

    @Override
    public String getVersion(){
        return ourPlugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        //UNDER THIS POINT IS PLAYER ONLY PLACEHOLDERS!!!
        // always check if the player is null for placeholders related to the player!
        if (player == null) {
            return "";
        }
        if (identifier.equals("tickets")) {
            TicketPlayer ticketPlayer = ourPlugin.getTicketManager().getPlayer(player);
            return ("" + (int)ticketPlayer.getTickets());
        }


        // anything else someone types is invalid because we never defined %customplaceholder_<what they want a value for>%
        // we can just return null so the placeholder they specified is not replaced.
        return null;
    }
}
