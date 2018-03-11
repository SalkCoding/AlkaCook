package net.alkacook.config;

import net.alkacook.food.Food;
import net.alkacook.rank.FoodRank;
import net.alkacook.main.Main;
import net.alkacook.untill.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.jline.utils.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodLoader {

    public static HashMap<String, Food> loadFoodList() throws IOException {
        Path path = Main.getInstance().getDataFolder().toPath().resolve("Food");
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        HashMap<String, Food> list = new HashMap<>();
        File[] files = path.toFile().listFiles();
        if (files == null) return list;
        for (File file : files) {
            try {
                LinmaluYamlConfiguration config = LinmaluYamlConfiguration.loadConfiguration(file);
                String name = config.getString("Name");
                List<String> lore = config.getStringList("Lore");
                String skinId = config.getString("SkinId");
                String skinValue = config.getString("SkinValue");
                int foodLevel = config.getInt("Hunger");
                List<PotionEffect> potionEffectList = (List<PotionEffect>) config.getList("PotionEffect");
                double exp = config.getDouble("Exp");
                boolean isFood = config.getBoolean("IsFood");
                Material type = Material.valueOf(config.getString("Type"));

                ArrayList<ItemStack> ingredient = new ArrayList<>();
                ArrayList<Material> overlapType = new ArrayList<>();
                for (String root : config.getKeys(false)) {
                    ConfigurationSection rootSection = config.getConfigurationSection(root);
                    if (rootSection != null) {
                        for (String child : rootSection.getKeys(false)) {
                            ConfigurationSection childSection = rootSection.getConfigurationSection(child);
                            ItemStack item = getOtherFood(files, child, childSection);
                            if (item != null) {
                                ingredient.add(item);
                            } else {
                                String ingredientName = childSection.getString("DisplayName");
                                List<String> ingredientLore = childSection.getStringList("Lore");
                                String strType = childSection.getString("Type");
                                Material ingredientType;
                                if (strType.contains(" ")) {
                                    for (String str : strType.split(" ")) {
                                        overlapType.add(Material.valueOf(str));
                                    }
                                    ingredientType = overlapType.get(0);
                                } else
                                    ingredientType = Material.valueOf(strType);
                                int ingredientAmount = childSection.getInt("Amount");
                                String specialType = childSection.getString("SpecialType");
                                ItemStack result;
                                if (specialType == null || specialType.equalsIgnoreCase("0"))
                                    result = new ItemStack(ingredientType, ingredientAmount);
                                else {
                                    result = new ItemStack(ingredientType, ingredientAmount, Byte.parseByte(specialType));
                                }
                                ItemMeta meta = result.getItemMeta();
                                meta.setDisplayName(ingredientName);
                                meta.setLore(ingredientLore);
                                result.setItemMeta(meta);
                                ingredient.add(result);
                            }
                        }
                    }
                }
                Food customFood = new Food(name, lore, skinId, skinValue, ingredient, overlapType, foodLevel, potionEffectList, exp, isFood, type);
                list.put(name, customFood);
                list.put(file.getName().split(".yml")[0], customFood);//For administration command
                Bukkit.getLogger().info(Constants.Console + file.getName() + " is loaded!");
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getLogger().warning(Constants.Console + "Error is occurred in " + file.getName());
            }
        }

        return list;
    }

    private static ItemStack getOtherFood(File[] files, String root, ConfigurationSection section) {
        for (File file : files) {
            if (file.getName().split(".yml")[0].equals(root)) {
                LinmaluYamlConfiguration config = LinmaluYamlConfiguration.loadConfiguration(file);
                String name = config.getString("Name");
                Material type = Material.valueOf(config.getString("Type"));
                List<String> lore = config.getStringList("Lore");
                ItemStack food = new ItemStack(type, section.getInt("Amount"));
                ItemMeta meta = food.getItemMeta();
                meta.setDisplayName(name);
                meta.setLore(lore);
                food.setItemMeta(meta);
                Bukkit.getLogger().info(Constants.Console + file.getName() + " is reused!");
                FoodRank.addElement(name);
                return food;
            }
        }
        return null;
    }

}
