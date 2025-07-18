package View;

import Game.DataBase.dataBaseInitializer;
import Game.DataBase.gameRepository;
import Game.GameManager;
import Game.Player;
import Kingdom.Block.Block;
import Kingdom.Structure.*;
import Kingdom.Unit.Unit;
import Utils.LoggerManager;
import Utils.StructureType;
import Utils.UnitType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainPanel {
    private JFrame frame;
    private GameMap gameMap;
    private MapPanel mapPanel;
    private InfoPanel infoPanel;
    private ControlPanel controlPanel;
    private GameManager gameManager;
    private Block selectedBlock;
    private Unit selectedUnit;
    private Timer turnTimer;
    private int timeLeft;
    private final int TURN_DURATION = 30;

    public MainPanel() {
        setupInitialUI();
        infoPanel.updateSelectionInfo("Initializing game, please wait...");
        loadGameInBackground();
    }

    private void setupInitialUI() {
        mapPanel = new MapPanel(null);
        infoPanel = new InfoPanel();
        controlPanel = new ControlPanel();
        frame = new JFrame("Realm War");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 0));
        frame.add(mapPanel, BorderLayout.CENTER);
        frame.add(infoPanel, BorderLayout.EAST);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void loadGameInBackground() {
        controlPanel.setAllButtonsEnabled(false);
        SwingWorker<GameManager, Void> worker = new SwingWorker<>() {
            @Override
            protected GameManager doInBackground() throws Exception {
                LoggerManager.setup();
                AssetManager.loadAssets();
                dataBaseInitializer.init();
                List<Player> players = new ArrayList<>();
                players.add(new Player(0, "Player 1 (Blue)"));
                players.add(new Player(1, "Player 2 (Red)"));
                GameMap newMap = new GameMap();
                return new GameManager(players, newMap);
            }

            @Override
            protected void done() {
                try {
                    gameManager = get();
                    gameMap = gameManager.getGameMap();
                    mapPanel.setGameMap(gameMap);
                    gameManager.setInfoPanel(infoPanel);
                    addListeners();
                    setupTimer();
                    gameManager.startGame();
                    updateUI();
                    startTurnTimer();
                } catch (Exception e) {
                    handleFatalError(e, "Application failed to start.");
                } finally {
                    controlPanel.setAllButtonsEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void addListeners() {
        controlPanel.getEndTurnButton().addActionListener(e -> endTurnAndReset());
        controlPanel.getBuildActionsButton().addActionListener(e -> handleShowBuildOptions());
        controlPanel.getUnitActionsButton().addActionListener(e -> handleShowUnitOptions());
        controlPanel.getOtherActionsButton().addActionListener(e -> handleShowOtherActions());
        controlPanel.getSaveButton().addActionListener(e -> handleSaveGame());
        controlPanel.getLoadButton().addActionListener(e -> handleLoadGame());
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameManager == null || gameManager.isGameOver()) return;
                int x = e.getX() / mapPanel.getCellSize();
                int y = e.getY() / mapPanel.getCellSize();
                if (!gameMap.isValidCoordinate(x, y)) return;
                Block clickedBlock = gameMap.getBlockAt(x, y);
                handleMapClick(clickedBlock);
            }
        });
    }

    private void handleMapClick(Block clickedBlock) {
        Player currentPlayer = gameManager.getCurrentPlayer();
        if (selectedUnit != null) {
            Unit targetUnit = clickedBlock.getUnit();
            boolean isFriendlyUnitTarget = targetUnit != null && targetUnit.getOwner() == currentPlayer && selectedUnit != targetUnit;
            boolean isEnemyBlock = clickedBlock.getOwner() != null && clickedBlock.getOwner() != currentPlayer;
            boolean isTargetDefended = clickedBlock.hasUnit() || clickedBlock.hasStructure();
            if (isFriendlyUnitTarget && selectedUnit.canMerge(targetUnit)) {
                gameManager.tryMergeUnits(currentPlayer, selectedUnit, targetUnit);
            } else if (isEnemyBlock && isTargetDefended) {
                gameManager.tryAttack(currentPlayer, selectedUnit, clickedBlock);
            } else {
                gameManager.tryMoveUnit(currentPlayer, selectedUnit, clickedBlock);
            }
            clearSelection();
            updateUI();
        } else {
            selectedBlock = clickedBlock;
            selectedUnit = clickedBlock.hasUnit() && clickedBlock.getUnit().getOwner() == currentPlayer ? clickedBlock.getUnit() : null;
            mapPanel.setSelectedBlock(selectedBlock);
            updateSelectionInfoPanel();
            updateButtonStates();
        }
    }

    private void handleSaveGame() {
        String saveName = JOptionPane.showInputDialog(frame, "Enter a name for your save:", "Save Game", JOptionPane.PLAIN_MESSAGE);
        if (saveName == null || saveName.trim().isEmpty()) return;
        infoPanel.updateSelectionInfo("Saving game as '" + saveName + "'...");
        controlPanel.setAllButtonsEnabled(false);
        SwingWorker<Void, Void> saveWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                gameRepository.saveGame(gameManager, saveName);
                return null;
            }
            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(frame, "Game saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    handleFatalError(e, "Failed to save game.");
                } finally {
                    updateUI();
                }
            }
        };
        saveWorker.execute();
    }

    private void handleLoadGame() {
        String saveName = JOptionPane.showInputDialog(frame, "Enter the Save Name to load:", "Load Game", JOptionPane.PLAIN_MESSAGE);
        if (saveName == null || saveName.trim().isEmpty()) return;
        infoPanel.updateSelectionInfo("Loading game '" + saveName + "'...");
        controlPanel.setAllButtonsEnabled(false);
        SwingWorker<GameManager, Void> loadWorker = new SwingWorker<>() {
            @Override
            protected GameManager doInBackground() {
                return gameRepository.loadGame(saveName);
            }
            @Override
            protected void done() {
                try {
                    GameManager loadedGame = get();
                    if (loadedGame != null) {
                        gameManager = loadedGame;
                        gameMap = gameManager.getGameMap();
                        mapPanel.setGameMap(gameMap);
                        gameManager.setInfoPanel(infoPanel);
                        JOptionPane.showMessageDialog(frame, "Game loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Could not find save: " + saveName, "Load Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    handleFatalError(e, "Failed to load game.");
                } finally {
                    clearSelection();
                    updateUI();
                }
            }
        };
        loadWorker.execute();
    }

    private void handleShowOtherActions() {
        if (selectedBlock == null) { JOptionPane.showMessageDialog(frame, "Please make a selection first.", "Action Error", JOptionPane.ERROR_MESSAGE); return; }
        ArrayList<String> options = new ArrayList<>();
        if (selectedBlock.hasStructure() && selectedBlock.getStructure().getOwner() == gameManager.getCurrentPlayer()) {
            Structure s = selectedBlock.getStructure();
            int maxLevel = 0;
            if (s instanceof Farm) maxLevel = Farm.MAX_LEVEL;
            else if (s instanceof Market) maxLevel = Market.MAX_LEVEL;
            else if (s instanceof Barrack) maxLevel = Barrack.MAX_LEVEL;
            else if (s instanceof Tower) maxLevel = Tower.MAX_LEVEL;
            if (s.getLevel() < maxLevel) {
                options.add("Upgrade (Cost: " + s.getLevelUpCost() + " Gold)");
            }
        }
        if (options.isEmpty()) { JOptionPane.showMessageDialog(frame, "No special actions available.", "Info", JOptionPane.INFORMATION_MESSAGE); return; }
        String choice = (String) JOptionPane.showInputDialog(frame, "Choose an action:", "Other Actions", JOptionPane.PLAIN_MESSAGE, null, options.toArray(), options.get(0));
        if (choice != null && choice.startsWith("Upgrade")) {
            gameManager.tryUpgradeStructure(gameManager.getCurrentPlayer(), selectedBlock.getStructure());
            updateUI();
        }
    }

    private void handleShowBuildOptions() {
        if (selectedBlock == null) { JOptionPane.showMessageDialog(frame, "Please select a block first.", "Build Error", JOptionPane.ERROR_MESSAGE); return; }
        StructureType[] buildOptions = StructureType.values();
        StructureType choice = (StructureType) JOptionPane.showInputDialog(frame, "Choose a structure to build:", "Build Structure", JOptionPane.PLAIN_MESSAGE, null, buildOptions, buildOptions[0]);
        if (choice != null) {
            gameManager.tryBuildStructure(gameManager.getCurrentPlayer(), choice, selectedBlock.getX(), selectedBlock.getY());
            clearSelection();
            updateUI();
        }
    }

    private void handleShowUnitOptions() {
        if (selectedBlock == null || !selectedBlock.hasStructure()) { JOptionPane.showMessageDialog(frame, "Please select a production building.", "Production Error", JOptionPane.ERROR_MESSAGE); return; }
        Structure productionBuilding = selectedBlock.getStructure();
        if (!(productionBuilding instanceof TownHall || productionBuilding instanceof Barrack)) { JOptionPane.showMessageDialog(frame, "This building cannot produce units.", "Production Error", JOptionPane.ERROR_MESSAGE); return; }
        UnitType[] produceOptions = UnitType.values();
        UnitType choice = (UnitType) JOptionPane.showInputDialog(frame, "Choose a unit to produce:", "Produce Unit", JOptionPane.PLAIN_MESSAGE, null, produceOptions, produceOptions[0]);
        if (choice != null) {
            gameManager.tryProduceUnit(gameManager.getCurrentPlayer(), choice, productionBuilding);
            clearSelection();
            updateUI();
        }
    }

    private void setupTimer() {
        timeLeft = TURN_DURATION;
        ActionListener timerAction = e -> {
            timeLeft--;
            infoPanel.updateTimer(timeLeft);
            if (timeLeft <= 0) {
                turnTimer.stop();
                endTurnAndReset();
            }
        };
        turnTimer = new Timer(1000, timerAction);
        turnTimer.setInitialDelay(0);
    }

    private void startTurnTimer() {
        if (turnTimer != null) {
            timeLeft = TURN_DURATION;
            infoPanel.updateTimer(timeLeft);
            turnTimer.restart();
        }
    }

    private void endTurnAndReset() {
        if (turnTimer != null) turnTimer.stop();
        gameManager.endTurn();
        clearSelection();
        updateUI();
        if (!gameManager.isGameOver()) {
            startTurnTimer();
        }
    }

    private void updateUI() {
        if (gameManager == null) return;
        Player currentPlayer = gameManager.getCurrentPlayer();
        controlPanel.setAllButtonsEnabled(!gameManager.isGameOver());
        if (gameManager.isGameOver()) {
            infoPanel.updatePlayerInfo(null);
            infoPanel.updateSelectionInfo("GAME OVER");
        } else {
            infoPanel.updatePlayerInfo(currentPlayer);
            updateButtonStates();
        }
        mapPanel.refreshView();
    }

    private void updateButtonStates() {
        Player currentPlayer = gameManager.getCurrentPlayer();
        if (currentPlayer == null) return;

        controlPanel.setAllButtonsEnabled(true);

        boolean canBuild = selectedBlock != null && selectedBlock.getOwner() == currentPlayer && !selectedBlock.hasStructure();
        controlPanel.getBuildActionsButton().setEnabled(canBuild);

        boolean canProduce = false;
        boolean canDoOtherActions = false;

        if (selectedBlock != null && selectedBlock.getOwner() == currentPlayer) {
            if (selectedBlock.hasStructure()) {
                Structure s = selectedBlock.getStructure();
                if (s instanceof TownHall || s instanceof Barrack) {
                    canProduce = true;
                }
                canDoOtherActions = true;
            }
            if (selectedBlock.hasUnit()) {
                canDoOtherActions = true;
            }
        }

        controlPanel.getUnitActionsButton().setEnabled(canProduce);
        controlPanel.getOtherActionsButton().setEnabled(canDoOtherActions);
    }

    private void clearSelection() {
        selectedBlock = null;
        selectedUnit = null;
        if (mapPanel != null) mapPanel.setSelectedBlock(null);
        updateSelectionInfoPanel();
    }

    private void updateSelectionInfoPanel() {
        if (selectedBlock == null) {
            infoPanel.updateSelectionInfo("No selection.");
            return;
        }
        StringBuilder info = new StringBuilder();
        info.append("Block: (").append(selectedBlock.getX()).append(", ").append(selectedBlock.getY()).append(")\n");
        info.append("Type: ").append(selectedBlock.getClass().getSimpleName()).append("\n");
        if (selectedBlock.getOwner() != null) { info.append("Owner: ").append(selectedBlock.getOwner().getPlayerName()).append("\n"); }
        else { info.append("Owner: None\n"); }
        if (selectedBlock.hasStructure()) {
            Structure s = selectedBlock.getStructure();
            info.append("\n--- Structure ---\n");
            info.append("Type: ").append(s.getClass().getSimpleName()).append(" (Lvl ").append(s.getLevel()).append(")\n");
            info.append("Durability: ").append(s.getDurability());
        }
        if (selectedBlock.hasUnit()) {
            Unit u = selectedBlock.getUnit();
            info.append("\n--- Unit ---\n");
            info.append("Type: ").append(u.getClass().getSimpleName()).append("\n");
            info.append("HP: ").append(u.getHitPoints()).append("\n");
            info.append("Attack: ").append(u.getAttackPower());
        }
        infoPanel.updateSelectionInfo(info.toString());
    }

    private void handleFatalError(Exception e, String message) {
        LoggerManager.severe(message + ": " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame, message, "Critical Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainPanel::new);
    }
}