package org.example.controller;

import org.example.model.Bacheca;
import org.example.model.ModelManager;
import org.example.model.TitoloBacheca;
import org.example.model.ToDo;
import org.example.view.*;

import javax.swing.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Controller {
    private static JFrame mainFrame;

    public static void startApp() {
        mainFrame = new JFrame("ToDo App");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);
        showLogin();
        mainFrame.setVisible(true);
    }

    public static void showLogin() {
        mainFrame.setContentPane(new LoginWindow().getPanel());
        refresh();
    }

    public static boolean login(String username, String password) {
        return username.equals("admin") && password.equals("1234");
    }

    public static void showBachecaWindow() {
        mainFrame.setContentPane(new BachecaWindow().getPanel());
        refresh();
    }

    public static boolean creaBacheca(TitoloBacheca titolo, String descrizione) {
        return ModelManager.creaBacheca(titolo, descrizione);
    }

    public static void showEditBachecaDialog(TitoloBacheca titolo) {
        String oldDescr = ModelManager.getBacheche().get(titolo).getDescrizione();
        String nuovaDescr = JOptionPane.showInputDialog(mainFrame, "Modifica descrizione per “" + titolo + "”:", oldDescr);
        if (nuovaDescr != null) {
            boolean ok = ModelManager.aggiornaDescrizione(titolo, nuovaDescr.trim());
            if (!ok) {
                JOptionPane.showMessageDialog(mainFrame, "Bacheca non trovata.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
        showBachecaWindow();
    }

    public static void showDeleteBachecaConfirmation(TitoloBacheca titolo) {
        int sc = JOptionPane.showConfirmDialog(mainFrame, "Eliminare la bacheca “" + titolo + "”? Tutti i ToDo andranno persi.", "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
        if (sc == JOptionPane.YES_OPTION) {
            boolean ok = ModelManager.eliminaBacheca(titolo);
            if (!ok) {
                JOptionPane.showMessageDialog(mainFrame, "Impossibile eliminare la bacheca.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
        showBachecaWindow();
    }

    public static Map<TitoloBacheca, Bacheca> getBacheche() {
        return ModelManager.getBacheche();
    }

    public static List<ToDo> getToDosByBacheca(TitoloBacheca titolo) {
        return ModelManager.getToDoPerBacheca(titolo);
    }

    public static void showToDoWindow(TitoloBacheca tipo) {
        ToDoFormWindow v = new ToDoFormWindow();
        initToDoFormController(v, tipo, null);
    }

    public static void showEditToDoWindow(TitoloBacheca tipo, ToDo todo) {
        ToDoFormWindow v = new ToDoFormWindow(todo);
        initToDoFormController(v, tipo, todo);
    }

    private static void initToDoFormController(ToDoFormWindow view, TitoloBacheca tipo, ToDo todoOriginal) {
        mainFrame.setContentPane(view.getPanel());
        mainFrame.getRootPane().setDefaultButton(view.getSalvaButton());

        view.getSalvaButton().addActionListener(e -> {
            String titolo = view.getTitoloField().getText().trim();
            String descrizione = view.getDescrizioneArea().getText().trim();
            String dataStr = view.getScadenzaField().getText().trim();

            if (titolo.isEmpty() || descrizione.isEmpty() || dataStr.isEmpty()) {
                view.showError("Compila tutti i campi obbligatori.");
                return;
            }

            Date scadenza;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                scadenza = sdf.parse(dataStr);
            } catch (ParseException ex) {
                view.showError("Formato data non valido (dd/MM/yyyy).");
                return;
            }

            String url = view.getUrlField().getText().trim();
            List<String> utenti = view.getUtentiList().getSelectedValuesList();
            String immaginePath = view.getImmaginePathField().getText().trim(); // ✅

            if (todoOriginal == null) {
                ToDo newTodo = new ToDo(titolo, descrizione, scadenza);
                if (!url.isEmpty()) newTodo.setURL(url);
                if (!utenti.isEmpty()) newTodo.setListaUtenti(utenti);
                if (!immaginePath.isEmpty()) newTodo.setImmaginePath(immaginePath);
                ModelManager.aggiungiToDo(tipo, newTodo);
                view.showInfo("ToDo creato con successo!");
            } else {
                todoOriginal.setTitolo(titolo);
                todoOriginal.setDescrizione(descrizione);
                todoOriginal.setScadenza(scadenza);
                todoOriginal.setURL(url);
                todoOriginal.setListaUtenti(utenti);
                todoOriginal.setImmaginePath(immaginePath);
                view.showInfo("ToDo aggiornato con successo!");
            }

            showBachecaWindow();
        });

        view.getAnnullaButton().addActionListener(e -> showBachecaWindow());
        refresh();
    }

    public static void eliminaToDo(TitoloBacheca tipo, ToDo todo) {
        ModelManager.rimuoviToDo(tipo, todo);
    }

    public static void spostaToDo(TitoloBacheca titolo, ToDo todo, int delta) {
        List<ToDo> lista = ModelManager.getToDoPerBacheca(titolo);
        int currentIndex = lista.indexOf(todo);
        int newIndex = currentIndex + delta;

        if (newIndex >= 0 && newIndex < lista.size()) {
            Collections.swap(lista, currentIndex, newIndex);
            riallineaPosizioni(lista);
        }
    }

    public static void spostaToDoInAltraBacheca(TitoloBacheca origine, TitoloBacheca destinazione, ToDo todo) {
        ModelManager.rimuoviToDo(origine, todo);
        ModelManager.aggiungiToDo(destinazione, todo);
    }

    private static void riallineaPosizioni(List<ToDo> lista) {
        for (int i = 0; i < lista.size(); i++) {
            lista.get(i).setPosizione(i);
        }
    }

    private static void refresh() {
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}
