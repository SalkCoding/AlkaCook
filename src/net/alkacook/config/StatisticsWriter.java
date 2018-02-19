package net.alkacook.config;

import net.alkacook.main.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class StatisticsWriter {

    public static void saveStatistics(HashMap<String, Integer> statistics) throws IOException {
        Path path = Main.getInstance().getDataFolder().toPath().resolve("Statistics");
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        File file = new File(path.toFile(), "Statistics.yml");
        if(!file.exists())
            file.createNewFile();
        LinmaluYamlConfiguration config = LinmaluYamlConfiguration.loadConfiguration(file);
        for (Map.Entry ele : statistics.entrySet()) {
            config.set(ele.getKey().toString(), ele.getValue());
        }
        config.save(file);
    }

}
