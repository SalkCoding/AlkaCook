package net.alkacook.food;

import net.alkacook.untill.Untill;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class Food {

    private String name;
    private List<String> lore;
    private String skinId;
    private String skinValue;
    private List<ItemStack> ingredient;
    private int foodLevel;
    private List<PotionEffect> potionList;
    private double exp;
    private boolean isFood;
    private ItemStack itemStack;

    public Food(String name,
                List<String> lore,
                String id,
                String skinValue,
                List<ItemStack> ingredient,
                int foodLevel,
                List<PotionEffect> potionList,
                double exp,
                boolean isFood,
                Material type) {
        this.name = name;
        this.lore = lore;
        this.skinId = id;
        this.skinValue = skinValue;
        this.ingredient = ingredient;
        this.foodLevel = foodLevel;
        this.potionList = potionList;
        this.exp = exp;
        this.isFood = isFood;
        ItemStack foodItem;
        ItemMeta foodItemMeta;
        if (type == Material.SKULL_ITEM) {
            foodItem = Untill.getCustomSkull(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), skinId, skinValue);
        } else
            foodItem = new ItemStack(type, 1);
        foodItemMeta = foodItem.getItemMeta();
        foodItemMeta.setDisplayName(name);//Set food name
        foodItemMeta.setLore(lore);//Set food lore
        foodItem.setItemMeta(foodItemMeta);
        this.itemStack = foodItem;
    }

    public String getDisplayName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<ItemStack> getIngredient() {
        return ingredient;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public List<PotionEffect> getPotionList() {
        return potionList;
    }

    public double getExp() {
        return exp;
    }

    public boolean isFood() {
        return isFood;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
