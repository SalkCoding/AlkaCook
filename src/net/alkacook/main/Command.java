package net.alkacook.main;

import net.alkacook.config.FoodWriter;
import net.alkacook.rank.FoodStatistics;
import net.alkacook.rank.FoodStatisticsRank;
import net.alkacook.rank.IngredientRank;
import net.alkacook.cook.CookGUI;
import net.alkacook.rank.FoodRank;
import net.alkacook.untill.Constants;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("cook")) {
            if (args.length != 0) {//make test food file command
                if (commandSender.isOp()) {
                    if (args[0].equalsIgnoreCase("example")) {
                        try {
                            FoodWriter.writeTestFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        commandSender.sendMessage(Constants.Prefix + ChatColor.GREEN + "Test Food 파일 생성 완료!");
                    } else if (args[0].equalsIgnoreCase("rank")) {
                        if (args[1].equalsIgnoreCase("craft")) {
                            for (FoodStatisticsRank rank : FoodStatistics.getStatisticsRank()) {
                                commandSender.sendMessage(Constants.Prefix + rank.getName() + "가 서버내에서 총" + rank.getCount() + "번 조합됨");
                            }
                        } else if (args[1].equalsIgnoreCase("ingredient")) {
                            for (IngredientRank rank : FoodRank.getIngredientRank()) {
                                commandSender.sendMessage(Constants.Prefix + rank.getName() + "가 조합법에 총" + rank.getCount() + "회 사용됨");
                            }
                        }
                    }
                } else {
                    commandSender.sendMessage(Constants.Prefix + ChatColor.RED + "해당 명령어를 사용할 권한이 없습니다!");
                }
            } else if (args.length == 0) {//gui command
                Player player = null;
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(Constants.Console + ChatColor.RED + "This command only can be used by player!");
                    return true;
                }
                player = (Player) commandSender;
                CookGUI.openCookingGUI(player);
            }
            return true;
        }
        return false;
    }
}
