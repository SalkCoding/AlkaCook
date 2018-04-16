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

    protected static HashMap<String, Food> correctCook = new HashMap<>();
    protected static HashMap<String, Integer> maxAmount = new HashMap<>();

    public static HashMap<String, Food> getCorrectCook() {
        return correctCook;
    }

    public static HashMap<String, Integer> getMaxAmount() {
        return maxAmount;
    }

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
    public void onCustomFoodCraft(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv != null) {//If player click a wrong gui,return pass the event
            String invName = inv.getName();
            if (invName.equals(Constants.InvName)) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && event.getRawSlot() == 4) {//If player click a blank,pass the event
                    String name = event.getWhoClicked().getName();
                    Food targetFood = correctCook.get(name);
                    if (targetFood == null) return;
                    List<ItemStack> ingredient = targetFood.getIngredient();
                    ArrayList<Material> alreadyCheck = new ArrayList<>();
                    for (int i = 1; i < (Constants.GUIHeight - 1); i++) {
                        for (int j = 1; j < 8; j++) {
                            ItemStack item = inv.getItem((i * 9) + j);
                            if (item != null) {
                                for (ItemStack ele : ingredient) {
                                    if ((ele.getType() == item.getType() && !alreadyCheck.contains(item.getType())) || targetFood.getOverlapType().contains(item.getType())) {
                                        alreadyCheck.add(item.getType());
                                        int amount;
                                        if (!event.isShiftClick())
                                            amount = item.getAmount() - ele.getAmount();
                                        else
                                            amount = item.getAmount() - (maxAmount.get(name) * ele.getAmount());
                                        if (amount <= 0)
                                            inv.removeItem(item);
                                        else
                                            item.setAmount(amount);
                                        Player player = (Player) event.getWhoClicked();
                                        switch (item.getType()) {
                                            case MILK_BUCKET:
                                                if (!event.isShiftClick())
                                                    player.getInventory().addItem(new ItemStack(Material.BUCKET, ele.getAmount()));
                                                else
                                                    player.getInventory().addItem(new ItemStack(Material.BUCKET, (maxAmount.get(name) * ele.getAmount())));
                                                break;
                                            case POTION:
                                                if (!event.isShiftClick())
                                                    player.getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE, ele.getAmount()));
                                                else
                                                    player.getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE, (maxAmount.get(name) * ele.getAmount())));
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ItemStack nullButton = new ItemStack(Material.FURNACE);
                    ItemMeta nullButtonMeta = nullButton.getItemMeta();
                    nullButtonMeta.setDisplayName("");
                    nullButton.setItemMeta(nullButtonMeta);
                    inv.setItem(4, nullButton);
                    ItemStack foodItem = correctCook.get(name).getItemStack().clone();
                    Integer amount = maxAmount.get(name);
                    if (event.isShiftClick()) {
                        if (amount != null)
                            foodItem.setAmount(amount);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "giveexp " + name + " " + targetFood.getExp() * amount);
                        FoodStatistics.addStatistics(correctCook.get(name).getDisplayName(), amount);
                    } else {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "giveexp " + name + " " + targetFood.getExp());
                        FoodStatistics.addStatistics(correctCook.get(name).getDisplayName(), 1);
                    }
                    event.getWhoClicked().getInventory().addItem(foodItem);
                    correctCook.remove(name);
                    maxAmount.remove(name);
                    event.setCancelled(true);
                }
            }
        }
    }

}