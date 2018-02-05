package net.alkacook.food;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Food {

    private String name;
    private String iconName;
    private boolean glow;
    private ItemStack[] ingredient;
    private int foodLevel;
    private PotionEffect[] potionList;

    public Food(String name,
                String iconName,
                boolean glow,
                ItemStack[] ingredient,
                int foodLevel,
                PotionEffect[] potionList) {
        this.name = name;
        this.iconName = iconName;
        this.glow = glow;
        this.ingredient = ingredient;
        this.foodLevel = foodLevel;
        this.potionList = potionList;
    }

    public String getDisplayName() {
        return name;
    }

    public String getIconName() {
        return iconName;
    }

    public boolean getGlow() {
        return glow;
    }

    public ItemStack[] getIngredient() {
        return ingredient;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public PotionEffect[] getPotionList() {
        return potionList;
    }

}
