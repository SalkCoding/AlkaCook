package net.alkacook.rank;

import java.util.*;

public class FoodStatistics {

    private static HashMap<String, Integer> statistics = new HashMap<>();

    public static void addStatistics(String name) {
        if (!statistics.containsKey(name))
            statistics.put(name, 0);
        else {
            int count = statistics.get(name);
            statistics.replace(name, count + 1);
        }
    }

    public static void setStatistics(HashMap<String, Integer> statistics) {
        FoodStatistics.statistics = statistics;
    }

    public static HashMap<String, Integer> getStatistics() {
        return statistics;
    }

    public static List<FoodStatisticsRank> getStatisticsRank(){
        ArrayList<FoodStatisticsRank> ranking = new ArrayList<>();
        for(Map.Entry<String,Integer> ele:statistics.entrySet()){
            ranking.add(new FoodStatisticsRank(ele.getKey(),ele.getValue()));
        }
        Collections.sort(ranking);
        return ranking;
    }

}
