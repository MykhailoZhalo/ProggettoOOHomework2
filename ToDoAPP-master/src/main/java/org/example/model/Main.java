package org.example.model;
import javax.swing.UIManager;



import org.example.controller.Controller;


public class Main {


    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println("Errore nel settaggio LookAndFeel: " + e.getMessage());
        }
        Controller.startApp();
    }
}

