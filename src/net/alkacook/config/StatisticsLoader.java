package net.alkacook.config;

import net.alkacook.main.Main;
import net.alkacook.rank.FoodStatistics;
import net.alkacook.untill.Constants;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class StatisticsLoader {

    public static void loadStatistics() throws IOException {
        HashMap<String, Integer> statistics = new HashMap<>();
        Path path = Main.getInstance().getDataFolder().toPath().resolve("Statistics");
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        File file = new File(path.toFile(), "Statistics.yml");
        if(!file.exists())
            file.createNewFile();
        LinmaluYamlConfiguration config = LinmaluYamlConfiguration.loadConfiguration(file);
        for (String root : config.getKeys(false)) {
            statistics.put(root, config.getInt(root));
        }
        FoodStatistics.setStatistics(statistics);
        Bukkit.getLogger().info(Constants.Console + "Statistics is loaded!");
    }

}
