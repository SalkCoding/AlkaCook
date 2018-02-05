package net.alkacook.config;

import net.alkacook.food.Food;
import net.alkacook.main.Main;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodLoader {

    public static HashMap<String, Food> loadFoodList() {
        File dir = Main.getInstance().getDataFolder();
        File dir1 = new File(dir, "Food");
        if (!dir.exists())
            dir.mkdirs();
        if (!dir1.exists())
            dir1.mkdirs();
        HashMap<String, Food> list = new HashMap<>();
        File[] files = dir1.listFiles();
        for (File file : files) {
            LinmaluYamlConfiguration config = LinmaluYamlConfiguration.loadConfiguration(file);
            String name = config.getString("Name");
            List<String> lore = config.getStringList("Lore");
            String iconName = config.getString("Icon");
            boolean glow = config.getBoolean("Glow");
            List<ItemStack> ingredient = (List<ItemStack>) config.getList("Ingredient");
            int foodLevel = config.getInt("Hunger");
            ArrayList<PotionEffect> potionList = new ArrayList<>();
            int amplifier = config.getInt("Amplifier");
            int duration = config.getInt("Duration");
            for (String typeName : config.getStringList("PotionEffect")) {
                PotionEffectType type = PotionEffectType.getByName(typeName);
                PotionEffect effect = type.createEffect(duration, amplifier);
                potionList.add(effect);
            }
            Food customFood = new Food(name, lore, iconName, glow, ingredient, foodLevel, potionList);
            list.put(name, customFood);
        }
        return list;
    }

}
