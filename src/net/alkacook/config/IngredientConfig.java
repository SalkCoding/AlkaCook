package net.alkacook.config;

import org.bukkit.Material;

import java.util.List;

public class IngredientConfig {

    private Material type;
    private String displayName;
    private List<String> lore;
    private int amount;
    private String name;
    private String specialType;

    public IngredientConfig(Material type,
                            String name,
                            String displayName,
                            List<String> lore,
                            int amount,
                            String specialType
    ) {
        this.type = type;
        this.displayName = displayName;
        this.lore = lore;
        this.amount = amount;
        this.name = name;
        this.specialType = specialType;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getType() {
        return type;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getAmount() {
        return amount;
    }

    public int getSpecialType() {
        if (specialType == null || specialType.equalsIgnoreCase("0"))
            return 0;
        else
            return Integer.parseInt(specialType) + 1;
    }
}
