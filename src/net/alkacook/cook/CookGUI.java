package net.alkacook.cook;

import net.alkacook.untill.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CookGUI {

    public static void openCookingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9 * Constants.GUIHeight, Constants.InvName);
        ItemStack nullButton = new ItemStack(Material.FURNACE);
        ItemMeta nullButtonMeta = nullButton.getItemMeta();
        nullButtonMeta.setDisplayName("");
        nullButton.setItemMeta(nullButtonMeta);
        ItemStack wall = new ItemStack(Material.STAINED_GLASS_PANE);
        wall.setDurability((short) 6);
        ItemMeta wallMeta = wall.getItemMeta();
        wallMeta.setDisplayName("");
        wall.setItemMeta(wallMeta);
        for (int i = 0; i < Constants.GUIHeight * 9; i++) {
            gui.setItem(i, wall);
        }
        for (int i = 1; i < (Constants.GUIHeight - 1); i++) {
            for (int j = 1; j < 8; j++) {
                gui.setItem(((i * 9) + j), null);
            }
        }
        gui.setItem(4,nullButton);
        player.openInventory(gui);
    }

}
