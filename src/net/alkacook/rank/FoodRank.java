package net.alkacook.rank;

import java.util.*;

public class FoodRank {

    private static HashMap<String, Integer> ingredientRank = new HashMap<>();

    public static void addElement(String name) {
        if (!ingredientRank.containsKey(name))
            ingredientRank.put(name, 0);
        else {
            int count = ingredientRank.get(name);
            ingredientRank.replace(name, count + 1);
        }
    }

    public static List<IngredientRank> getIngredientRank() {
        ArrayList<IngredientRank> ranking = new ArrayList<>();
        for(Map.Entry<String,Integer> ele:ingredientRank.entrySet()){
            ranking.add(new IngredientRank(ele.getKey(),ele.getValue()));
        }
        Collections.sort(ranking);
        return ranking;
    }

}
