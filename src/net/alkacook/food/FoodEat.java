package net.alkacook.food;

import net.alkacook.main.Main;
import net.alkacook.untill.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class FoodEat implements Listener {

    protected static HashSet<Player> eatTimer = new HashSet<>();
    protected static HashSet<Player> eatMotion = new HashSet<>();

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

    HashSet<Player> milkEvent = new HashSet<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMilkToEntity(PlayerInteractEntityEvent event) {
        ItemStack mainItem = event.getPlayer().getInventory().getItemInMainHand();
        ItemStack offItem = event.getPlayer().getInventory().getItemInOffHand();
        if (mainItem.getType() == Material.MILK_BUCKET || offItem.getType() == Material.MILK_BUCKET) {
            milkEvent.add(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCakeOrMilkEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack item = event.getItem();
            if (item == null) return;
            if (item.getType() == Material.MILK_BUCKET) {
                Player player = event.getPlayer();
                if (milkEvent.contains(player)) {
                    milkEvent.remove(player);
                    return;
                }
                event.setCancelled(true);
                int amount = item.getAmount() - 1;
                if (amount <= 0) {
                    item.setType(Material.BUCKET);
                } else {
                    item.setAmount(amount);
                    event.getPlayer().getInventory().addItem(new ItemStack(Material.BUCKET));
                }
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 50, 50);
                return;
            }
            Block block = event.getClickedBlock();
            if (block == null) return;
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
                event.setCancelled(true);
                Player player = event.getPlayer();
                List<String> worldNames = Main.getLimitWorldNames();
                if (worldNames != null) {
                    if (worldNames.contains(player.getWorld().getName())) {
                        player.sendMessage(Constants.Prefix + ChatColor.RED + "현재 월드에서는 음식을 섭취할 수 없습니다.");
                        return;
                    }
                }
                if (!food.getPotionList().contains(null)) {
                    if (eatTimer.contains(player) || eatMotion.contains(player))
                        return;
                } else {
                    if (eatMotion.contains(player))
                        return;
                }
                if (!food.isFood())
                    return;
                List<String> foodLore = food.getLore();
                List<String> itemLore = item.getItemMeta().getLore();
                for (int i = 0; i < foodLore.size(); i++) {//Check lore
                    if (!foodLore.equals(itemLore)) return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PARROT_EAT, 50, 50);
                int foodLevel = player.getFoodLevel() + food.getFoodLevel();
                player.setFoodLevel(foodLevel);//Set the player's food level
                List<PotionEffect> potionEffects;
                potionEffects = food.getPotionList();
                if (food.isRemoveHarmful()) {
                    for (PotionEffectType effectType : Constants.HarmfulPotionEffect) {
                        player.removePotionEffect(effectType);
                    }
                    player.setFireTicks(0);
                }
                for (PotionEffect effect : potionEffects) {
                    if (effect != null)
                        player.addPotionEffect(effect);//Add the effects to player
                }
                item.setAmount(item.getAmount() - 1);
                if (!food.getPotionList().contains(null)) {
                    eatTimer.add(player);
                    int wait = FoodEat.getMostLongDuration(potionEffects);
                    EatCoolTimer timer = new EatCoolTimer(player);
                    timer.setTask(Bukkit.getScheduler().runTaskTimer(Main.getInstance(), timer, wait, 0));
                }
                eatMotion.add(player);
                EatMotion motion = new EatMotion(player, food);
                motion.setTask(Bukkit.getScheduler().runTaskTimer(Main.getInstance(), motion, 20, 0));
            }
        }
    }

    private static int getMostLongDuration(List<PotionEffect> potionEffects) {
        if (potionEffects.contains(null))
            return 0;
        ArrayList<Integer> result = new ArrayList<>();
        for (PotionEffect ele : potionEffects) {
            result.add(ele.getDuration());
        }
        Collections.sort(result);
        return result.get(result.size() - 1);
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

class EatMotion implements Runnable {
    private BukkitTask task;
    private Player player;
    private Food food;

    public void setTask(BukkitTask task) {
        this.task = task;
    }

    public EatMotion(Player player, Food food) {
        this.player = player;
        this.food = food;
    }

    @Override
    public void run() {
        player.sendMessage(Constants.Prefix + ChatColor.GOLD
                + food.getDisplayName() + ChatColor.YELLOW + "을/를 섭취하였습니다.");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 50, 50);
        FoodEat.eatMotion.remove(player);
        task.cancel();
    }
}