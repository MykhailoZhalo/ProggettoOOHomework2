package org.example.view;

import org.example.controller.Controller;
import org.example.model.TitoloBacheca;

import javax.swing.*;
import java.awt.*;

/**
 * Finestra di dialogo per la creazione di una nuova bacheca da parte dell'utente.
 * Permette la selezione del tipo di bacheca e l'inserimento di una descrizione.
 */
public class NuovaBachecaDialog extends JDialog {

    /** Menù a discesa per selezionare il tipo di bacheca (Università, Lavoro, Tempo Libero) */
    private JComboBox<TitoloBacheca> comboTitolo;

    /** Campo di testo per inserire la descrizione della bacheca */
    private JTextField txtDescrizione;

    /** Bottone per confermare la creazione della bacheca */
    private JButton btnCrea;

    /** Bottone per annullare e chiudere il dialog */
    private JButton btnAnnulla;

    /**
     * Costruttore della finestra modale per la creazione di una nuova bacheca.
     *
     * @param parent     La finestra principale da cui è aperto il dialog
     * @param onSuccess  Callback da eseguire se la bacheca viene creata con successo
     */
    public NuovaBachecaDialog(JFrame parent, Runnable onSuccess) {
        super(parent, "Crea nuova Bacheca", true);
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("Tipo Bacheca:"));
        comboTitolo = new JComboBox<>(TitoloBacheca.values());
        add(comboTitolo);

        add(new JLabel("Descrizione:"));
        txtDescrizione = new JTextField();
        add(txtDescrizione);

        btnCrea = new JButton("Crea");
        btnAnnulla = new JButton("Annulla");
        add(btnCrea);
        add(btnAnnulla);

        // Listener per creare la bacheca

        btnCrea.addActionListener(e -> {
            TitoloBacheca titolo = (TitoloBacheca) comboTitolo.getSelectedItem();
            String descrizione = txtDescrizione.getText().trim();
            if (descrizione.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Inserisci una descrizione valida.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean ok = Controller.creaBacheca(titolo, descrizione);
            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "Hai già una bacheca di tipo “" + titolo + "”.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            onSuccess.run();
            dispose();
        });

        // Listener per chiudere la finestra senza salvare

        btnAnnulla.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }
}
