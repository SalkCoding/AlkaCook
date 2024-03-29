package net.alkacook.untill;

import net.alkacook.cook.CookGUI;
import net.alkacook.cook.CookGUIClick;
import net.alkacook.food.Food;
import net.alkacook.food.FoodListManager;
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
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Untill {

    public static int getMaxCraftAmount(Food resultFood, List<ItemStack> ingredients) {
        int maxAmount = 100;
        List<Material> overlapTypes = resultFood.getOverlapType();
        for (ItemStack resultIngredient : resultFood.getIngredient()) {
            for (ItemStack ingredient : ingredients) {
                if (ingredient.getType() == resultIngredient.getType() || (overlapTypes.contains(ingredient.getType()) && overlapTypes.contains(resultIngredient.getType()))) {
                    int amount = ingredient.getAmount() / resultIngredient.getAmount();
                    maxAmount = Math.min(amount, maxAmount);
                }
            }
        }
        return maxAmount;
    }

    public static Food CookIngredient(List<ItemStack> ingredients) {
        for (Map.Entry<String, Food> ele : FoodListManager.getFoodList().entrySet()) {
            Food food = ele.getValue();
            List<ItemStack> foodIngredient = food.getIngredient();
            List<Material> overlapTypes = food.getOverlapType();
            ArrayList<Material> alreadyCheck = new ArrayList<>();
            int count = 0;
            for (ItemStack ingredient : foodIngredient) {
                loreError:
                for (ItemStack item : ingredients) {//Check the ingredient to return a food
                    if ((ingredient.getType() == item.getType() || overlapTypes.contains(item.getType())) && ingredient.getAmount() <= item.getAmount() && !alreadyCheck.contains(item.getType())) {
                        if (!item.getI18NDisplayName().equals(ingredient.getI18NDisplayName()))
                            continue;
                        List<String> itemLore = item.getItemMeta().getLore();
                        List<String> ingredientLore = ingredient.getItemMeta().getLore();
                        if (itemLore != null) {
                            if (itemLore.size() != ingredientLore.size())
                                continue;
                            for (int i = 0; i < itemLore.size(); i++)
                                if (!itemLore.get(i).equals(ingredientLore.get(i))) {
                                    continue loreError;
                                }
                        }
                        //if (!Constants.NotCheckMaterialList.contains(item.getType()))
                        if (overlapTypes.contains(item.getType()))
                            alreadyCheck.addAll(overlapTypes);
                        if (item.getType() != Material.SKULL_ITEM)
                            alreadyCheck.add(item.getType());
                        count++;
                    }
                }
                if (count == foodIngredient.size() && ingredients.size() == count) return food;
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
            if (item.getType() == Material.AIR)
                player.sendMessage(Constants.Prefix + ChatColor.RED + "아이템을 손에들고 있어야합니다.");
            else
                player.sendMessage(Constants.Prefix + ChatColor.RED + "해당 아이템은 명령어로 모으기가 금지되어 있습니다.");
            return;
        }
        int amount = 0;
        Inventory inv = player.getInventory();
        for (ItemStack invItem : inv.getStorageContents()) {
            if (invItem == null)
                continue;
            if (invItem.isSimilar(item)) {
                inv.remove(invItem);
                amount += invItem.getAmount();
            }
        }
        PlayerInventory playerInv = null;
        if (inv instanceof PlayerInventory)
            playerInv = (PlayerInventory) inv;
        if (playerInv != null) {
            if(playerInv.getItemInOffHand().isSimilar(item)) {
                playerInv.setItemInOffHand(null);
                amount += 1;
            }
        }
        ItemStack result = item.clone();
        result.setAmount(amount);
        player.getInventory().addItem(result);
        player.sendMessage(Constants.Prefix + ChatColor.GREEN + "아이템이 한곳으로 모아졌습니다.");
    }

    public static void safeDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTitle().equalsIgnoreCase(Constants.InvName)) {
                InventoryView inv = player.getOpenInventory();
                copyItemFromInventory(player, inv);
                player.sendMessage(Constants.Prefix + ChatColor.YELLOW + "관리자가 플러그인을 리로드 하여서 GUI가 종료됩니다.");
                player.closeInventory();
            }
        }
        FoodListManager.getFoodList().clear();
        CookGUIClick.getMaxAmount().clear();
        CookGUIClick.getCorrectCook().clear();
        CookGUI.getCookingList().clear();
    }

    public static void copyItemFromInventory(Player player, InventoryView inv) {
        for (int i = 1; i < (Constants.GUIHeight - 1); i++) {
            for (int j = 1; j < 8; j++) {
                ItemStack item = inv.getItem((i * 9) + j);
                if (item == null) continue;
                player.getInventory().addItem(item);
            }
        }
    }

}
