package net.alkacook.main.command;

import net.alkacook.config.FoodWriter;
import net.alkacook.food.Food;
import net.alkacook.food.FoodListManager;
import net.alkacook.rank.FoodStatistics;
import net.alkacook.rank.FoodStatisticsRank;
import net.alkacook.rank.IngredientRank;
import net.alkacook.cook.CookGUI;
import net.alkacook.rank.FoodRank;
import net.alkacook.untill.Constants;
import net.alkacook.untill.Untill;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length != 0) {//make test food file command
            if (args[0].equalsIgnoreCase("collect")) {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(Constants.Console + "You can't use this command with console ");
                    return true;
                }
                Player player = (Player) commandSender;
                Untill.getGroupItemAtOnce(player.getInventory().getItemInMainHand(), player);
                return true;
            } else if (args[0].equalsIgnoreCase("example")) {
                if (!commandSender.isOp()) {
                    printCommand(commandSender);
                    return true;
                }
                try {
                    FoodWriter.writeTestFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                commandSender.sendMessage(Constants.Prefix + ChatColor.GREEN + "Test Food 파일 생성 완료!");
            } else if (args[0].equalsIgnoreCase("rank")) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("craft")) {
                        for (FoodStatisticsRank rank : FoodStatistics.getStatisticsRank()) {
                            commandSender.sendMessage(Constants.Prefix + rank.getName() + "이/가 서버내에서 총" + rank.getCount() + "번 조합됨");
                        }
                        return true;
                    } else if (args[1].equalsIgnoreCase("ingredient")) {
                        for (IngredientRank rank : FoodRank.getIngredientRank()) {
                            commandSender.sendMessage(Constants.Prefix + rank.getName() + "이/가 조합법에 총" + rank.getCount() + "회 사용됨");
                        }
                        return true;
                    } else {
                        printCommand(commandSender);
                        return true;
                    }
                } else {
                    printCommand(commandSender);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("type")) {
                if (!commandSender.isOp()) {
                    printCommand(commandSender);
                    return true;
                }
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(Constants.Console + "You can't use this command with console ");
                    return true;
                }
                Player player = (Player) commandSender;
                ItemStack item = player.getInventory().getItemInMainHand();
                player.sendMessage(Constants.Prefix + ChatColor.GREEN + "아이템 타입 : " + ChatColor.WHITE + item.getType() + ChatColor.GREEN + " 스페셜 타입(0일경우 무시) : " + ChatColor.WHITE + item.getDurability());
                return true;
            } else if (args[0].equalsIgnoreCase("give")) {
                if (!commandSender.isOp() || args.length != 4) {
                    printCommand(commandSender);
                    return true;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    commandSender.sendMessage(Constants.Prefix + ChatColor.YELLOW + "해당 유저가 존재하지 않습니다!");
                    return true;
                }
                Food food = FoodListManager.getCustomFood(args[2]);
                if (food == null) {
                    commandSender.sendMessage(Constants.Prefix + ChatColor.YELLOW + "해당 음식이 존재하지 않습니다!");
                    return true;
                }
                int amount;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException exception) {
                    commandSender.sendMessage(Constants.Prefix + ChatColor.YELLOW + "잘못된 수량입니다!");
                    return true;
                }
                Inventory inv = player.getInventory();
                for (int i = 0; i < amount; i++) {
                    inv.addItem(food.getItemStack());
                }
                commandSender.sendMessage(Constants.Prefix + ChatColor.GRAY + player.getName() + ChatColor.RESET + "에게 " + food.getDisplayName() + "을/를 " + amount
                        + " 만큼 지급하였습니다.");
                player.sendMessage(Constants.Prefix + "관리자 " + ChatColor.GRAY + commandSender.getName() + ChatColor.RESET + "이/가 당신에게 " + food.getDisplayName() + "을/를 " + amount
                        + " 만큼 지급하였습니다.");
                return true;
            } else {
                printCommand(commandSender);
                return true;
            }
        } else {//gui command
            Player player;
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Constants.Console + ChatColor.RED + "This command only can be used by player!");
                return true;
            }
            player = (Player) commandSender;
            CookGUI.openCookingGUI(player);
            return true;
        }
        return true;
    }

    private void printCommand(CommandSender sender) {
        if (sender.isOp()) {
            sender.sendMessage(Constants.Prefix + "[관리자 명령어]");
            sender.sendMessage(Constants.Prefix + "/AlkaCook : reload 명령어");
            sender.sendMessage(Constants.Prefix + "/Cook example : test파일 생성");
            sender.sendMessage(Constants.Prefix + "/Cook rank ingredient : 음식에 들어가는 재료 통계를 보여줍니다.");
            sender.sendMessage(Constants.Prefix + "/Cook rank craft : 음식들이 서버에서 얼마나 조합됬는지 보여줍니다.");
            sender.sendMessage(Constants.Prefix + "/Cook : 요리 GUI 명령어입니다.");
            sender.sendMessage(Constants.Prefix + "/Cook collect : 손에 들고있는 아이템을 인벤토리내에서 한 곳으로 모아줍니다.");
            sender.sendMessage(Constants.Prefix + "/Cook give [name] [food] [amount] : [name]이라는 이름을 가진 유저에게 [food]라는 음식을 [amount]만큼 지급합니다.");
        } else {
            sender.sendMessage(Constants.Prefix + "[유저 명령어]");
            sender.sendMessage(Constants.Prefix + "/Cook : 요리 GUI 명령어입니다.");
            sender.sendMessage(Constants.Prefix + "/Cook collect : 손에 들고있는 아이템을 인벤토리내에서 한 곳으로 모아줍니다.");
        }
    }

}
