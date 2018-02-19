package net.alkacook.untill;

import net.alkacook.food.Food;
import net.alkacook.food.FoodListManager;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.lang.reflect.Field;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.inventory.meta.SkullMeta;
import org.jline.utils.Log;

public class Untill {

    /*public static ItemStack addGlow(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }*/

    public static Food CookIngredient(List<ItemStack> ingredients) {
        for (Map.Entry<String, Food> ele : FoodListManager.getFoodList().entrySet()) {
            Food food = ele.getValue();
            List<ItemStack> foodIngredient = food.getIngredient();
            ArrayList<Material> alreadyCheck = new ArrayList<>();
            int count = 0;
            for (ItemStack ingredient : foodIngredient) {
                for (ItemStack item : ingredients) {//Check the ingredient to return a food
                    if (ingredient.getType() == item.getType() && ingredient.getAmount() <= item.getAmount() && !alreadyCheck.contains(item.getType())) {
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

    public static ItemStack getCustomSkull(ItemStack item,String id,String skinValue) {
        net.minecraft.server.v1_12_R1.ItemStack head = CraftItemStack.asNMSCopy(item);
        NBTTagCompound root = new NBTTagCompound();
        NBTTagCompound skullOwner = root.getCompound("SkullOwner");
        skullOwner.setString("Id",id);
        NBTTagCompound properties = skullOwner.getCompound("Properties");
        NBTTagCompound textures = new NBTTagCompound();
        textures.setString("Value",skinValue);
        NBTTagList list = new NBTTagList();
        list.add(textures);
        properties.set("textures",list);
        skullOwner.set("Properties",properties);
        root.set("SkullOwner",skullOwner);
        head.setTag(root);
        return CraftItemStack.asCraftMirror(head);
    }

}
