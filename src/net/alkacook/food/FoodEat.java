package net.alkacook.food;

import com.mysql.fabric.xmlrpc.base.Array;
import net.alkacook.main.Main;
import net.alkacook.untill.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class FoodEat implements Listener {

    protected static HashSet<Player> eatTimer = new HashSet<>();

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();//Consumed food
        Food food = FoodListManager.getCustomFood(item.getItemMeta().getDisplayName());
        Player player = event.getPlayer();
        if (food == null) {//If food is Not registered
            event.setCancelled(true);//Cancel the event
            player.sendMessage(Constants.Prefix + ChatColor.RED + "해당 음식은 섭취할 수 없습니다.");
        }
    }

    @EventHandler
    public void onCakeEat(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block.getType() == Material.CAKE_BLOCK) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Constants.Prefix + ChatColor.RED + "해당 음식은 섭취할 수 없습니다.");
            }
        }
    }

    @EventHandler
    public void onCustomFoodConsume(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item == null) return;
            Food food = FoodListManager.getCustomFood(item.getItemMeta().getDisplayName());
            if (food != null) {
                Player player = event.getPlayer();
                if (eatTimer.contains(player)) {
                    event.setCancelled(true);
                    return;
                }
                if (!food.isFood()) {
                    event.setCancelled(true);
                    return;
                }
                List<String> foodLore = food.getLore();
                List<String> itemLore = item.getItemMeta().getLore();
                for (int i = 0; i < foodLore.size(); i++) {//Check lore
                    if (!foodLore.equals(itemLore)) return;
                }
                int foodLevel = player.getFoodLevel() + food.getFoodLevel();
                player.setFoodLevel(foodLevel);//Set the player's food level
                List<PotionEffect> potionEffects;
                potionEffects = food.getPotionList();
                for (PotionEffect effect : potionEffects) {
                    if (effect != null)
                        player.addPotionEffect(effect);//Add the effects to player
                }
                item.setAmount(item.getAmount() - 1);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "giveexp " + player.getName() + " " + food.getExp());
                event.setCancelled(true);
                eatTimer.add(player);
                EatCoolTimer timer = new EatCoolTimer(player);
                timer.setTask(Bukkit.getScheduler().runTaskTimer(Main.getInstance(), timer, getMostLongDuration(potionEffects), 0));
            }
        }
    }

    private int getMostLongDuration(List<PotionEffect> potionEffects) {
        ArrayList<Integer> result = new ArrayList<>();
        for (PotionEffect ele : potionEffects) {
            result.add(ele.getDuration());
        }
        Collections.sort(result);
        return result.get(0);
    }

}

class EatCoolTimer implements Runnable {
    private BukkitTask task;
    private Player target;

    public void setTask(BukkitTask task) {
        this.task = task;
    }

    public EatCoolTimer(Player target) {
        this.target = target;
    }

    @Override
    public void run() {
        FoodEat.eatTimer.remove(target);
        task.cancel();
    }
}