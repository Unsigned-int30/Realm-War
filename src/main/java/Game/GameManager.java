package Game;

import Kingdom.Block.Block;
import Kingdom.Block.VoidBlock;
import Kingdom.Block.ForestBlock;
import Kingdom.Structure.*;
import Kingdom.Unit.*;
import Utils.LoggerManager;
import Utils.ResourceType;
import Utils.StructureType;
import Utils.ResourceYield;
import Utils.UnitType;
import View.GameMap;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class GameManager {
    private List<Player> players;
    private int currentPlayerIndex;
    private GameMap gameMap;
    private boolean isGameOver;

    public boolean isGameOver() {
        return isGameOver;
    }

    public GameManager(List<Player> players, GameMap gameMap) {
        this.players = players != null ? players : new ArrayList<>();
        this.gameMap = gameMap;
        this.currentPlayerIndex = 0;
        this.isGameOver = false;
        if (!this.players.isEmpty()) {
            initializePlayerPositions();
        }
    }

    private void initializePlayerPositions() {
        if (players.size() > 0) {
            Player p1 = players.get(0);
            Block startBlockP1 = gameMap.getBlockAt(1, 1);
            if(startBlockP1 != null) {
                p1.firstPosition(new TownHall(p1), startBlockP1);
                startBlockP1.getStructure().setBlock(startBlockP1);
                List<Block> adjacentBlocks = gameMap.getAdjacentBlocks(startBlockP1);
                for (Block block : adjacentBlocks) {
                    if (!(block instanceof VoidBlock)) {
                        p1.addOwnedBlock(block);
                    }
                }
            }
        }
        if (players.size() > 1) {
            Player p2 = players.get(1);
            Block startBlockP2 = gameMap.getBlockAt(gameMap.getWidth() - 2, gameMap.getHeight() - 2);
            if(startBlockP2 != null) {
                p2.firstPosition(new TownHall(p2), startBlockP2);
                startBlockP2.getStructure().setBlock(startBlockP2);
                List<Block> adjacentBlocks = gameMap.getAdjacentBlocks(startBlockP2);
                for (Block block : adjacentBlocks) {
                    if (!(block instanceof VoidBlock)) {
                        p2.addOwnedBlock(block);
                    }
                }
            }
        }
    }

    public Player getCurrentPlayer() {
        if (players.isEmpty() || isGameOver) return null;
        return players.get(currentPlayerIndex);
    }

    public void startGame() {
        if (players.isEmpty()) {
            LoggerManager.warning("Cannot start game with no players.");
            return;
        }
        LoggerManager.info("Game Started with " + players.size() + " players.");
        startTurnFor(getCurrentPlayer());
    }

    public void endTurn() {
        if (isGameOver) {
            return;
        }
        Player playerWhoEndedTurn = getCurrentPlayer();
        LoggerManager.info("Player '" + playerWhoEndedTurn.getPlayerName() + "' ended their turn.");
        applyMaintenanceCosts(playerWhoEndedTurn);
        checkWinLossConditions();
        if (isGameOver) {
            return;
        }
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (players.get(currentPlayerIndex).isDefeated());
        startTurnFor(getCurrentPlayer());
    }

    private void startTurnFor(Player player) {
        LoggerManager.info("Turn started for player '" + player.getPlayerName() + "'.");
        for (Unit unit : player.getUnits()) {
            unit.resetTurnActions();
        }
        collectTurnResourcesForPlayer(player);
    }

    public void collectTurnResourcesForPlayer(Player player) {
        if (player == null) return;
        int totalGoldCollected = 0;
        int totalFoodCollected = 0;
        for (Block block : player.getOwnedBlocks()) {
            ResourceYield yield = block.produceResources();
            if (yield.getType() == ResourceType.GOLD) { totalGoldCollected += yield.getAmount(); }
            else if (yield.getType() == ResourceType.FOOD) { totalFoodCollected += yield.getAmount(); }
        }
        for (Structure structure : player.getStructures()) {
            totalGoldCollected += structure.getGoldProductionPerTurn();
            totalFoodCollected += structure.getFoodProductionPerTurn();
        }
        player.addGold(totalGoldCollected);
        player.addFood(totalFoodCollected);
        LoggerManager.info("Player '" + player.getPlayerName() + "' collected " + totalGoldCollected + " gold and " + totalFoodCollected + " food.");
    }

    private void applyMaintenanceCosts(Player player) {
        int totalGoldMaintenance = 0;
        int totalFoodMaintenance = 0;
        for (Structure s : player.getStructures()) { totalGoldMaintenance += s.getMaintenanceCost(); }
        for (Unit u : player.getUnits()) {
            totalGoldMaintenance += u.getPaymentGold();
            totalFoodMaintenance += u.getRationFood();
        }
        if (!player.payCost(totalGoldMaintenance, totalFoodMaintenance)) {
            LoggerManager.warning("Player '" + player.getPlayerName() + "' could not afford maintenance costs.");
            handleInsolvency(player, totalGoldMaintenance, totalFoodMaintenance);
        }
    }

    private void handleInsolvency(Player player, int goldUpkeep, int foodUpkeep) {
        LoggerManager.warning("Units are deserting due to lack of payment for player '" + player.getPlayerName() + "'.");
        while (!player.canAfford(goldUpkeep, foodUpkeep) && !player.getUnits().isEmpty()) {
            List<Unit> sortedUnits = new ArrayList<>(player.getUnits());
            sortedUnits.sort(Comparator.comparingInt((Unit u) -> u.getPaymentGold() + u.getRationFood()).reversed());
            Unit unitToDisband = sortedUnits.get(0);
            LoggerManager.info("Unit " + unitToDisband.getClass().getSimpleName() + " has deserted from player '" + player.getPlayerName() + "'.");
            goldUpkeep -= unitToDisband.getPaymentGold();
            foodUpkeep -= unitToDisband.getRationFood();
            removeUnitFromGame(unitToDisband);
            if (goldUpkeep < 0) goldUpkeep = 0;
            if (foodUpkeep < 0) foodUpkeep = 0;
        }
    }

    private void checkWinLossConditions() {
        for (Player player : players) {
            if (!player.isDefeated() && (player.getTownHall() == null || player.getTownHall().isDestroyed())) {
                player.setDefeated(true);
                LoggerManager.info("Player '" + player.getPlayerName() + "' has been defeated!");
            }
        }
        List<Player> activePlayers = players.stream().filter(p -> !p.isDefeated()).collect(Collectors.toList());
        if (activePlayers.size() <= 1) {
            isGameOver = true;
            if (activePlayers.size() == 1) {
                LoggerManager.info("Game Over! The winner is " + activePlayers.get(0).getPlayerName());
            } else {
                LoggerManager.info("Game Over! It's a draw!");
            }
        }
    }

    public void tryMoveUnit(Player player, Unit unit, Block destination) {
        if (player != getCurrentPlayer()) return;
        if (unit == null) return;
        if (unit.getOwner() != player) return;

        Block source = unit.getBlock();
        if (source == null || destination == null) return;

        if (gameMap.getDistance(source, destination) > unit.getMovementBlockRange()) return;
        if (destination.hasUnit()) return;
        if (destination.hasStructure() && destination.getStructure().getOwner() != player) return; // حرکت به بلوک با ساختمان دشمن مجاز نیست
        if (!destination.canMoveInto()) return;

        List<Block> neighborsOfDestination = gameMap.getAdjacentBlocks(destination);
        for (Block neighbor : neighborsOfDestination) {
            if (neighbor.hasStructure() && neighbor.getStructure().getOwner() != player) {
                if (neighbor.getStructure() instanceof Tower) {
                    Tower enemyTower = (Tower) neighbor.getStructure();
                    if (unit.getRank() <= enemyTower.getRestrictionLevel()) {
                        return;
                    }
                }
            }
        }

        source.setUnit(null);
        destination.setUnit(unit);
        unit.setBlock(destination);
        if (destination.getOwner() != player) {
            if (destination.getOwner() != null) {
                destination.getOwner().removeOwnedBlock(destination);
            }
            player.addOwnedBlock(destination);
        }
        LoggerManager.info("Unit " + unit.getClass().getSimpleName() + " moved to (" + destination.getX() + "," + destination.getY() + ").");
    }

    public void tryAttack(Player player, Unit attacker, Block targetBlock) {
        if (player != getCurrentPlayer()) return;
        if (attacker.hasAttackedThisTurn()) {
            System.out.println("Attack failed: This unit has already attacked this turn.");
            return;
        }
        if (targetBlock == null) return;
        if (gameMap.getDistance(attacker.getBlock(), targetBlock) > attacker.getAttackRange()) return;

        Object target = targetBlock.getUnit() != null ? targetBlock.getUnit() : targetBlock.getStructure();
        if (target == null) return;

        Player targetOwner = (target instanceof Unit) ? ((Unit) target).getOwner() : ((Structure) target).getOwner();
        if (targetOwner == player) return;

        int damage = attacker.getAttackPower();
        if (attacker.getBlock() instanceof ForestBlock && ((ForestBlock) attacker.getBlock()).hasForest()) { damage = (int) (damage * 1.5); }
        if (targetBlock instanceof ForestBlock && ((ForestBlock) targetBlock).hasForest()) { damage = (int) (damage * 0.75); }

        if (target instanceof Unit) {
            Unit targetUnit = (Unit) target;
            targetUnit.dealDamage(damage);
            LoggerManager.info(attacker.getClass().getSimpleName() + " attacked " + targetUnit.getClass().getSimpleName() + " for " + damage + " damage.");
            if (!targetUnit.isAlive()) {
                LoggerManager.info(targetUnit.getClass().getSimpleName() + " was defeated!");
                removeUnitFromGame(targetUnit);
            }
        } else if (target instanceof Structure) {
            Structure targetStructure = (Structure) target;
            targetStructure.takeDamage(damage);
            LoggerManager.info(attacker.getClass().getSimpleName() + " attacked " + targetStructure.getClass().getSimpleName() + " for " + damage + " damage.");
            if (targetStructure.isDestroyed()) {
                LoggerManager.info(targetStructure.getClass().getSimpleName() + " was destroyed!");
                removeStructureFromGame(targetStructure);
            }
        }
        attacker.setHasAttackedThisTurn(true);
    }

    public void tryMergeUnits(Player player, Unit unit1, Unit unit2) {
        if (player != getCurrentPlayer()) return;
        if (unit1 == null || unit2 == null) return;
        if (unit1.getOwner() != player || unit2.getOwner() != player) return;
        if (gameMap.getDistance(unit1.getBlock(), unit2.getBlock()) > 1) return;
        if (!unit1.canMerge(unit2)) return;

        Unit newUnit = unit1.merge(unit2);
        if (newUnit != null) {
            Block position = unit1.getBlock();
            LoggerManager.info("Units " + unit1.getClass().getSimpleName() + " merged into " + newUnit.getClass().getSimpleName() + ".");
            removeUnitFromGame(unit1);
            removeUnitFromGame(unit2);
            player.addUnit(newUnit);
            position.setUnit(newUnit);
            newUnit.setBlock(position);
        }
    }

    public void tryBuildStructure(Player player, StructureType type, int x, int y) {
        if (player != getCurrentPlayer()) return;
        Block targetBlock = gameMap.getBlockAt(x, y);
        if (targetBlock == null || targetBlock.getOwner() != player || !targetBlock.canBuildStructure()) return;
        int buildingCost = 0;
        int maxAllowed = Integer.MAX_VALUE;
        switch (type) {
            case FARM: buildingCost = Farm.BUILDING_COST; maxAllowed = Farm.MAX_ALLOWED; break;
            case MARKET: buildingCost = Market.BUILDING_COST; maxAllowed = Market.MAX_ALLOWED; break;
            case BARRACK: buildingCost = Barrack.BUILDING_COST; maxAllowed = Barrack.MAX_ALLOWED; break;
            case TOWER: buildingCost = Tower.BUILDING_COST; break;
        }
        if (player.getStructureCountByType(type) >= maxAllowed) return;
        if (!player.canAfford(buildingCost, 0)) return;
        player.payCost(buildingCost, 0);
        Structure newStructure = null;
        switch (type) {
            case FARM: newStructure = new Farm(player); break;
            case MARKET: newStructure = new Market(player); break;
            case BARRACK: newStructure = new Barrack(player); break;
            case TOWER: newStructure = new Tower(player); break;
        }
        player.addStructure(newStructure);
        targetBlock.setStructure(newStructure);
        newStructure.setBlock(targetBlock);
        if (targetBlock instanceof ForestBlock) { ((ForestBlock) targetBlock).removeForest(); }
        LoggerManager.info("Player '" + player.getPlayerName() + "' built a " + type + ".");
    }

    public void tryUpgradeStructure(Player player, Structure structureToUpgrade) {
        if (player != getCurrentPlayer()) return;
        if (structureToUpgrade == null) return;
        if (structureToUpgrade.getOwner() != player) return;
        if (player.canAfford(structureToUpgrade.getLevelUpCost(), 0)) {
            player.payCost(structureToUpgrade.getLevelUpCost(), 0);
            structureToUpgrade.upgradeLevel();
            LoggerManager.info("Structure " + structureToUpgrade.getClass().getSimpleName() + " upgraded.");
        }
    }

    public void tryProduceUnit(Player player, UnitType type, Structure productionBuilding) {
        if (player != getCurrentPlayer()) return;
        if (productionBuilding == null || productionBuilding.getOwner() != player) return;
        if (!(productionBuilding instanceof Barrack) && !(productionBuilding instanceof TownHall)) return;
        int goldCost = 0;
        Unit newUnit = null;
        switch (type) {
            case PEASANT: goldCost = Peasant.CREATION_GOLD_COST; newUnit = new Peasant(player); break;
            case SPEARMAN: goldCost = Spearman.CREATION_GOLD_COST; newUnit = new Spearman(player); break;
            case SWORDMAN: goldCost = Swordman.CREATION_GOLD_COST; newUnit = new Swordman(player); break;
            case KNIGHT: goldCost = Knight.CREATION_GOLD_COST; newUnit = new Knight(player); break;
        }
        if (newUnit == null) return;
        if (!player.canAfford(goldCost, 0)) return;
        if (!player.canPlaceUnit(newUnit)) return;
        Block buildingBlock = productionBuilding.getBlock();
        if (buildingBlock == null) return;
        Block placementTarget = null;
        for (Block block : gameMap.getAdjacentBlocks(buildingBlock)) {
            if (block.canMoveInto() && (block.getOwner() == player || block.getOwner() == null)) {
                placementTarget = block;
                break;
            }
        }
        if (placementTarget == null) return;
        player.payCost(goldCost, 0);
        player.addUnit(newUnit);
        placementTarget.setUnit(newUnit);
        newUnit.setBlock(placementTarget);
        LoggerManager.info("Player '" + player.getPlayerName() + "' produced a " + type + ".");
    }

    private void removeUnitFromGame(Unit unit) {
        if (unit == null) return;
        if (unit.getOwner() != null) { unit.getOwner().removeUnit(unit); }
        if (unit.getBlock() != null) { unit.getBlock().setUnit(null); }
        LoggerManager.info("Unit " + unit.getClass().getSimpleName() + " was removed from the game.");
    }

    private void removeStructureFromGame(Structure structure) {
        if (structure == null) return;
        if (structure.getOwner() != null) { structure.getOwner().removeStructure(structure); }
        if (structure.getBlock() != null) { structure.getBlock().setStructure(null); }
        LoggerManager.info("Structure " + structure.getClass().getSimpleName() + " was removed from the game.");
    }
}