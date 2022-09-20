package com.tobiassteely.eliteenchants.command;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.API;
import com.tobiassteely.eliteenchants.api.Log;
import com.tobiassteely.eliteenchants.api.config.ConfigObject;
import com.tobiassteely.eliteenchants.enchant.Enchant;
import com.tobiassteely.eliteenchants.ticket.TicketManager;
import com.tobiassteely.eliteenchants.ticket.TicketPlayer;
import com.tobiassteely.eliteenchants.voucher.VoucherManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EliteEnchantsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("EliteEnchants") || label.equalsIgnoreCase("ee") || label.equalsIgnoreCase("ta") || label.equalsIgnoreCase("te")){
            Player p;
            API api = new API();

            try {
                p = (Player)sender;
            } catch (ClassCastException ex) {
                p = null;
            }

            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")) {
                    if(args.length == 3) {
                        Player player = Bukkit.getPlayer(args[1]);
                        OfflinePlayer offlinePlayer = null;
                        if(player == null) {
                            offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                            if(offlinePlayer != null) {
                                player = offlinePlayer.getPlayer();
                            }
                        }

                        if(player != null) {
                            TicketPlayer ticketPlayer = EliteEnchants.getInstance().getTicketManager().getPlayer(player);
                            try {
                                api.sendMessage(sender,
                                        "You have added " + args[2] + " ticket(s) to " + player.getName() +
                                                "'s balance. They now have " + ticketPlayer.addTickets(Double.parseDouble(args[2])) + " ticket(s).");
                            } catch (Exception ex) {
                                api.sendMessage(player, "That is not a valid number.");
                            }
                            if(offlinePlayer != null) {
                                EliteEnchants.getInstance().getTicketManager().removePlayer(player);
                            }
                        } else {
                            api.sendMessage(sender, "That is not a valid player!");
                        }
                    } else {
                        api.sendMessage(sender, "/ee give <player> <amount>");
                    }
                    return true;
                } else if(args[0].equalsIgnoreCase("set")) {
                    if(args.length == 3) {
                        Player player = Bukkit.getPlayer(args[1]);
                        OfflinePlayer offlinePlayer = null;
                        if(player == null) {
                            offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                            if(offlinePlayer != null) {
                                player = offlinePlayer.getPlayer();
                            }
                        }

                        if(player != null) {
                            TicketPlayer ticketPlayer = EliteEnchants.getInstance().getTicketManager().getPlayer(player);
                            try {
                                api.sendMessage(sender,
                                        "You set " + player.getName() + " ticket balance to " + ticketPlayer.setTickets(Double.parseDouble(args[2])) +
                                                "'s balance.");
                            } catch (Exception ex) {
                                api.sendMessage(player, "That is not a valid number.");
                            }
                            if(offlinePlayer != null) {
                                EliteEnchants.getInstance().getTicketManager().removePlayer(player);
                            }
                        } else {
                            api.sendMessage(sender, "That is not a valid player!");
                        }
                    } else {
                        api.sendMessage(sender, "/ee set <player> <amount>");
                    }
                    return true;
                } else if(args[0].equalsIgnoreCase("remove")) {
                    if(args.length == 3) {
                        Player player = Bukkit.getPlayer(args[1]);
                        OfflinePlayer offlinePlayer = null;
                        if (player == null) {
                            offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                            if (offlinePlayer != null) {
                                player = offlinePlayer.getPlayer();
                            }
                        }

                        if (player != null) {
                            TicketPlayer ticketPlayer = EliteEnchants.getInstance().getTicketManager().getPlayer(player);
                            try {
                                api.sendMessage(sender,
                                        "You have removed " + args[2] + " ticket(s) from " + player.getName() +
                                                "'s balance. They now have " + ticketPlayer.removeTickets(Double.parseDouble(args[2])) + " ticket(s).");
                            } catch (Exception ex) {
                                api.sendMessage(player, "That is not a valid number.");
                            }
                            if (offlinePlayer != null) {
                                EliteEnchants.getInstance().getTicketManager().removePlayer(player);
                            }
                        } else {
                            api.sendMessage(sender, "That is not a valid player!");
                        }
                    } else {
                        api.sendMessage(sender, "/ee remove <player> <amount>");
                    }
                    return true;
                } else if(args[0].equalsIgnoreCase("reset")) {
                        if(args.length == 2) {
                            Player player = Bukkit.getPlayer(args[1]);
                            OfflinePlayer offlinePlayer = null;
                            if (player == null) {
                                offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                                if (offlinePlayer != null) {
                                    player = offlinePlayer.getPlayer();
                                }
                            }

                            if (player != null) {
                                TicketPlayer ticketPlayer = EliteEnchants.getInstance().getTicketManager().getPlayer(player);
                                ticketPlayer.setTickets(0);
                                api.sendMessage(sender, "You have reset " + args[1] + "'s ticket balance.");
                                if (offlinePlayer != null) {
                                    EliteEnchants.getInstance().getTicketManager().removePlayer(player);
                                }
                            } else {
                                api.sendMessage(sender, "That is not a valid player!");
                            }
                        } else {
                            api.sendMessage(sender, "/ee reset <player>");
                        }
                    return true;
                } else if(args[0].equalsIgnoreCase("enchant")) {

                    if(args.length == 4) {

                        Player player = Bukkit.getPlayer(args[1]);
                        if (player == null) {
                            player = Bukkit.getOfflinePlayer(args[1]).getPlayer();
                        }

                        if(player != null) {
                            try {
                                if(EliteEnchants.getInstance().getEnchantAPI().enchantItem(player.getInventory().getItemInHand(), args[2], Integer.parseInt(args[3]))) {
                                    api.sendMessage(sender, "You have successfully enchanted that item!");
                                } else {
                                    api.sendMessage(sender, "That is not a valid enchantment!");
                                }
                            } catch (Exception ex) {
                                api.sendMessage(sender, "That is not a valid number!");
                            }
                        } else {
                            api.sendMessage(sender, "That is not a valid player!");
                        }

                    } else {
                        api.sendMessage(sender, "/ee enchant <player> <enchantment> <amount>");
                    }
                    return true;
                } else if(args[0].equalsIgnoreCase("import")) {

                    File folder = new File("plugins/TicketEnchant/userdata/");
                    if(folder.exists()) {
                        try {
                            Stream<Path> walk = Files.walk(folder.toPath());
                            List<Path> result = walk.filter(Files::isRegularFile).collect(Collectors.toList());
                            HashMap<String, Path> paths = new HashMap<>();

                            for(Path path : result) {
                                if(!paths.containsKey(path.toString()) && path.toString().endsWith(".yml")) {

                                    YamlConfiguration config = new YamlConfiguration();
                                    config.load(path.toFile());

                                    String uuid = path.toString();
                                    uuid = uuid.replace(folder.toString() + "/", "");
                                    uuid = uuid.replace(".yml", "");

                                    ConfigObject configObject = new ConfigObject("/Players/" + uuid + ".yml");
                                    if(configObject.loadDefault("tickets", config.getDouble("Tickets")) > 0) {
                                        configObject.save();
                                    }

                                }
                                paths.put(path.toString(), path);
                            }

                            api.sendMessage(sender, "Successfully imported users!");
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                } else if(args.length == 4 && args[0].equalsIgnoreCase("voucher")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if(player == null) {
                        api.sendMessage(sender, "&cInvalid player!");
                        return true;
                    }
                    Enchant enchant = EliteEnchants.getInstance().getEnchantManager().getEnchantByName(args[2]);

                    if(enchant == null) {
                        api.sendMessage(sender, "&cInvalid enchantment!");
                        return true;
                    }

                    try {
                        player.getInventory().addItem(VoucherManager.getEnchantVoucher(enchant, Integer.parseInt(args[3])));
                        api.sendMessage(sender, "&aYou have given " + player.getName() + " a " + enchant.getName() + " voucher with " + args[3] + " level(s)!");
                    } catch (Exception ex) {
                        api.sendMessage(sender, "&cInvalid number");
                    }
                    return true;
                }
            }
            api.sendMessage(sender, api.getHeader(), false);
            api.sendMessage(sender, "/ee give <player> <amount>", false);
            api.sendMessage(sender, "/ee set <player> <amount>", false);
            api.sendMessage(sender, "/ee remove <player> <amount>", false);
            api.sendMessage(sender, "/ee reset <player>", false);
            api.sendMessage(sender, "/ee enchant <player> <enchantment> <amount>", false);
            api.sendMessage(sender, "/ee import - Imports from Ticket Enchant", false);
            api.sendMessage(sender, "/ee voucher <player> <enchantment> <levels>", false);
            api.sendMessage(sender, api.getFooter(), false);
        }
        return true;
    }

}
