package net.alkacook.cook;

import net.alkacook.food.Food;
import net.alkacook.main.Main;
import net.alkacook.rank.FoodStatistics;
import net.alkacook.untill.Constants;
import net.alkacook.untill.Untill;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CookGUIClick implements Listener {

    private HashMap<String, Food> correctCook = new HashMap<>();

    @EventHandler
    public void onNotAllowClick(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv != null) {//If player click a wrong gui,return pass the event
            String invName = inv.getName();
            if (invName.equals(Constants.InvName)) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null) {//If player click a blank,pass the event
                    Material itemType = clickedItem.getType();
                    if (itemType == Material.STAINED_GLASS_PANE || itemType == Material.FURNACE) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCustomFoodClick(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv != null) {//If player click a wrong gui,return pass the event
            String invName = inv.getName();
            if (invName.equals(Constants.InvName)) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && event.getRawSlot() == 4) {//If player click a blank,pass the event
                    String name = event.getWhoClicked().getName();
                    Food targetFood = correctCook.get(name);
                    if(targetFood == null) return;
                    List<ItemStack> ingredient = targetFood.getIngredient();
                    ArrayList<Material> alreadyCheck = new ArrayList<>();
                    for (int i = 1; i < (Constants.GUIHeight - 1); i++) {
                        for (int j = 1; j < 8; j++) {
                            ItemStack item = inv.getItem((i * 9) + j);
                            if (item != null) {
                                for (ItemStack ele : ingredient) {
                                    if (ele.getType() == item.getType() && !alreadyCheck.contains(item.getType())) {
                                        alreadyCheck.add(item.getType());
                                        int amount = item.getAmount() - ele.getAmount();
                                        if (amount <= 0)
                                            inv.removeItem(item);
                                        else
                                            item.setAmount(amount);
                                    }
                                }
                            }
                        }
                    }
                    ItemStack nullButton = new ItemStack(Material.FURNACE);
                    ItemMeta nullButtonMeta = nullButton.getItemMeta();
                    nullButtonMeta.setDisplayName("");
                    nullButton.setItemMeta(nullButtonMeta);
                    inv.setItem(4,nullButton);
                    event.getWhoClicked().getInventory().addItem(correctCook.get(name).getItemStack());
                    FoodStatistics.addStatistics(correctCook.get(name).getDisplayName());
                    correctCook.remove(name);
                    event.setCancelled(true);
                }
            }
        }
    }

    private HashMap<String, BukkitTask> cookingList = new HashMap<>();

    @EventHandler
    public void onCookingStart(InventoryOpenEvent event) {
        if(cookingList.containsKey(event.getPlayer().getName()))
            return;
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            Inventory inv = event.getInventory();
            if (inv != null) {//If player click a wrong gui,return pass the event
                String invName = inv.getName();
                if (invName.equals(Constants.InvName)) {
                    ArrayList<ItemStack> ingredient = new ArrayList<>();
                    for (int i = 1; i < (Constants.GUIHeight - 1); i++) {
                        for (int j = 1; j < 8; j++) {
                            ItemStack item = inv.getItem((i * 9) + j);
                            if (item != null)
                                ingredient.add(item);
                        }
                        Food resultFood = Untill.CookIngredient(ingredient);
                        if (resultFood != null) {//Add item in slot where have to locate a food
                            inv.setItem(4,resultFood.getItemStack());
                            correctCook.put(event.getPlayer().getName(), resultFood);
                        } else {
                            ItemStack nullButton = new ItemStack(Material.FURNACE);
                            ItemMeta nullButtonMeta = nullButton.getItemMeta();
                            nullButtonMeta.setDisplayName("");
                            nullButton.setItemMeta(nullButtonMeta);
                            inv.setItem(4, nullButton);
                            correctCook.remove(event.getPlayer().getName());
                        }
                    }
                }
            }
        }, 5, 5);
        cookingList.put(event.getPlayer().getName(), task);
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
                    player.getInventory().addItem(inv.getItem((i * 9) + j));
                }
            }
            cookingList.get(event.getPlayer().getName()).cancel();
            cookingList.remove(event.getPlayer().getName());
        }
    }


}