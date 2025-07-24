package org.example.view;

import org.example.model.ToDo;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Finestra per l'inserimento o la modifica di un ToDo.
 * Permette all'utente di compilare titolo, descrizione, data di scadenza,
 * URL, immagine e colore di sfondo.
 */
public class ToDoFormWindow {

    /** Pannello principale che contiene tutti i componenti del form */
    private JPanel panelToDo;

    /** Campo di testo per il titolo del ToDo */
    private JTextField titoloField;

    /** Area di testo per la descrizione del ToDo */
    private JTextArea descrizioneArea;

    /** Campo di testo per la data di scadenza del ToDo */
    private JTextField scadenzaField;

    /** Bottone per salvare il ToDo */
    private JButton salvaButton;

    /** Bottone per annullare e tornare indietro */
    private JButton annullaButton;

    /** Campo di testo per inserire un URL associato al ToDo */
    private JTextField urlField;

    /** Campo di testo per il percorso dell'immagine */
    private JTextField immaginePathField; // nuovo campo per il path

    /** Bottone per aprire un selettore di file per l'immagine */
    private JButton sfogliaButton;         // pulsante per selezionare immagine

    /** Campo di testo per il colore di sfondo (in formato esadecimale) */
    private JTextField coloreField;

    /** Bottone per aprire un selettore di colore */
    private JButton coloreButton;

    /**
     * Costruttore per creare un nuovo ToDo da zero.
     */
    public ToDoFormWindow() {
        setupUI();
    }

    /**
     * Costruttore che inizializza il form con i dati di un ToDo esistente.
     *
     * @param todo Il ToDo da modificare
     */
    public ToDoFormWindow(ToDo todo) {
        setupUI();
        panelToDo.setBackground(new Color(245, 245, 250)); // colore sfondo chiaro
        titoloField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descrizioneArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        salvaButton.setFocusPainted(false);

            coloreButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(panelToDo, "Scegli un colore", Color.WHITE);
            if (selectedColor != null) {
                String hexColor = String.format("#%06X", (0xFFFFFF & selectedColor.getRGB()));
                coloreField.setText(hexColor);
            }
        });
        if (todo != null) {
            titoloField.setText(todo.getTitolo());
            descrizioneArea.setText(todo.getDescrizione());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            scadenzaField.setText(sdf.format(todo.getScadenza()));
            urlField.setText(todo.getUrl());
            if (todo.getImmaginePath() != null) {
                immaginePathField.setText(todo.getImmaginePath());
            }

            coloreField.setText(todo.getColoreSfondo() != null ? todo.getColoreSfondo() : "");
        }
    }

    /**
     * Inizializza i componenti grafici e registra i listener per pulsanti e campi.
     */
    private void setupUI() {

        coloreButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(panelToDo, "Scegli un colore", Color.WHITE);
            if (selectedColor != null) {
                String hexColor = String.format("#%06X", (0xFFFFFF & selectedColor.getRGB()));
                coloreField.setText(hexColor);
            }
        });
        sfogliaButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(panelToDo);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                immaginePathField.setText(selectedFile.getAbsolutePath());
            }
        });
    }
    /**
     * Restituisce il pannello principale del form.
     *
     * @return JPanel principale
     */
    public JPanel getPanel() {
        return panelToDo;
    }

    /** @return campo per il colore di sfondo */
    public JTextField getColoreSfondoField() {
        return coloreField;
    }


    /** @return campo di testo per il titolo */
    public JTextField getTitoloField() {
        return titoloField;
    }

    /** @return area di testo per la descrizione */
    public JTextArea getDescrizioneArea() {
        return descrizioneArea;
    }

    /** @return campo di testo per la data di scadenza */
    public JTextField getScadenzaField() {
        return scadenzaField;
    }

    /** @return bottone per salvare */
    public JButton getSalvaButton() {
        return salvaButton;
    }

    /** @return bottone per annullare */
    public JButton getAnnullaButton() {
        return annullaButton;
    }

    /** @return campo di testo per l'URL */
    public JTextField getUrlField() {
        return urlField;
    }


    /** @return campo di testo per il path dell'immagine */
    public JTextField getImmaginePathField() {
        return immaginePathField;
    }

    /**
     * Mostra un messaggio di errore all'utente.
     *
     * @param message Testo del messaggio
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(panelToDo, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Mostra un messaggio informativo all'utente.
     *
     * @param message Testo del messaggio
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(panelToDo, message, "Informazione", JOptionPane.INFORMATION_MESSAGE);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelToDo = new JPanel();
        panelToDo.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(9, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelToDo.setBackground(new Color(-1641729));
        descrizioneArea = new JTextArea();
        panelToDo.add(descrizioneArea, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 34), null, 0, false));
        urlField = new JTextField();
        panelToDo.add(urlField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Titolo*:");
        panelToDo.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Data scadenza*:");
        panelToDo.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("URL:");
        panelToDo.add(label3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        salvaButton = new JButton();
        salvaButton.setText("Salva");
        panelToDo.add(salvaButton, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Descrizione*:");
        panelToDo.add(label4, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(82, 34), null, 0, false));
        titoloField = new JTextField();
        panelToDo.add(titoloField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        annullaButton = new JButton();
        annullaButton.setText("Annulla");
        panelToDo.add(annullaButton, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scadenzaField = new JFormattedTextField();
        panelToDo.add(scadenzaField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        immaginePathField = new JTextField();
        panelToDo.add(immaginePathField, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        sfogliaButton = new JButton();
        sfogliaButton.setText("Inserisci la foto");
        panelToDo.add(sfogliaButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        coloreField = new JTextField();
        coloreField.setText("");
        panelToDo.add(coloreField, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        coloreButton = new JButton();
        coloreButton.setText("Inserisci colore sfondo");
        panelToDo.add(coloreButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelToDo;
    }


}
