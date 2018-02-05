package net.alkacook.main;

import net.alkacook.food.FoodListManager;
import net.alkacook.untill.Constants;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
        getCommand("Cook").setExecutor(new Command());
        Bukkit.getLogger().info(Constants.Console + "Plugin is now enable");
    }

    @Override
    public void onDisable() {
        if (instance == this) {
            instance = null;
        }
        Bukkit.getLogger().info(Constants.Console + "Plugin is now disable");
    }

}
