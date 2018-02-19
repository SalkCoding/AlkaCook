package net.alkacook.rank;

public class FoodStatisticsRank implements Comparable<FoodStatisticsRank>{
    private String name;
    private int count;

    public FoodStatisticsRank(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(FoodStatisticsRank o) {
        if (count < o.getCount())
            return 1;
        else if (count > o.getCount())
            return -1;
        else
            return 0;
    }

}
