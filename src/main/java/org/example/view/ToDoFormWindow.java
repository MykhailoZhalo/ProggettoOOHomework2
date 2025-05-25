package org.example.view;

import org.example.model.ToDo;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ToDoFormWindow {
    private JPanel panelToDo;
    private JTextField titoloField;
    private JTextArea descrizioneArea;
    private JFormattedTextField scadenzaField;
    private JTextField posizioneField;
    private JTextField urlField;
    private JList<String> utentiList;
    private JButton salvaButton;
    private JButton annullaButton;

    private ToDo todoToEdit;

    public ToDoFormWindow() {
        setupUI();
    }

    public ToDoFormWindow(ToDo todo) {
        this();
        this.todoToEdit = todo;
        salvaButton.setText("Aggiorna");
        titoloField.setText(todo.getTitolo());
        descrizioneArea.setText(todo.getDescrizione());
        scadenzaField.setText(new SimpleDateFormat("dd/MM/yyyy").format(todo.getScadenza()));
        posizioneField.setText(String.valueOf(todo.getPosizione()));
        urlField.setText(todo.getURL() != null ? todo.getURL() : "");
        List<String> utenti = todo.getListaUtenti();
        utentiList.setListData(utenti != null ? utenti.toArray(new String[0]) : new String[0]);
    }

    private void setupUI() {
        panelToDo = new JPanel(new GridBagLayout());
        panelToDo.setBackground(new Color(0xEAEAEA)); // colore chiaro di sfondo

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        addLabel("Titolo*:", y);
        titoloField = new JTextField(20);
        addComponent(titoloField, y++);

        addLabel("Data scadenza*:", y);
        scadenzaField = new JFormattedTextField();
        scadenzaField.setColumns(20);
        addComponent(scadenzaField, y++);

        addLabel("Posizione*:", y);
        posizioneField = new JTextField(20);
        addComponent(posizioneField, y++);

        addLabel("URL:", y);
        urlField = new JTextField(20);
        addComponent(urlField, y++);

        addLabel("Lista utenti:", y);
        utentiList = new JList<>();
        JScrollPane utentiScroll = new JScrollPane(utentiList);
        utentiScroll.setPreferredSize(new Dimension(200, 60));
        gbc.gridx = 1;
        gbc.gridy = y++;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.BOTH;
        panelToDo.add(utentiScroll, gbc);

        addLabel("Descrizione*:", y);
        descrizioneArea = new JTextArea(4, 20);
        JScrollPane descrizioneScroll = new JScrollPane(descrizioneArea);
        gbc.gridx = 1;
        gbc.gridy = y++;
        gbc.weighty = 0.3;
        panelToDo.add(descrizioneScroll, gbc);

        salvaButton = new JButton("Salva");
        annullaButton = new JButton("Annulla");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(annullaButton);
        buttonPanel.add(salvaButton);
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        gbc.weighty = 0;
        panelToDo.add(buttonPanel, gbc);
    }

    private void addLabel(String text, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        panelToDo.add(new JLabel(text), gbc);
    }

    private void addComponent(JComponent component, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 10, 5, 10);
        panelToDo.add(component, gbc);
    }

    public JPanel getPanel() {
        return panelToDo;
    }

    public ToDo getTodoToEdit() {
        return todoToEdit;
    }

    public JTextField getTitoloField() {
        return titoloField;
    }

    public JTextArea getDescrizioneArea() {
        return descrizioneArea;
    }

    public JFormattedTextField getScadenzaField() {
        return scadenzaField;
    }

    public JTextField getPosizioneField() {
        return posizioneField;
    }

    public JTextField getUrlField() {
        return urlField;
    }

    public JList<String> getUtentiList() {
        return utentiList;
    }

    public JButton getSalvaButton() {
        return salvaButton;
    }

    public JButton getAnnullaButton() {
        return annullaButton;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(panelToDo, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(panelToDo, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
