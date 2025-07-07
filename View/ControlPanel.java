package View;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ControlPanel extends JPanel {
    private JButton endTurnButton;
    private JButton buildButton;
    private JButton produceUnitButton;

    private JButton buildActionsButton;
    private JButton unitActionsButton;
    private JButton otherActionsButton;


    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        endTurnButton = new JButton("End Turn");
        buildActionsButton = new JButton("Build Structure...");
        unitActionsButton = new JButton("Unit Actions...");
        otherActionsButton = new JButton("Other Actions...");

        endTurnButton.setPreferredSize(new Dimension(120, 40));
        buildActionsButton.setPreferredSize(new Dimension(150, 40));
        unitActionsButton.setPreferredSize(new Dimension(150, 40));
        otherActionsButton.setPreferredSize(new Dimension(150, 40));

        add(endTurnButton);
        add(buildActionsButton);
        add(unitActionsButton);
        add(otherActionsButton);
    }

    public JButton getEndTurnButton() { return endTurnButton; }
    public JButton getBuildActionsButton() { return buildActionsButton; }
    public JButton getUnitActionsButton() { return unitActionsButton; }
    public JButton getOtherActionsButton() { return otherActionsButton; }
}