package org.example.view;

import org.example.controller.Controller;
import org.example.model.Bacheca;
import org.example.model.ToDo;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Collection;

public class BachecaWindow {
    private JPanel mainPanel;
    private JPanel bachechePanel;
    private JButton homeButton;
    private JButton createBachecaButton;

    public BachecaWindow() {
        setupUI();
        setupActions();
        aggiornaListaBacheche();
    }

    private void setupActions() {
        homeButton.addActionListener(e -> Controller.showHome());

        createBachecaButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            NuovaBachecaDialog dialog = new NuovaBachecaDialog(parentFrame, this::aggiornaListaBacheche);
            dialog.setVisible(true);
        });
    }

    private void aggiornaListaBacheche() {
        bachechePanel.removeAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Collection<Bacheca> bacheche = Controller.getBacheche().values();

        for (Bacheca b : bacheche) {
            JPanel bachecaBox = new JPanel();
            bachecaBox.setLayout(new BoxLayout(bachecaBox, BoxLayout.Y_AXIS));
            bachecaBox.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    b.getTitolo().name() + " - " + b.getDescrizione(),
                    TitledBorder.LEFT,
                    TitledBorder.TOP
            ));

            JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton editBachecaButton = new JButton("Modifica");
            editBachecaButton.setToolTipText("Modifica descrizione bacheca");
            editBachecaButton.addActionListener(e -> Controller.showEditBachecaDialog(b.getTitolo()));
            header.add(editBachecaButton);

            JButton deleteBachecaButton = new JButton("Elimina");
            deleteBachecaButton.setToolTipText("Elimina bacheca");
            deleteBachecaButton.addActionListener(e -> Controller.showDeleteBachecaConfirmation(b.getTitolo()));
            header.add(deleteBachecaButton);

            bachecaBox.add(header);

            for (ToDo todo : b.getToDoList()) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));

                JCheckBox checkBox = new JCheckBox(todo.getTitolo() + " - Scadenza: " + sdf.format(todo.getScadenza()));
                checkBox.setSelected(todo.isCompletato());
                checkBox.addActionListener(e -> todo.setCompletato(checkBox.isSelected()));
                row.add(checkBox);

                JButton editToDoButton = new JButton("Modifica");
                editToDoButton.setToolTipText("Modifica questo ToDo");
                editToDoButton.addActionListener(e -> Controller.showEditToDoWindow(b.getTitolo(), todo));
                row.add(editToDoButton);

                JButton deleteToDoButton = new JButton("Elimina");
                deleteToDoButton.setToolTipText("Elimina questo ToDo");
                deleteToDoButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            mainPanel,
                            "Eliminare il ToDo '" + todo.getTitolo() + "'?",
                            "Conferma eliminazione",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        Controller.eliminaToDo(b.getTitolo(), todo);
                        aggiornaListaBacheche();
                    }
                });
                row.add(deleteToDoButton);

                bachecaBox.add(row);
            }

            JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton addToDoButton = new JButton(" Aggiungi ToDo");
            addToDoButton.addActionListener(e -> Controller.showToDoWindow(b.getTitolo()));
            addPanel.add(addToDoButton);

            bachecaBox.add(addPanel);
            bachechePanel.add(bachecaBox);
        }

        bachechePanel.revalidate();
        bachechePanel.repaint();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    private void setupUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        createBachecaButton = new JButton("Crea Bacheca");
        homeButton = new JButton("Home");
        topPanel.add(createBachecaButton);
        topPanel.add(homeButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        bachechePanel = new JPanel();
        bachechePanel.setLayout(new BoxLayout(bachechePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(bachechePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }
}
