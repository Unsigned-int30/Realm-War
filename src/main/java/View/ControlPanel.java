package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        setBackground(new Color(220, 215, 215));

        endTurnButton = createStyledButton("End Turn");
        buildActionsButton = createStyledButton("Build...");
        unitActionsButton = createStyledButton("Unit Actions...");
        otherActionsButton = createStyledButton("Other Actions...");
        saveButton = createStyledButton("Save Game");
        loadButton = createStyledButton("Load Game");

        add(endTurnButton);
        add(buildActionsButton);
        add(unitActionsButton);
        add(otherActionsButton);
        add(saveButton);
        add(loadButton);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        Color normalColor = new Color(70, 70, 80);
        Color hoverColor = new Color(100, 100, 110); // وقتی موس روی دکمه است

        button.setPreferredSize(new Dimension(140, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(normalColor);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });

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