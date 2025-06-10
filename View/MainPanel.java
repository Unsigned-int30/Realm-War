package View;

import Game.GameManager;
import Game.Player;
import View.ControlPanel;
import View.InfoPanel;
import View.MapPanel;
import Kingdom.Block.Block;
import Kingdom.Structure.Structure;
import Kingdom.Structure.Barrack;
import Kingdom.Structure.TownHall;
import Kingdom.Unit.Unit;
import View.GameMap;
import Utils.ResourceType;
import Utils.StructureType;
import Utils.ResourceYield;
import Utils.UnitType;

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

    public MainPanel() {
        try {
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
            frame.setLayout(new BorderLayout());

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
            System.err.println("An unexpected error occurred during application startup:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "A critical error occurred and the application must close.\nDetails: " + e.getMessage(),
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addListeners() {
        controlPanel.getEndTurnButton().addActionListener(e -> {
            gameManager.endTurn();
            selectedBlock = null;
            mapPanel.setSelectedBlock(null);
            updateUI();
        });

        controlPanel.getBuildFarmButton().addActionListener(e -> handleBuildStructure(StructureType.FARM));
        controlPanel.getBuildMarketButton().addActionListener(e -> handleBuildStructure(StructureType.MARKET));
        controlPanel.getBuildBarrackButton().addActionListener(e -> handleBuildStructure(StructureType.BARRACK));
        controlPanel.getBuildTowerButton().addActionListener(e -> handleBuildStructure(StructureType.TOWER));

        controlPanel.getProducePeasantButton().addActionListener(e -> handleProduceUnit(UnitType.PEASANT));
        controlPanel.getProduceSpearmanButton().addActionListener(e -> handleProduceUnit(UnitType.SPEARMAN));

        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / mapPanel.getCellSize();
                int y = e.getY() / mapPanel.getCellSize();
                if (gameMap.isValidCoordinate(x, y)) {
                    selectedBlock = gameMap.getBlockAt(x, y);
                    mapPanel.setSelectedBlock(selectedBlock);
                    updateSelectionInfoPanel();
                    updateButtonStates();
                }
            }
        });
    }

    private void handleBuildStructure(StructureType type) {
        if (selectedBlock == null) {
            JOptionPane.showMessageDialog(frame, "Please select a block first.", "Build Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        gameManager.tryBuildStructure(gameManager.getCurrentPlayer(), type, selectedBlock.getX(), selectedBlock.getY());
        updateUI();
    }

    private void handleProduceUnit(UnitType type) {
        if (selectedBlock == null || !selectedBlock.hasStructure()) {
            JOptionPane.showMessageDialog(frame, "Please select a production building first.", "Production Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        gameManager.tryProduceUnit(gameManager.getCurrentPlayer(), type, selectedBlock.getStructure());
        updateUI();
    }

    private void updateSelectionInfoPanel() {
        if (selectedBlock == null) {
            infoPanel.updateSelectionInfo("No selection.");
            return;
        }
        StringBuilder info = new StringBuilder();
        info.append("Block: (").append(selectedBlock.getX()).append(", ").append(selectedBlock.getY()).append(")\n");
        info.append("Type: ").append(selectedBlock.getClass().getSimpleName()).append("\n");
        if (selectedBlock.getOwner() != null) {
            info.append("Owner: ").append(selectedBlock.getOwner().getPlayerName()).append("\n");
        } else {
            info.append("Owner: None\n");
        }
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

    private void updateButtonStates() {
        boolean canProduce = false;
        if (selectedBlock != null && selectedBlock.hasStructure() && selectedBlock.getOwner() == gameManager.getCurrentPlayer()) {
            Structure s = selectedBlock.getStructure();
            if (s instanceof TownHall || s instanceof Barrack) {
                canProduce = true;
            }
        }
        controlPanel.getProducePeasantButton().setEnabled(canProduce);
        controlPanel.getProduceSpearmanButton().setEnabled(canProduce);
    }

    private void updateUI() {
        Player currentPlayer = gameManager.getCurrentPlayer();
        if (currentPlayer == null || gameManager.isGameOver()) {
            infoPanel.updatePlayerInfo(null);
            updateButtonStates();
            controlPanel.getEndTurnButton().setEnabled(false);
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
