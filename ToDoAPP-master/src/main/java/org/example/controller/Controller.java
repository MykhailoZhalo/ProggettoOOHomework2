package org.example.controller;

import org.example.dao.BachecaDAO;
import org.example.dao.ToDoDAO;
import org.example.dao.UtenteDAO;
import org.example.db.ConnessioneDatabase;
import org.example.model.Bacheca;
import org.example.model.ModelManager;
import org.example.model.TitoloBacheca;
import org.example.model.ToDo;
import org.example.view.*;

import javax.swing.*;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Controller {
    private static JFrame mainFrame;
    private static final UtenteDAO utenteDao = new UtenteDAO();
    public static String utenteLoggato = null;

    public static void startApp() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println("Errore nel settaggio LookAndFeel: " + e.getMessage());
        }

        mainFrame = new JFrame("ToDo App");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);
        showLogin();
        mainFrame.setVisible(true);
    }

    public static void setUtenteLoggato(String username) {
        utenteLoggato = username;
    }

    public static String getUtenteLoggato() {
        return utenteLoggato;
    }

    public static void showLogin() {
        mainFrame.setContentPane(new LoginWindow().getPanel());
        refresh();
    }

    public static boolean login(String username, String password) {
        if (utenteDao.checkLogin(username, password)) {
            setUtenteLoggato(username);

            try (Connection conn = ConnessioneDatabase.getConnection()) {
                BachecaDAO bachecaDAO = new BachecaDAO(conn);
                ToDoDAO todoDAO = new ToDoDAO(conn);

                List<Bacheca> bacheche = bachecaDAO.getBachechePerUtente(username);
                for (Bacheca b : bacheche) {
                    ModelManager.creaBacheca(b.getTitolo(), b.getDescrizione());
                    List<ToDo> todos = todoDAO.getToDosByBachecaAndUser(b.getTitolo().name(), username);
                    for (ToDo t : todos) {
                        ModelManager.aggiungiToDo(b.getTitolo(), t);
                    }
                }
            } catch (Exception e) {
                System.err.println("Errore nel caricamento dati utente: " + e.getMessage());
                return false;
            }

            return true;
        }
        return false;
    }

    public static void showBachecaWindow() {
        mainFrame.setContentPane(new BachecaWindow().getPanel());
        refresh();
    }

    public static boolean creaBacheca(TitoloBacheca titolo, String descrizione) {
        boolean creata = ModelManager.creaBacheca(titolo, descrizione);
        if (creata) {
            try (Connection conn = ConnessioneDatabase.getConnection()) {
                BachecaDAO dao = new BachecaDAO(conn);
                dao.inserisciSeNonEsiste(titolo, descrizione, utenteLoggato);
            } catch (Exception e) {
                System.err.println("Errore salvataggio bacheca: " + e.getMessage());
            }
        }
        return creata;
    }

    public static void showEditBachecaDialog(TitoloBacheca titolo) {
        String oldDescr = ModelManager.getBacheche().get(titolo).getDescrizione();
        String nuovaDescr = JOptionPane.showInputDialog(mainFrame, "Modifica descrizione per \u201c" + titolo + "\u201d:", oldDescr);
        if (nuovaDescr != null) {
            boolean ok = ModelManager.aggiornaDescrizione(titolo, nuovaDescr.trim());
            if (!ok) {
                JOptionPane.showMessageDialog(mainFrame, "Bacheca non trovata.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
        showBachecaWindow();
    }

    public static void showDeleteBachecaConfirmation(TitoloBacheca titolo) {
        int scelta = JOptionPane.showConfirmDialog(
                null,
                "Eliminare la bacheca \u201c" + titolo + "\u201d? Tutti i ToDo associati verranno eliminati.",
                "Conferma eliminazione",
                JOptionPane.YES_NO_OPTION
        );

        if (scelta == JOptionPane.YES_OPTION) {
            try (Connection conn = ConnessioneDatabase.getConnection()) {
                BachecaDAO bachecaDAO = new BachecaDAO(conn);
                boolean eliminata = bachecaDAO.elimina(titolo, utenteLoggato);

                if (eliminata) {
                    ModelManager.eliminaBacheca(titolo);
                    showBachecaWindow();
                    JOptionPane.showMessageDialog(null, "Bacheca eliminata con successo!");
                } else {
                    JOptionPane.showMessageDialog(null, "Bacheca non trovata o non eliminata.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Errore durante l'eliminazione: " + e.getMessage());
                e.printStackTrace();
            }
        }
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
            String immaginePath = view.getImmaginePathField().getText().trim();
            String coloreSfondo = view.getColoreSfondoField() != null ? view.getColoreSfondoField().getText().trim() : null;
            String username = getUtenteLoggato();

            try (Connection conn = ConnessioneDatabase.getConnection()) {
                ToDoDAO todoDAO = new ToDoDAO(conn);
                BachecaDAO bachecaDAO = new BachecaDAO(conn);
                bachecaDAO.inserisciSeNonEsiste(tipo, "Bacheca auto-creata", username);

                if (todoOriginal == null) {
                    ToDo newTodo = new ToDo(titolo, descrizione, scadenza);
                    newTodo.setBachecaTitolo(tipo);
                    if (!url.isEmpty()) newTodo.setUrl(url);
                    if (!immaginePath.isEmpty()) newTodo.setImmaginePath(immaginePath);
                    if (coloreSfondo != null && !coloreSfondo.isEmpty()) newTodo.setColoreSfondo(coloreSfondo);
                    newTodo.setAutore(username);

                    todoDAO.insert(newTodo, tipo.name(), username);
                    ModelManager.aggiungiToDo(tipo, newTodo);
                    view.showInfo("ToDo salvato con successo!");
                } else {
                    todoOriginal.setTitolo(titolo);
                    todoOriginal.setDescrizione(descrizione);
                    todoOriginal.setScadenza(scadenza);
                    todoOriginal.setUrl(url);
                    todoOriginal.setBachecaTitolo(tipo);
                    todoOriginal.setImmaginePath(immaginePath);
                    if (coloreSfondo != null) todoOriginal.setColoreSfondo(coloreSfondo);
                    todoOriginal.setAutore(username);

                    todoDAO.update(todoOriginal);
                    view.showInfo("ToDo aggiornato con successo!");
                }

            } catch (Exception ex) {
                view.showError("Errore durante salvataggio DB: " + ex.getMessage());
                ex.printStackTrace();
            }

            showBachecaWindow();
        });

        view.getAnnullaButton().addActionListener(e -> showBachecaWindow());
        refresh();
    }

    public static void eliminaToDo(TitoloBacheca tipo, ToDo todo) {
        try {
            ToDoDAO dao = new ToDoDAO(ConnessioneDatabase.getConnection());
            boolean success = dao.delete(todo, utenteLoggato);

            if (success) {
                ModelManager.rimuoviToDo(tipo, todo);
            } else {
                System.err.println("Nessuna riga eliminata dal DB.");
            }
        } catch (Exception e) {
            System.err.println("Errore durante eliminazione dal DB: " + e.getMessage());
        }
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

    public static List<String> getTuttiUtenti() {
        try {
            return utenteDao.getAllUsernamesExcept(utenteLoggato);
        } catch (Exception e) {
            System.err.println("Errore durante il recupero degli utenti: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
