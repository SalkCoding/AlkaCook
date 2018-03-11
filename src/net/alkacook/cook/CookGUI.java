package net.alkacook.cook;

import net.alkacook.food.Food;
import net.alkacook.main.Main;
import net.alkacook.untill.Constants;
import net.alkacook.untill.Untill;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class CookGUI implements Listener {

    protected static HashMap<Integer, BukkitTask> cookingList = new HashMap<>();

    public static HashMap<Integer, BukkitTask> getCookingList() {
        return cookingList;
    }

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
        gui.setItem(4, nullButton);
        player.openInventory(gui);
        if (!cookingList.containsKey(player.getEntityId())) {
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new CraftIngredients(player.getName(), gui), 5, 0);
            cookingList.put(player.getEntityId(), task);
        }
    }

    @EventHandler
    public void onCookingEnd(InventoryCloseEvent event) {
        if (event.getInventory().getName().equals(Constants.InvName)) {
            Inventory inv = event.getInventory();
            if (!(event.getPlayer() instanceof Player))
                return;
            Player player = (Player) event.getPlayer();
            for (int i = 1; i < (Constants.GUIHeight - 1); i++) {
                for (int j = 1; j < 8; j++) {
                    ItemStack item = inv.getItem((i * 9) + j);
                    if (item == null) continue;
                    player.getInventory().addItem(item);
                }
            }
            BukkitTask task = cookingList.get(event.getPlayer().getEntityId());
            if (task != null)
                task.cancel();
            cookingList.remove(event.getPlayer().getEntityId());
        }
    }

}

class CraftIngredients implements Runnable {

    private String name;
    private Inventory inv;

    public CraftIngredients(String name, Inventory inv) {
        this.name = name;
        this.inv = inv;
    }


    @Override
    public void run() {
        ArrayList<ItemStack> ingredient = new ArrayList<>();
        for (int i = 1; i < (Constants.GUIHeight - 1); i++) {
            for (int j = 1; j < 8; j++) {
                ItemStack item = inv.getItem((i * 9) + j);
                if (item != null)
                    ingredient.add(item);
            }
            Food resultFood = Untill.CookIngredient(ingredient);
            if (resultFood != null) {//Add item in slot where have to locate a food
                ItemStack foodItem = resultFood.getItemStack().clone();
                int amount = Untill.getMaxCraftAmount(resultFood, ingredient);
                foodItem.setAmount(amount);
                inv.setItem(4, foodItem);
                CookGUIClick.correctCook.put(name, resultFood);
                CookGUIClick.maxAmount.put(name, amount);
            } else {
                ItemStack nullButton = new ItemStack(Material.FURNACE);
                ItemMeta nullButtonMeta = nullButton.getItemMeta();
                nullButtonMeta.setDisplayName("");
                nullButton.setItemMeta(nullButtonMeta);
                inv.setItem(4, nullButton);
                CookGUIClick.correctCook.remove(name);
                CookGUIClick.maxAmount.remove(name);
            }
        }
    }
}
