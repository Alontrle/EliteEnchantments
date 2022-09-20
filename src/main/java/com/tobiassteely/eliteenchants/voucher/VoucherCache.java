package com.tobiassteely.eliteenchants.voucher;

import com.tobiassteely.eliteenchants.enchant.Enchant;
import org.bukkit.entity.Player;

import java.util.UUID;

public class VoucherCache {

    private Enchant enchant;
    private int levels;
    private UUID uuid;

    public VoucherCache(Enchant enchant, int levels, UUID uuid) {
        this.enchant = enchant;
        this.levels = levels;
        this.uuid = uuid;
    }

    public Enchant getEnchant() {
        return enchant;
    }

    public int getLevels() {
        return levels;
    }

    public UUID getUuid() {
        return uuid;
    }
}
