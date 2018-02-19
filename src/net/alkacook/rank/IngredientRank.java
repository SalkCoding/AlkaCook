package net.alkacook.rank;

public class IngredientRank implements Comparable<IngredientRank> {

    private String name;
    private int count;

    public IngredientRank(String name, int count) {
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
    public int compareTo(IngredientRank o) {
        if (count < o.getCount())
            return 1;
        else if (count > o.getCount())
            return -1;
        else
            return 0;
    }
}
