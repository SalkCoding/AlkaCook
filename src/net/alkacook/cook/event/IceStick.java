package net.alkacook.cook.event;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class IceStick implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (handItem.getType() == Material.STICK) {
            Block block = event.getBlock();
            if (block.getType() == Material.ICE) {
                ItemStack drop = new ItemStack(block.getType());
                player.getWorld().dropItem(block.getLocation(), drop);
                block.setType(Material.AIR);
            }
        }
    }

}
