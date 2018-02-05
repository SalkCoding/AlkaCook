package net.alkacook.food;

import net.alkacook.untill.Constants;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class FoodEat implements Listener {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();//Consumed food
        Food food = FoodListManager.getCustomFood(item.getItemMeta().getDisplayName());
        Player player = event.getPlayer();
        if (food == null) {//If food is Not registered
            event.setCancelled(true);//Cancel the event
            player.sendMessage(Constants.Prefix + ChatColor.RED + "해당 음식은 섭취할 수 없습니다.");
        } else {//If it's registered
            int foodLevel = player.getFoodLevel() + food.getFoodLevel();
            player.setFoodLevel(foodLevel);//Set the player's food level
            List<PotionEffect> potionEffects;
            potionEffects = food.getPotionList();
            for (PotionEffect effect : potionEffects) {
                player.addPotionEffect(effect);//Add the effects to player
            }
        }
    }

}
