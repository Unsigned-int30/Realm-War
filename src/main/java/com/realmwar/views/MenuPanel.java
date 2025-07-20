package com.realmwar.views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.Timer;
import java.awt.Component;

public class MenuPanel extends JPanel {
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem newGameItem;
    private JMenuItem loadGameItem;
    private JMenuItem saveGameItem;
    private JMenuItem exitItem;

    public MenuPanel() {
        // Set background color
        setBackground(new Color(30, 30, 30));

        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");

        newGameItem = createMenuItem("New Game");
        loadGameItem = createMenuItem("Load Game");
        saveGameItem = createMenuItem("Save Game");
        exitItem = createMenuItem("Exit");

        gameMenu.add(newGameItem);
        gameMenu.add(loadGameItem);
        gameMenu.add(saveGameItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        menuBar.add(gameMenu);
        add(menuBar);

        newGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement new game logic
                System.out.println("New Game selected");
            }
        });

        loadGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement load game logic
                System.out.println("Load Game selected");
            }
        });

        saveGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement save game logic
                System.out.println("Save Game selected");
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement exit logic
                System.exit(0);
            }
        });

        // Add a simple fade-in effect for the menu
        fadeInMenu();
    }

    private JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setForeground(Color.WHITE);
        item.setBackground(new Color(50, 50, 50));
        item.setOpaque(true);
        item.setBorderPainted(false);

        // Add hover effect
        item.addChangeListener(e -> {
            if (item.isArmed()) {
                item.setBackground(new Color(70, 70, 70));
            } else {
                item.setBackground(new Color(50, 50, 50));
            }
        });

        return item;
    }

    // Add a simple fade-in effect for the menu
    private void fadeInMenu() {
        Timer timer = new Timer(10, new ActionListener() {
            private float opacity = 0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity > 1f) {
                    opacity = 1f;
                    ((Timer) e.getSource()).stop();
                }
                setOpacity(opacity);
            }
        });
        timer.start();
    }

    private void setOpacity(float opacity) {
        for (Component component : menuBar.getComponents()) {
            component.setForeground(new Color(1f, 1f, 1f, opacity));
        }
    }
}
