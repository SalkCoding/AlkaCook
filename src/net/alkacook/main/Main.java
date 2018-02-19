package net.alkacook.main;

import net.alkacook.config.StatisticsLoader;
import net.alkacook.config.StatisticsWriter;
import net.alkacook.cook.CookGUIClick;
import net.alkacook.food.FoodEat;
import net.alkacook.food.FoodListManager;
import net.alkacook.rank.FoodStatistics;
import net.alkacook.untill.Constants;
import net.alkacook.untill.Untill;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        if (instance != null) {
            throw new IllegalStateException();
        }
        instance = this;
        FoodListManager.loadFoodList();
        try {
            StatisticsLoader.loadStatistics();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getCommand("cook").setExecutor(new Command());
        getCommand("alkacook").setExecutor(new ReloadCommand());
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new CookGUIClick(), this);
        pluginManager.registerEvents(new FoodEat(), this);
        Bukkit.getLogger().info(Constants.Console + "Plugin is now enable");
    }

    @Override
    public void onDisable() {
        try {
            StatisticsWriter.saveStatistics(FoodStatistics.getStatistics());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getLogger().info(Constants.Console + "Plugin is now disable");
    }

}
