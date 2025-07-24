package org.example.view;

import org.example.controller.Controller;
import org.example.dao.UtenteDAO;
import org.example.interfaceDAO.UtenteDAOInterface;

import javax.swing.*;

public class LoginWindow {

    /** Pannello principale del login */
    private JPanel panel;

    /** Campo di inserimento per il nome utente */
    private JTextField textUsername;

    /** Campo di inserimento per la password (offuscata) */
    private JPasswordField textPassword;

    /** Checkbox per mostrare o nascondere la password */
    private JCheckBox mostraPasswordCheckBox;

    /** Bottone per effettuare il login */
    private JButton loginButton;

    /** Bottone per registrare un nuovo utente */
    private JButton registratiButton;



    /**
     * Costruttore della finestra di login.
     * Imposta gli action listener per login, registrazione e visualizzazione della password.
     */
    public LoginWindow() {
        loginButton.addActionListener(e -> {
            String user = textUsername.getText().trim();
            String pass = new String(textPassword.getPassword()).trim();
            if (Controller.login(user, pass)) {
                JOptionPane.showMessageDialog(null, "Login riuscito!");
                Controller.showBachecaWindow();
                SwingUtilities.invokeLater(Controller::mostraDialogRichieste);
            } else {
                JOptionPane.showMessageDialog(null, "Credenziali errate.");
            }
        });

        mostraPasswordCheckBox.addActionListener(e -> {
            textPassword.setEchoChar(mostraPasswordCheckBox.isSelected() ? (char) 0 : '•');
        });

        registratiButton.addActionListener(e -> {
            String user = textUsername.getText().trim();
            String pass = new String(textPassword.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Inserisci username e password.");
                return;
            }
            UtenteDAOInterface utenteDAO = new UtenteDAO();
            if (utenteDAO.registraUtente(user, pass)) {
                JOptionPane.showMessageDialog(null, "Registrazione avvenuta con successo!");
            } else {
                JOptionPane.showMessageDialog(null, "Errore nella registrazione. Utente già esistente?");
            }
        });
    }
    /**
     * Restituisce il pannello principale della finestra di login,
     * da usare per mostrare il contenuto nella finestra principale.
     *
     * @return il pannello di login
     */
    public JPanel getPanel() {
        return panel;
    }
}
