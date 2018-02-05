package net.alkacook.food;

import net.alkacook.config.FoodLoader;

import java.util.HashMap;

public class FoodListManager {

    private static HashMap<String,Food> foodList = new HashMap<>();

    public static void loadFoodList() {
        foodList = FoodLoader.loadFoodList();
    }

    public static Food getCustomFood(String name){
        return foodList.get(name);
    }

    public static HashMap<String,Food> getFoodList(){
        return  foodList;
    }

}
