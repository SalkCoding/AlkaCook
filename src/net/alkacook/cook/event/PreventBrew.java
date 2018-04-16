package net.alkacook.cook.event;

import net.alkacook.untill.Constants;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;

public class PreventBrew implements Listener {

    @EventHandler
    public void onBrewing(BrewEvent event){//Already prevent completed but this is not time to apply it
        for(HumanEntity entity:event.getContents().getViewers()){
            entity.sendMessage(Constants.Prefix + ChatColor.RED + "포션 양조는 금지되어 있습니다.");
        }
        event.setCancelled(true);
    }

}
