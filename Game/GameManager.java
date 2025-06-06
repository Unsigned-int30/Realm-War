package Game;

import Kingdom.Block.Block;
import Kingdom.Structure.Structure;

import java.util.List;

public class GameManager {
    public GameManager() {

    }

    public void collectResources(Player player) {
        if (player == null) {
            System.err.println("Cannot collect resources: Player is null.");
            return;
        }

        System.out.println("Collecting resources for " + player.getPlayerName() + "...");
        int totalGoldCollected = 0;
        int totalFoodCollected = 0;

        // جمع‌آوری منابع از بلوک‌های تحت مالکیت
        List<Block> ownedBlocks = player.getOwnedBlocks();
        for (Block block : ownedBlocks) {
            if (block != null) {
                ResourceYield yield = block.produceResources();
                if (yield != null && yield.getAmount() > 0) {
                    if (yield.getType() == ResourceType.GOLD) {
                        totalGoldCollected += yield.getAmount();
                    } else if (yield.getType() == ResourceType.FOOD) {
                        totalFoodCollected += yield.getAmount();
                    }
                }
            }
        }

        //  جمع‌آوری منابع از ساختمان‌ها
        List<Structure> structures = player.getStructures();
        for (Structure structure : structures) {
            if (structure != null) {
                int goldFromStructure = structure.getGoldProductionTurn();
                int foodFromStructure = structure.getFoodProduction();

                if (goldFromStructure > 0) {
                    totalGoldCollected += goldFromStructure;
                }
                if (foodFromStructure > 0) {
                    totalFoodCollected += foodFromStructure;
                }
            }
        }

        // اضافه کردن منابع جمع‌آوری شده به بازیکن
        if (totalGoldCollected > 0) {
            player.addGold(totalGoldCollected);
        }
        if (totalFoodCollected > 0) {
            player.addFood(totalFoodCollected);
        }

        System.out.println(player.getPlayerName() + " collected: " + totalGoldCollected + " Gold, " + totalFoodCollected + " Food.");
        System.out.println("Current totals - Gold: " + player.getGold() + ", Food: " + player.getFood());
    }

    // public void nextTurn() { ... }
    // public void checkForWin() { ... }
}
