package net.alkacook.untill;

import io.netty.util.Constant;
import net.alkacook.config.IngredientConfig;
import net.alkacook.cook.CookGUI;
import net.alkacook.cook.CookGUIClick;
import net.alkacook.food.Food;
import net.alkacook.food.FoodListManager;
import net.alkacook.rank.IngredientRank;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jline.utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Untill {

    public static int getMaxCraftAmount(Food resultFood, List<ItemStack> ingredients) {
        int maxAmount = 100;
        for (ItemStack resultIngredient : resultFood.getIngredient()) {
            for (ItemStack ingredient : ingredients) {
                if (ingredient.getType() == resultIngredient.getType() || resultFood.getOverlapType().contains(ingredient.getType())) {
                    int amount = ingredient.getAmount() / resultIngredient.getAmount();
                    maxAmount = Math.min(amount, maxAmount);
                }
            }
        }
        return maxAmount;
    }

    public static Food CookIngredient(List<ItemStack> ingredients) {
        /*for(ItemStack item:ingredients){

        }*/
        for (Map.Entry<String, Food> ele : FoodListManager.getFoodList().entrySet()) {
            Food food = ele.getValue();
            List<ItemStack> foodIngredient = food.getIngredient();
            ArrayList<Material> alreadyCheck = new ArrayList<>();
            int count = 0;
            for (ItemStack ingredient : foodIngredient) {
                for (ItemStack item : ingredients) {//Check the ingredient to return a food
                    List<String> itemLore = item.getItemMeta().getLore();
                    List<String> ingredientLore = ingredient.getItemMeta().getLore();
                    if ((ingredient.getType() == item.getType() || food.getOverlapType().contains(item.getType())) && ingredient.getAmount() <= item.getAmount() && !alreadyCheck.contains(item.getType())) {
                        if (!item.getI18NDisplayName().equals(ingredient.getI18NDisplayName()))
                            continue;
                        if (itemLore != null) {
                            if (itemLore.size() != ingredientLore.size())
                                continue;
                            for (int i = 0; i < itemLore.size(); i++) {
                                if (!itemLore.get(i).equals(ingredientLore.get(i)))
                                    continue;
                            }
                        }
                        //if (!Constants.NotCheckMaterialList.contains(item.getType()))
                        alreadyCheck.add(item.getType());
                        count++;
                    }
                }
            }
            if (count == foodIngredient.size() && ingredients.size() == count) {
                return food;
            }
        }
        return null;//If the search fails on the list, returns null.
    }

    public static ItemStack getCustomSkull(ItemStack item, String id, String skinValue) {
        net.minecraft.server.v1_12_R1.ItemStack head = CraftItemStack.asNMSCopy(item);
        NBTTagCompound root = new NBTTagCompound();
        NBTTagCompound skullOwner = root.getCompound("SkullOwner");
        skullOwner.setString("Id", id);
        NBTTagCompound properties = skullOwner.getCompound("Properties");
        NBTTagCompound textures = new NBTTagCompound();
        textures.setString("Value", skinValue);
        NBTTagList list = new NBTTagList();
        list.add(textures);
        properties.set("textures", list);
        skullOwner.set("Properties", properties);
        root.set("SkullOwner", skullOwner);
        head.setTag(root);
        return CraftItemStack.asCraftMirror(head);
    }

    public static void getGroupItemAtOnce(ItemStack item, Player player) {
        if (!Constants.AllowItemList.contains(item.getType())) {
            if(item.getType() == Material.AIR)
                player.sendMessage(Constants.Prefix + ChatColor.RED + "아이템을 손에들고 있어야합니다.");
            else
                player.sendMessage(Constants.Prefix + ChatColor.RED + "해당 아이템은 명령어로 모으기가 금지되어 있습니다.");
            return;
        }
        int amount = 0;
        Inventory inv = player.getInventory();
        for (ItemStack invItem : inv.getContents()) {
            if (invItem == null)
                continue;
            if (Constants.AllowItemList.contains(invItem.getType()) && invItem.getType() == item.getType()) {
                inv.remove(invItem);
                amount += invItem.getAmount();
            }
        }
        ItemStack result = item.clone();
        result.setAmount(amount);
        player.getInventory().addItem(result);
        player.sendMessage(Constants.Prefix + ChatColor.GREEN + "아이템이 한곳으로 모아졌습니다.");
    }

    public static void safeDisable(){
        for(Player player: Bukkit.getOnlinePlayers()){
            if(player.getOpenInventory().getTitle().equalsIgnoreCase(Constants.InvName)) {
                InventoryView inv = player.getOpenInventory();
                for (int i = 1; i < (Constants.GUIHeight - 1); i++) {
                    for (int j = 1; j < 8; j++) {
                        ItemStack item = inv.getItem((i * 9) + j);
                        if (item == null) continue;
                        player.getInventory().addItem(item);
                    }
                }
                player.sendMessage(Constants.Prefix + ChatColor.YELLOW + "관리자가 플러그인을 리로드 하여서 GUI가 종료됩니다.");
                player.closeInventory();
            }
        }
        FoodListManager.getFoodList().clear();
        CookGUIClick.getMaxAmount().clear();
        CookGUIClick.getCorrectCook().clear();
        CookGUI.getCookingList().clear();
    }

}
