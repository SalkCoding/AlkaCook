package net.alkacook.config;

import net.alkacook.food.Food;
import net.alkacook.main.Main;

import java.io.File;
import java.util.HashMap;

public class FoodLoader {

    public static HashMap<String, Food> loadFoodList() {
        File dir = Main.getInstance().getDataFolder();
        HashMap<String, Food> list = new HashMap<>();
        return list;
    }

}
