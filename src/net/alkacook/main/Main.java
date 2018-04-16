package net.alkacook.main;

import net.alkacook.config.StatisticsLoader;
import net.alkacook.config.StatisticsWriter;
import net.alkacook.cook.CookGUI;
import net.alkacook.cook.CookGUIClick;
import net.alkacook.cook.event.IceStick;
import net.alkacook.cook.event.NotAllowCraft;
import net.alkacook.cook.event.PlayerQuit;
import net.alkacook.cook.event.PreventBrew;
import net.alkacook.food.FoodEat;
import net.alkacook.food.FoodListManager;
import net.alkacook.main.command.Command;
import net.alkacook.main.command.OtherCommand;
import net.alkacook.rank.FoodStatistics;
import net.alkacook.untill.Constants;
import net.alkacook.untill.Untill;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private static ArrayList<String> limitWorld = new ArrayList<>();
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public static List<String> getLimitWorldNames() {
        return limitWorld;
    }

    @Override
    public void onEnable() {
        if (instance != null) {
            throw new IllegalStateException();
        }
        instance = this;
        FoodListManager.loadFoodList();
        getConfig().options().copyDefaults(true);
        for (String worldName : getConfig().getStringList("limitworld")) {
            limitWorld.add(worldName);
        }
        saveConfig();
        try {
            StatisticsLoader.loadStatistics();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getCommand("cook").setExecutor(new Command());
        getCommand("alkacook").setExecutor(new OtherCommand());
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new IceStick(), this);
        pluginManager.registerEvents(new NotAllowCraft(), this);
        pluginManager.registerEvents(new PlayerQuit(), this);
        pluginManager.registerEvents(new CookGUI(), this);
        pluginManager.registerEvents(new CookGUIClick(), this);
        pluginManager.registerEvents(new FoodEat(), this);
        //pluginManager.registerEvents(new PreventBrew(), this);
        Bukkit.getLogger().info(Constants.Console + "Plugin is now enable");
    }

    @Override
    public void onDisable() {
        try {
            StatisticsWriter.saveStatistics(FoodStatistics.getStatistics());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Untill.safeDisable();
        limitWorld.clear();
        Bukkit.getLogger().info(Constants.Console + "Plugin is now disable");
    }

}
