package net.alkacook.config;

import net.alkacook.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FoodWriter {

    public static void writeTestFile() throws IOException {
        Path path = Main.getInstance().getDataFolder().toPath().resolve("Food");
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        File testFile = new File(path.toFile(), "TestFood.yml");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("Test");
        lore.add("Food");
        lore.add("Lore");
        ArrayList<IngredientConfig> ingredient = new ArrayList<>();
        ingredient.add(new IngredientConfig(Material.RED_ROSE, "장미", ChatColor.RED + "좋은거", lore, 2,null));
        ingredient.add(new IngredientConfig(Material.SUGAR, "설탕", ChatColor.RED + "좋은거", lore, 1,null));
        ArrayList<PotionEffect> potionEffects = new ArrayList<>();
        potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 0, 20));
        potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 1, 20));
        LinmaluYamlConfiguration config = LinmaluYamlConfiguration.loadConfiguration(testFile);
        config.set("Name", "TestFood");
        config.set("Type", Material.SKULL_ITEM.toString());
        config.set("Lore", lore);
        config.set("SkinId", "317e4fcb-cd8f-401f-bd6e-1123f2089414");
        config.set("SkinValue","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNkZTZlMzQ2MWM4OGZkNTVlYTNlNmE2OTVhNDhmNmQ0ZWFkYzFlYzI3OTc4MmFhYmIyZWNjMzI2M2U5ODYifX19");
        //config.set("Glow", true);
        for (IngredientConfig ingredientConfig : ingredient) {
            String name = ingredientConfig.getName();
            config.set("Ingredient." + name + ".DisplayName", ingredientConfig.getDisplayName());
            config.set("Ingredient." + name + ".Lore", ingredientConfig.getLore().toArray());
            config.set("Ingredient." + name + ".Type", ingredientConfig.getType().toString());
            config.set("Ingredient." + name + ".Amount", ingredientConfig.getAmount());
            config.set("Ingredient." + name + ".SpecialType", ingredientConfig.getSpecialType());
        }
        config.set("SpecialType",0);
        config.set("RemoveHarmful", true);
        config.set("Hunger", 8);
        config.set("PotionEffect", potionEffects);
        config.set("Exp", 0);
        config.set("IsFood", true);
        config.save(testFile);
    }

}
