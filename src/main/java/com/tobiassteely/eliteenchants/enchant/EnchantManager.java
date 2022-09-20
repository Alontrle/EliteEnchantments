package com.tobiassteely.eliteenchants.enchant;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.API;
import com.tobiassteely.eliteenchants.api.Log;
import com.tobiassteely.eliteenchants.api.config.ConfigObject;
import com.tobiassteely.eliteenchants.enchant.potion.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerCommandEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnchantManager {

    private ArrayList<Enchant> enchants;
    private HashMap<String, Enchant> enchantNameCache ;
    private HashMap<String, Enchant> enchantDisplayNameCache;

    public EnchantManager() {
        reload();
    }

    public void registerEnchantment(Enchant enchant) {
        EliteEnchants.getInstance().getServer().getPluginManager().registerEvents(enchant, EliteEnchants.getInstance());
        this.enchants.add(enchant);
        this.enchantNameCache.put(enchant.getName().toLowerCase(), enchant);
        this.enchantDisplayNameCache.put(new API().cc(enchant.getDisplayName()), enchant);
    }

    public void unregisterEnchantment(Enchant enchant) {
        for(int i = 0; i < enchants.size(); i++) {
            Enchant ench = enchants.get(i);
            if(ench.getName().equalsIgnoreCase(enchant.getName())) {
                enchants.remove(i);
                break;
            }
        }
        this.enchantNameCache.remove(enchant.getName());
        this.enchantNameCache.remove(enchant.getDisplayName());

        HandlerList.unregisterAll(enchant);
    }

    public Enchant getEnchantByName(String string) {
        if(enchantNameCache.containsKey(string.toLowerCase()))
            return enchantNameCache.get(string.toLowerCase());
        return null;
    }

    public Enchant getEnchantByDisplayName(String string) {
        if(enchantDisplayNameCache.containsKey(string))
            return enchantDisplayNameCache.get(string);
        return null;
    }

    public ArrayList<Enchant> getEnchants() {
        return enchants;
    }

    public ArrayList<Enchant> getEnchantsByTool(String tool) {
        ArrayList<Enchant> enchantsToSend = new ArrayList<>();
        for (Enchant enchant : enchants) {
            for (String enchantTool : enchant.getTools()) {
                if(enchantTool.equalsIgnoreCase("any")) {
                    enchantsToSend.add(enchant);
                    break;
                }
                if(enchantTool.equalsIgnoreCase("axe")) {
                    if (tool.toUpperCase().contains(enchantTool)) {
                        if (!tool.toUpperCase().contains("PICKAXE")) {
                            enchantsToSend.add(enchant);
                            break;
                        }
                    }
                }
                else if(enchantTool.equalsIgnoreCase("pickaxe")) {
                    if (tool.toUpperCase().contains("PICKAXE")) {
                        enchantsToSend.add(enchant);
                        break;
                    }
                }
                else if(enchantTool.equalsIgnoreCase("tool")) {
                    if (tool.toUpperCase().contains("AXE")) {
                        enchantsToSend.add(enchant);
                        break;
                    } else if (tool.toUpperCase().contains("SPADE")) {
                        enchantsToSend.add(enchant);
                        break;
                    } else if (tool.contains("HOE")) {
                        enchantsToSend.add(enchant);
                        break;
                    }
                }
                else if(enchantTool.equalsIgnoreCase("armor")) {
                    if (tool.toUpperCase().contains("HELMET")) {
                        enchantsToSend.add(enchant);
                        break;
                    } else if (tool.toUpperCase().contains("CHESTPLATE")) {
                        enchantsToSend.add(enchant);
                        break;
                    } else if (tool.toUpperCase().contains("LEGGINGS")) {
                        enchantsToSend.add(enchant);
                        break;
                    } else if (tool.toUpperCase().contains("BOOTS")) {
                        enchantsToSend.add(enchant);
                        break;
                    }
                }
                else if(enchantTool.equalsIgnoreCase("weapon")) {
                    if (tool.toUpperCase().contains("SWORD")) {
                        enchantsToSend.add(enchant);
                        break;
                    }
                }
                else {
                    if (tool.toUpperCase().contains(enchantTool)) {
                        enchantsToSend.add(enchant);
                        break;
                    }
                }
            }
        }
        return enchantsToSend;
    }

    public void reload() {
        this.enchants = new ArrayList<>();
        this.enchantNameCache = new HashMap<>();
        this.enchantDisplayNameCache = new HashMap<>();

        EliteEnchants.getInstance().getConfigManager().loadConfig("enchantments.yml");
        ConfigObject configObject = EliteEnchants.getInstance().getConfigManager().getConfig("enchantments.yml");

        int updated = 0;
        updated += configObject.loadDefault("settings.command-prefix", "&8[&3Crates&8]");
        updated += configObject.loadDefault("settings.enchantment-prefix", "&8- &7");
        updated += configObject.loadDefault("settings.enabled", true);

        List<String> enabledItems = new ArrayList<>();
        enabledItems.add("TOOL");

        updated += configObject.loadDefault("enchantments.Fortune.enabled", true);
        updated += configObject.loadDefault("enchantments.Fortune.name", "&3Fortune");
        updated += configObject.loadDefault("enchantments.Fortune.lore", Arrays.asList("&7Increases the amount of", "&7blocks you collect", "&7while mining with autosell!"));
        updated += configObject.loadDefault("enchantments.Fortune.item", "DIAMOND");
        updated += configObject.loadDefault("enchantments.Fortune.max-level", 10);
        updated += configObject.loadDefault("enchantments.Fortune.cost.currency", "tickets");
        updated += configObject.loadDefault("enchantments.Fortune.cost.base", 1000.0);
        updated += configObject.loadDefault("enchantments.Fortune.cost.increase", "flat");
        updated += configObject.loadDefault("enchantments.Fortune.cost.increment", 10.0);
        updated += configObject.loadDefault("enchantments.Fortune.enabled-items", enabledItems);

        enabledItems = new ArrayList<>();
        enabledItems.add("PICKAXE");
        enabledItems.add("AXE");

        updated += configObject.loadDefault("enchantments.Efficiency.enabled", true);
        updated += configObject.loadDefault("enchantments.Efficiency.name", "&4Efficiency");
        updated += configObject.loadDefault("enchantments.Efficiency.lore", Arrays.asList("&7Increases the speed in", "&7which you mine."));
        updated += configObject.loadDefault("enchantments.Efficiency.item", "FEATHER");
        updated += configObject.loadDefault("enchantments.Efficiency.max-level", 250);
        updated += configObject.loadDefault("enchantments.Efficiency.cost.currency", "tickets");
        updated += configObject.loadDefault("enchantments.Efficiency.cost.base", 100.0);
        updated += configObject.loadDefault("enchantments.Efficiency.cost.increase", "multiplier");
        updated += configObject.loadDefault("enchantments.Efficiency.cost.increment", 2.0);
        updated += configObject.loadDefault("enchantments.Efficiency.enabled-items", enabledItems);

        enabledItems = new ArrayList<>();
        enabledItems.add("PICKAXE");
        enabledItems.add("ARMOR");

        updated += configObject.loadDefault("enchantments.Unbreaking.enabled", true);
        updated += configObject.loadDefault("enchantments.Unbreaking.name", "&4Unbreaking");
        updated += configObject.loadDefault("enchantments.Unbreaking.lore", Arrays.asList("&7Makes your tools last longer"));
        updated += configObject.loadDefault("enchantments.Unbreaking.item", "BEDROCK");
        updated += configObject.loadDefault("enchantments.Unbreaking.max-level", 250);
        updated += configObject.loadDefault("enchantments.Unbreaking.cost.currency", "tickets");
        updated += configObject.loadDefault("enchantments.Unbreaking.cost.base", 10.0);
        updated += configObject.loadDefault("enchantments.Unbreaking.cost.increase", "exponential");
        updated += configObject.loadDefault("enchantments.Unbreaking.cost.increment", 1.2);
        updated += configObject.loadDefault("enchantments.Unbreaking.enabled-items", enabledItems);

        enabledItems = new ArrayList<>();
        enabledItems.add("ARMOR");

        updated += configObject.loadDefault("enchantments.Protection.enabled", true);
        updated += configObject.loadDefault("enchantments.Protection.name", "&3Protection");
        updated += configObject.loadDefault("enchantments.Protection.lore", Arrays.asList("&7Keeps you safe in a fight!"));
        updated += configObject.loadDefault("enchantments.Protection.item", "SHIELD");
        updated += configObject.loadDefault("enchantments.Protection.max-level", 250);
        updated += configObject.loadDefault("enchantments.Protection.cost.currency", "tickets");
        updated += configObject.loadDefault("enchantments.Protection.cost.base", 10.0);
        updated += configObject.loadDefault("enchantments.Protection.cost.increase", "none");
        updated += configObject.loadDefault("enchantments.Protection.cost.increment", 0.0);
        updated += configObject.loadDefault("enchantments.Protection.enabled-items", enabledItems);

        enabledItems = new ArrayList<>();
        enabledItems.add("ARMOR");

        updated += configObject.loadDefault("enchantments.ProjectileProtection.enabled", true);
        updated += configObject.loadDefault("enchantments.ProjectileProtection.name", "&2Projectile Protection");
        updated += configObject.loadDefault("enchantments.ProjectileProtection.lore", Arrays.asList("&7Protect your self from raining-death."));
        updated += configObject.loadDefault("enchantments.ProjectileProtection.item", "BOW");
        updated += configObject.loadDefault("enchantments.ProjectileProtection.max-level", 250);
        updated += configObject.loadDefault("enchantments.ProjectileProtection.cost.currency", "tickets");
        updated += configObject.loadDefault("enchantments.ProjectileProtection.cost.base", 10.0);
        updated += configObject.loadDefault("enchantments.ProjectileProtection.cost.increase", "none");
        updated += configObject.loadDefault("enchantments.ProjectileProtection.cost.increment", 0.0);
        updated += configObject.loadDefault("enchantments.ProjectileProtection.enabled-items", enabledItems);

        enabledItems = new ArrayList<>();
        enabledItems.add("ARMOR");

        updated += configObject.loadDefault("enchantments.FireProtection.enabled", true);
        updated += configObject.loadDefault("enchantments.FireProtection.name", "&CFire Protection");
        updated += configObject.loadDefault("enchantments.FireProtection.lore", Arrays.asList("&7Protect your self from fiery-death."));
        updated += configObject.loadDefault("enchantments.FireProtection.item", "FLINT_AND_STEEL");
        updated += configObject.loadDefault("enchantments.FireProtection.max-level", 250);
        updated += configObject.loadDefault("enchantments.FireProtection.cost.currency", "tickets");
        updated += configObject.loadDefault("enchantments.FireProtection.cost.base", 10.0);
        updated += configObject.loadDefault("enchantments.FireProtection.cost.increase", "none");
        updated += configObject.loadDefault("enchantments.FireProtection.cost.increment", 0.0);
        updated += configObject.loadDefault("enchantments.FireProtection.enabled-items", enabledItems);

        enabledItems = new ArrayList<>();
        enabledItems.add("SWORD");
        enabledItems.add("AXE");

        updated += configObject.loadDefault("enchantments.Sharpness.enabled", true);
        updated += configObject.loadDefault("enchantments.Sharpness.name", "&cSharpness");
        updated += configObject.loadDefault("enchantments.Sharpness.lore", Arrays.asList("&7Makes your weapons do more damage!"));
        updated += configObject.loadDefault("enchantments.Sharpness.item", "DIAMOND_SWORD");
        updated += configObject.loadDefault("enchantments.Sharpness.max-level", 5);
        updated += configObject.loadDefault("enchantments.Sharpness.cost.currency", "tickets");
        updated += configObject.loadDefault("enchantments.Sharpness.cost.base", 100.0);
        updated += configObject.loadDefault("enchantments.Sharpness.cost.increase", "none");
        updated += configObject.loadDefault("enchantments.Sharpness.cost.increment", 0.0);
        updated += configObject.loadDefault("enchantments.Sharpness.enabled-items", enabledItems);

        enabledItems = new ArrayList<>();
        enabledItems.add("SWORD");
        enabledItems.add("AXE");

        updated += configObject.loadDefault("enchantments.FireAspect.enabled", true);
        updated += configObject.loadDefault("enchantments.FireAspect.name", "&4Fire Aspect");
        updated += configObject.loadDefault("enchantments.FireAspect.lore", Arrays.asList("&7Inflict fiery pain on your enemies."));
        updated += configObject.loadDefault("enchantments.FireAspect.item", "DIAMOND_SWORD");
        updated += configObject.loadDefault("enchantments.FireAspect.max-level", 5);
        updated += configObject.loadDefault("enchantments.FireAspect.cost.currency", "tickets");
        updated += configObject.loadDefault("enchantments.FireAspect.cost.base", 100.0);
        updated += configObject.loadDefault("enchantments.FireAspect.cost.increase", "none");
        updated += configObject.loadDefault("enchantments.FireAspect.cost.increment", 0.0);
        updated += configObject.loadDefault("enchantments.FireAspect.enabled-items", enabledItems);

        if(updated > 0)
            configObject.save();

        Set<String> enchants = configObject.getFileConfiguration().getConfigurationSection("enchantments").getValues(false).keySet();

        for (String enchant : enchants) {
            if (configObject.getFileConfiguration().getBoolean("enchantments." + enchant + ".enabled")) {
                Enchant enchantObject = new Enchant(enchant);

                enchant = enchant.replace(" ", "").replace("_", "").replace("-", "");

                if(enchant.equalsIgnoreCase("Fortune")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Looting")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Power")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Flame")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Infinity")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Punch")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Binding")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Sharpness")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Smite")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("BaneOfAthropods")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("DepthStrider")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Efficiency")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Unbreaking")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("FireAspect")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("FrostWalker")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Knockback")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Luck")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Lure")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Mending")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Respiration")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Protection")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("BlastProtection")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("FallProtection")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("FireProtection")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("ProjectileProtection")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("SilkTouch")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("SweepingEdge")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Thorns")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("Vanishing")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                } else if(enchant.equalsIgnoreCase("AquaAffinity")) {
                    enchantObject.setVanilla(true);
                    registerEnchantment(enchantObject);
                }
            }
        }

        // Loads Potion Enchantments
        registerEnchantment(new Haste());
        registerEnchantment(new HealthBoost());
        registerEnchantment(new Nightvision());
        registerEnchantment(new RegenEnchant());
        registerEnchantment(new Speed());
        registerEnchantment(new Strength());

        // Loads custom Enchantments

        try {
            Stream<Path> walk = Files.walk(Paths.get(EliteEnchants.getInstance().getDataFolder()+ "/Enchantments/"));
            List<Path> result = walk.filter(Files::isRegularFile).collect(Collectors.toList());
            HashMap<String, Path> paths = new HashMap<>();

            for(Path path : result) {
                if(!paths.containsKey(path.toString()) && path.toString().endsWith(".jar")) {
                    loadExternalEnchantment(path);
                }
                paths.put(path.toString(), path);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void loadExternalEnchantment(Path file){
        try {
            String inputFile = "jar:file:" + file.toString() + "!/enchantment.json";
            URL inputURL = new URL(inputFile);
            JarURLConnection conn = (JarURLConnection)inputURL.openConnection();
            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            StringBuilder fileData = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fileData.append(line + System.lineSeparator());
            }

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(String.valueOf(fileData));

            String classPath = (String)json.get("mainClass");

            EnchantLoader<Enchant> loader = new EnchantLoader<>();

            Enchant enchant = loader.LoadClass(file.toFile(), classPath, Enchant.class);

            if(enchant != null) {
                Log.sendMessage(0, "Successfully loaded " + enchant.getName());
                registerEnchantment(enchant);
            } else {
                Log.sendMessage(2, "Failed to load external enchantment " + file.toString());
            }

        } catch (IOException | ParseException e1) {
            Log.sendMessage(2, "Failed while loading external enchantment " + file.toString());
            e1.printStackTrace();
        }
    }
}
