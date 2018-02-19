package net.alkacook.main;

import net.alkacook.food.FoodListManager;
import net.alkacook.untill.Constants;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("alkacook")) {
            if (commandSender.isOp()) {
                FoodListManager.loadFoodList();
                commandSender.sendMessage(Constants.Prefix + ChatColor.GREEN + "reload 완료!");
            } else {
                commandSender.sendMessage(Constants.Prefix + ChatColor.RED + "해당 명령어를 사용할 권한이 없습니다!");
            }
            return true;
        }
        return false;
    }

}
