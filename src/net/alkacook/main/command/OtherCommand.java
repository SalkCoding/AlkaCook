package net.alkacook.main.command;

import net.alkacook.config.StatisticsWriter;
import net.alkacook.food.FoodListManager;
import net.alkacook.rank.FoodStatistics;
import net.alkacook.untill.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.HashSet;

public class OtherCommand implements CommandExecutor {

    private HashSet<CommandSender> check = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        if (commandSender.isOp()) {
            if (args.length == 0) {
                FoodListManager.loadFoodList();
                commandSender.sendMessage(Constants.Prefix + ChatColor.GREEN + "reload 완료!");
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reset")) {
                    if (!check.contains(commandSender)) {
                        check.add(commandSender);
                        commandSender.sendMessage(Constants.Prefix + ChatColor.RED + "서버의 전체 요리 통계를 삭제하려는 경우 /AlkaCook confirm를 입력하세요.");
                    }
                } else if (args[0].equalsIgnoreCase("confirm")) {
                    if (check.contains(commandSender)) {
                        StatisticsWriter.removeStatistics();
                        FoodStatistics.setStatistics(new HashMap<>());
                        commandSender.sendMessage(Constants.Prefix + ChatColor.GREEN + "통계 삭제가 완료되었습니다.");
                        Bukkit.getLogger().warning(Constants.Console + commandSender.getName() + "님이 서버의 전체 요리 통계를 삭제하셨습니다.");
                        check.remove(commandSender);
                    }
                }
            }
        } else {
            commandSender.sendMessage(Constants.Prefix + ChatColor.RED + "해당 명령어를 사용할 권한이 없습니다!");
        }
        return true;
    }

}
