package org.example.model;

import com.formdev.flatlaf.FlatLightLaf;
import org.example.controller.Controller;

import javax.swing.*;

/**
 * Classe principale per l'avvio dell'applicazione ToDo.
 * Imposta il tema grafico (LookAndFeel) e avvia il controller principale.
 */
public class Main {

    /**
     * Metodo principale (entry point) dell'applicazione.
     * Imposta il tema grafico FlatLaf e richiama l'inizializzazione dell'app.
     *
     * @param args Argomenti da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // Imposta il tema chiaro
        } catch (Exception e) {
            e.printStackTrace(); // Stampa errore se il tema non viene caricato
        }

        // Avvia l'applicazione richiamando il controller
        Controller.startApp();
    }
}
