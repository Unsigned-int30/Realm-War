package Kingdom.Structure;

import Game.Player;

public class Farm extends Structure {
    public static final int BUILDING_COST = 20;
    public static final int INITIAL_DURABILITY = 40;
    public static final int MAINTENANCE_COST = 2;
    public static final int INITIAL_LEVEL_UP_COST = 15;
    public static final int MAX_ALLOWED = 4;
    private static final int BASE_FOOD_PRODUCTION_PER_LEVEL = 5;
    public static final int MAX_LEVEL = 3;
    private static final int DURABILITY_INCREASE_PER_LEVEL = 15;

    public Farm(Player owner) {
        super(INITIAL_DURABILITY, owner, 1, MAINTENANCE_COST, BUILDING_COST, INITIAL_LEVEL_UP_COST);
    }

    @Override
    public int getFoodProductionPerTurn() {
        return getLevel() * BASE_FOOD_PRODUCTION_PER_LEVEL;
    }

    @Override
    public void upgradeLevel() {
        if (getLevel() < MAX_LEVEL) {
            setLevel(getLevel() + 1);
            setDurability(getDurability() + DURABILITY_INCREASE_PER_LEVEL);
        }
    }
}