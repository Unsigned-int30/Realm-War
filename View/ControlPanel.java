package View;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ControlPanel extends JPanel {
    private JButton endTurnButton;
    private JButton buildFarmButton;
    private JButton buildMarketButton;
    private JButton buildBarrackButton;
    private JButton buildTowerButton;

    private JButton producePeasantButton;
    private JButton produceSpearmanButton;

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));


        JPanel generalActionsPanel = new JPanel();
        endTurnButton = new JButton("End Turn");
        endTurnButton.setPreferredSize(new Dimension(120, 40));
        generalActionsPanel.add(endTurnButton);


        JPanel buildActionsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buildActionsPanel.setBorder(new TitledBorder("Build Actions"));

        buildFarmButton = new JButton("Build Farm");
        buildMarketButton = new JButton("Build Market");
        buildBarrackButton = new JButton("Build Barrack");
        buildTowerButton = new JButton("Build Tower");

        buildActionsPanel.add(buildFarmButton);
        buildActionsPanel.add(buildMarketButton);
        buildActionsPanel.add(buildBarrackButton);
        buildActionsPanel.add(buildTowerButton);


        JPanel unitActionsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        unitActionsPanel.setBorder(new TitledBorder("Unit Actions"));

        producePeasantButton = new JButton("Produce Peasant");
        produceSpearmanButton = new JButton("Produce Spearman");

        unitActionsPanel.add(producePeasantButton);
        unitActionsPanel.add(produceSpearmanButton);

        add(generalActionsPanel);
        add(buildActionsPanel);
        add(unitActionsPanel);
    }

    public JButton getEndTurnButton() { return endTurnButton; }
    public JButton getBuildFarmButton() { return buildFarmButton; }
    public JButton getBuildMarketButton() { return buildMarketButton; }
    public JButton getBuildBarrackButton() { return buildBarrackButton; }
    public JButton getBuildTowerButton() { return buildTowerButton; }
    public JButton getProducePeasantButton() { return producePeasantButton; }
    public JButton getProduceSpearmanButton() { return produceSpearmanButton; }
}