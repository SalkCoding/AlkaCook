package net.alkacook.cook;

import net.alkacook.untill.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CookGUI {

    public static void openGUI(Player player) {
        int height = 5;
        Inventory gui = Bukkit.createInventory(null, 9 * height, Constants.InvName);
        ItemStack wall = new ItemStack(Material.STAINED_GLASS_PANE);
        wall.setDurability((short) 6);
        ItemMeta wallMeta = wall.getItemMeta();
        wallMeta.setDisplayName("");
        wall.setItemMeta(wallMeta);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < 9; j++) {
                if(i==0 && j == 4){//Empty the slot where the completed work will be placed.
                    continue;
                }
                if (j / 8 == 0 || j / 8 == 1 || i == height) {//Surround the gui by pink stained glass pane
                    gui.setItem(i*j,wall);
                }
            }
        }
    }

}
