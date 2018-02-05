package net.alkacook.food;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class Food {

    private String name;
    private List<String> lore;
    private String iconName;
    private boolean glow;
    private List<ItemStack> ingredient;
    private int foodLevel;
    private List<PotionEffect> potionList;

    public Food(String name,
                List<String> lore,
                String iconName,
                boolean glow,
                List<ItemStack> ingredient,
                int foodLevel,
                List<PotionEffect> potionList) {
        this.name = name;
        this.lore = lore;
        this.iconName = iconName;
        this.glow = glow;
        this.ingredient = ingredient;
        this.foodLevel = foodLevel;
        this.potionList = potionList;
    }

    public String getDisplayName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getIconName() {
        return iconName;
    }

    public boolean getGlow() {
        return glow;
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

}
