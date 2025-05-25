package org.example.view;

import org.example.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class HomeWindow {
    private JPanel panelHome;
    private JButton visualizzaBachecheButton;
    private JButton logoutButton;

    public HomeWindow() {
        panelHome = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 40, 20, 40); // margini interni

        visualizzaBachecheButton = new JButton("Visualizza Bacheche");
        visualizzaBachecheButton.setPreferredSize(new Dimension(200, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelHome.add(visualizzaBachecheButton, gbc);

        logoutButton = new JButton("Esci");
        logoutButton.setPreferredSize(new Dimension(200, 50));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 20, 40);
        panelHome.add(logoutButton, gbc);

        visualizzaBachecheButton.addActionListener(e -> Controller.showBachecaWindow());
        logoutButton.addActionListener(e -> Controller.showLogin());
    }

    public JPanel getPanel() {
        return panelHome;
    }
}
