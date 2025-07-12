package View;

import Game.GameManager;
import Game.Player;
import Kingdom.Block.Block;
import Kingdom.Structure.Barrack;
import Kingdom.Structure.Structure;
import Kingdom.Structure.TownHall;
import Kingdom.Unit.Unit;
import Utils.StructureType;
import Utils.UnitType;
import Utils.LoggerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    public MainPanel() {
        try {
            LoggerManager.setup();
            AssetManager.loadAssets();
            gameMap = new GameMap();
            List<Player> players = new ArrayList<>();
            players.add(new Player(0, "Player 1 (Blue)"));
            players.add(new Player(1, "Player 2 (Red)"));
            gameManager = new GameManager(players, gameMap);
            mapPanel = new MapPanel(gameMap);
            infoPanel = new InfoPanel();
            controlPanel = new ControlPanel();
            frame = new JFrame("Realm War");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout(10, 0));
            frame.add(mapPanel, BorderLayout.CENTER);
            frame.add(infoPanel, BorderLayout.EAST);
            frame.add(controlPanel, BorderLayout.SOUTH);
            addListeners();
            gameManager.startGame();
            updateUI();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        } catch (Exception e) {
            LoggerManager.severe("Application startup failed: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "A critical error occurred: " + e.getMessage(), "Application Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addListeners() {
        controlPanel.getEndTurnButton().addActionListener(e -> {
            gameManager.endTurn();
            clearSelection();
            updateUI();
        });

        controlPanel.getBuildActionsButton().addActionListener(e -> handleShowBuildOptions());
        controlPanel.getUnitActionsButton().addActionListener(e -> handleShowUnitOptions());
        controlPanel.getOtherActionsButton().addActionListener(e -> handleShowOtherActions());

        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / mapPanel.getCellSize();
                int y = e.getY() / mapPanel.getCellSize();
                if (!gameMap.isValidCoordinate(x, y)) return;

                Block clickedBlock = gameMap.getBlockAt(x, y);
                Player currentPlayer = gameManager.getCurrentPlayer();

                if (selectedUnit != null) {
                    Unit targetUnit = clickedBlock.getUnit();
                    boolean isFriendlyUnitTarget = targetUnit != null && targetUnit.getOwner() == currentPlayer && selectedUnit != targetUnit;
                    boolean isEnemyBlock = clickedBlock.getOwner() != null && clickedBlock.getOwner() != currentPlayer;
                    boolean isTargetDefended = clickedBlock.hasUnit() || clickedBlock.hasStructure();

                    if (isFriendlyUnitTarget) {
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
                    if (clickedBlock.hasUnit() && clickedBlock.getUnit().getOwner() == currentPlayer) {
                        selectedUnit = clickedBlock.getUnit();
                    } else {
                        selectedUnit = null;
                    }
                    mapPanel.setSelectedBlock(selectedBlock);
                    updateSelectionInfoPanel();
                    updateButtonStates();
                }
            }
        });
    }

    private void handleShowOtherActions() {
        if (selectedBlock == null) {
            JOptionPane.showMessageDialog(frame, "Please make a selection first.", "Action Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedBlock.hasStructure() && selectedBlock.getStructure().getOwner() == gameManager.getCurrentPlayer()) {
            String[] options = {"Upgrade Structure"};
            String choice = (String) JOptionPane.showInputDialog(frame, "Choose an action for the selected structure:", "Structure Actions", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (choice != null && choice.equals("Upgrade Structure")) {
                gameManager.tryUpgradeStructure(gameManager.getCurrentPlayer(), selectedBlock.getStructure());
                clearSelection();
                updateUI();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No special actions for this selection.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleShowBuildOptions() {
        if (selectedBlock == null) {
            JOptionPane.showMessageDialog(frame, "Please select a block first.", "Build Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        StructureType[] buildOptions = StructureType.values();
        StructureType choice = (StructureType) JOptionPane.showInputDialog(frame, "Choose a structure to build:", "Build Structure", JOptionPane.PLAIN_MESSAGE, null, buildOptions, buildOptions[0]);
        if (choice != null) {
            gameManager.tryBuildStructure(gameManager.getCurrentPlayer(), choice, selectedBlock.getX(), selectedBlock.getY());
            clearSelection();
            updateUI();
        }
    }

    private void handleShowUnitOptions() {
        if (selectedBlock == null || !selectedBlock.hasStructure()) {
            JOptionPane.showMessageDialog(frame, "Please select a production building.", "Production Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Structure productionBuilding = selectedBlock.getStructure();
        if (!(productionBuilding instanceof TownHall || productionBuilding instanceof Barrack)) {
            JOptionPane.showMessageDialog(frame, "This building cannot produce units.", "Production Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UnitType[] produceOptions = UnitType.values();
        UnitType choice = (UnitType) JOptionPane.showInputDialog(frame, "Choose a unit to produce:", "Produce Unit", JOptionPane.PLAIN_MESSAGE, null, produceOptions, produceOptions[0]);
        if (choice != null) {
            gameManager.tryProduceUnit(gameManager.getCurrentPlayer(), choice, productionBuilding);
            clearSelection();
            updateUI();
        }
    }

    private void updateButtonStates() {
        Player currentPlayer = gameManager.getCurrentPlayer();
        if (currentPlayer == null) return;

        boolean buildEnabled = selectedBlock != null && selectedBlock.getOwner() == currentPlayer && !selectedBlock.hasStructure();
        controlPanel.getBuildActionsButton().setEnabled(buildEnabled);

        boolean produceEnabled = false;
        boolean otherActionsEnabled = false;

        if (selectedBlock != null && selectedBlock.getOwner() == currentPlayer) {
            if (selectedBlock.hasStructure()) {
                Structure s = selectedBlock.getStructure();
                if (s instanceof TownHall || s instanceof Barrack) {
                    produceEnabled = true;
                }
                otherActionsEnabled = true;
            }
            if (selectedBlock.hasUnit()) {
                otherActionsEnabled = true;
            }
        }
        controlPanel.getUnitActionsButton().setEnabled(produceEnabled);
        controlPanel.getOtherActionsButton().setEnabled(otherActionsEnabled);
    }

    private void clearSelection() {
        selectedBlock = null;
        selectedUnit = null;
        mapPanel.setSelectedBlock(null);
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

    private void updateUI() {
        Player currentPlayer = gameManager.getCurrentPlayer();
        if (gameManager.isGameOver()) {
            infoPanel.updatePlayerInfo(null);
            infoPanel.updateSelectionInfo("GAME OVER");
            controlPanel.getEndTurnButton().setEnabled(false);
            controlPanel.getBuildActionsButton().setEnabled(false);
            controlPanel.getUnitActionsButton().setEnabled(false);
            controlPanel.getOtherActionsButton().setEnabled(false);
        } else {
            infoPanel.updatePlayerInfo(currentPlayer);
            updateButtonStates();
        }
        mapPanel.refreshView();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainPanel::new);
    }
}