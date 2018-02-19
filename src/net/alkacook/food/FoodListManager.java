package net.alkacook.food;

import net.alkacook.config.FoodLoader;

import java.io.IOException;
import java.util.HashMap;

public class FoodListManager {

    private static HashMap<String,Food> foodList = new HashMap<>();

    public static void loadFoodList() {
        try {
            foodList = FoodLoader.loadFoodList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Food getCustomFood(String name){
        return foodList.get(name);
    }

    public static HashMap<String,Food> getFoodList(){
        return  foodList;
    }

}
