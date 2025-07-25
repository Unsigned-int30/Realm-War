package Kingdom.Structure;

import Game.Player;

public class Tower extends Structure {
    public static final int BUILDING_COST = 40;
    public static final int INITIAL_DURABILITY = 50;
    public static final int MAINTENANCE_COST = 5;
    public static final int INITIAL_LEVEL_UP_COST = 30;
    public static final int MAX_LEVEL = 3;
    private static final int DURABILITY_INCREASE_PER_LEVEL = 20;

    public Tower(Player owner) {
        super(INITIAL_DURABILITY, owner, 1, MAINTENANCE_COST, BUILDING_COST, INITIAL_LEVEL_UP_COST);
    }

    public int getRestrictionLevel() {
        return getLevel();
    }

    @Override
    public void upgradeLevel() {
        if (getLevel() < MAX_LEVEL) {
            setLevel(getLevel() + 1);
            setDurability(getDurability() + DURABILITY_INCREASE_PER_LEVEL);
        }
    }
}