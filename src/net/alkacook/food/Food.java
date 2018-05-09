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
    private List<ItemStack> ingredient;
    private List<Material> overlapType;
    private int foodLevel;
    private List<PotionEffect> potionList;
    private double exp;
    private boolean isFood;
    private boolean removeHarmful;
    private ItemStack itemStack;

    public Food(String name,
                List<String> lore,
                String skinId,
                String skinValue,
                List<ItemStack> ingredient,
                List<Material> overlapType,
                int foodLevel,
                List<PotionEffect> potionList,
                double exp,
                boolean isFood,
                Material type,
                int specialType,
                boolean removeHarmful) {
        this.name = name;
        this.lore = lore;
        this.ingredient = ingredient;
        this.overlapType = overlapType;
        this.foodLevel = foodLevel;
        this.potionList = potionList;
        this.exp = exp;
        this.isFood = isFood;
        this.removeHarmful = removeHarmful;
        ItemStack foodItem;
        ItemMeta foodItemMeta;
        if (type == Material.SKULL_ITEM) {
            foodItem = Untill.getCustomSkull(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), skinId, skinValue);
        } else
            foodItem = new ItemStack(type, 1);
        if (type != Material.SKULL_ITEM)
            if (specialType >= 0)
                foodItem.setDurability((short) specialType);
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

    public List<Material> getOverlapType() {
        return overlapType;
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

    public boolean isRemoveHarmful() {
        return removeHarmful;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

}
