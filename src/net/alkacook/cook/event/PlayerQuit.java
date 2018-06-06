package net.alkacook.cook.event;

import net.alkacook.cook.CookGUI;
import net.alkacook.cook.CookGUIClick;
import net.alkacook.food.Food;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String name = event.getPlayer().getName();
        HashMap<Integer, BukkitTask> cookingList = CookGUI.getCookingList();
        HashMap<String, Integer> maxAmount = CookGUIClick.getMaxAmount();
        HashMap<String, Food> correctCook = CookGUIClick.getCorrectCook();
        if (cookingList.containsKey(event.getPlayer().getEntityId())) {
            int id = event.getPlayer().getEntityId();
            cookingList.get(id).cancel();
            cookingList.remove(id);
        }
        maxAmount.remove(name);
        correctCook.remove(name);
    }

}
