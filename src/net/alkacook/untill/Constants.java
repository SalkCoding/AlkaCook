package net.alkacook.untill;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;

public class Constants {

    private Constants() {
    }

    public static String Console = "[AlkaCook] ";
    public static String Prefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "!" + ChatColor.GRAY + "] " + ChatColor.RESET;
    public static String InvName = ChatColor.translateAlternateColorCodes('&', "&7-= &cA&6l&ek&aa&dCook &7=-");
    public static int GUIHeight = 5;
    public static ArrayList<Material> NotAllowCraftList = new ArrayList<>();
    public static ArrayList<Material> AllowItemList = new ArrayList<>();

    static{
        NotAllowCraftList.add(Material.CAKE);
        NotAllowCraftList.add(Material.COOKIE);
        NotAllowCraftList.add(Material.BREAD);
        NotAllowCraftList.add(Material.BEETROOT_SOUP);
        NotAllowCraftList.add(Material.MUSHROOM_SOUP);
        NotAllowCraftList.add(Material.GOLDEN_APPLE);
        NotAllowCraftList.add(Material.GOLDEN_CARROT);
        NotAllowCraftList.add(Material.PUMPKIN_PIE);
        NotAllowCraftList.add(Material.RABBIT_STEW);

        //AllowItemList.add(Material.LAVA_BUCKET);
        //AllowItemList.add(Material.WATER_BUCKET);
        AllowItemList.add(Material.MILK_BUCKET);
        AllowItemList.add(Material.RABBIT_STEW);
        AllowItemList.add(Material.MUSHROOM_SOUP);
        AllowItemList.add(Material.BEETROOT_SOUP);
        AllowItemList.add(Material.CAKE);
        //AllowItemList.add(Material.TOTEM);
        AllowItemList.add(Material.POTION);
    }

}
