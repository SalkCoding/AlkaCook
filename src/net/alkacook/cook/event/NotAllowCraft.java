package net.alkacook.cook.event;

import net.alkacook.untill.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class NotAllowCraft implements Listener{

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Material result = event.getRecipe().getResult().getType();
        if(Constants.NotAllowCraftList.contains(result)){
            HumanEntity entity = (Player) event.getWhoClicked();
            entity.sendMessage(Constants.Prefix + ChatColor.RED + "해당 음식은 제작할 수 없습니다.");
            event.setCancelled(true);
        }
    }

}
