package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ControlPanel extends JPanel {
    private JButton endTurnButton;
    private JButton buildActionsButton;
    private JButton unitActionsButton;
    private JButton otherActionsButton;
    private JButton saveButton;
    private JButton loadButton;
    private final List<JButton> allButtons = new ArrayList<>();

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        endTurnButton = createButton("End Turn", new Dimension(120, 40));
        buildActionsButton = createButton("Build...", new Dimension(140, 40));
        unitActionsButton = createButton("Unit Actions...", new Dimension(140, 40));
        otherActionsButton = createButton("Other Actions...", new Dimension(140, 40));
        saveButton = createButton("Save Game", new Dimension(120, 40));
        loadButton = createButton("Load Game", new Dimension(120, 40));

        add(endTurnButton);
        add(buildActionsButton);
        add(unitActionsButton);
        add(otherActionsButton);
        add(saveButton);
        add(loadButton);
    }

    private JButton createButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        allButtons.add(button);
        return button;
    }

    public void setAllButtonsEnabled(boolean enabled) {
        for (JButton button : allButtons) {
            button.setEnabled(enabled);
        }
    }

    public JButton getEndTurnButton() { return endTurnButton; }
    public JButton getBuildActionsButton() { return buildActionsButton; }
    public JButton getUnitActionsButton() { return unitActionsButton; }
    public JButton getOtherActionsButton() { return otherActionsButton; }
    public JButton getSaveButton() { return saveButton; }
    public JButton getLoadButton() { return loadButton; }
}