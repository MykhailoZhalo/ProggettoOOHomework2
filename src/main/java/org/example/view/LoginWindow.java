package org.example.view;

import org.example.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class LoginWindow {
    private JPanel panel1;
    private JTextField textUsername;
    private JPasswordField textPassword;
    private JButton loginButton;

    public LoginWindow() {
        panel1 = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // margini

        JLabel labelUsername = new JLabel("USERNAME:");
        labelUsername.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(labelUsername, gbc);

        textUsername = new JTextField();
        textUsername.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(textUsername, gbc);

        JLabel labelPassword = new JLabel("PASSWORD:");
        labelPassword.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel1.add(labelPassword, gbc);

        textPassword = new JPasswordField();
        textPassword.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(textPassword, gbc);

        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel1.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String user = textUsername.getText();
            String pass = new String(textPassword.getPassword());

            if (Controller.login(user, pass)) {
                JOptionPane.showMessageDialog(null, "Login riuscito!");
                Controller.showHome();
            } else {
                JOptionPane.showMessageDialog(null, "Credenziali errate.");
            }
        });
    }

    public JPanel getPanel() {
        return panel1;
    }
}
