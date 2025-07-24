package org.example.controller;

import org.example.dao.*;
import org.example.db.ConnessioneDatabase;
import org.example.interfaceDAO.BachecaDAOInterface;
import org.example.interfaceDAO.RichiestaDAOInterface;
import org.example.interfaceDAO.ToDoDAOInterface;
import org.example.model.Bacheca;
import org.example.model.ModelManager;
import org.example.model.TitoloBacheca;
import org.example.model.ToDo;
import org.example.model.Richiesta;
import org.example.view.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


/**
 * La classe Controller coordina la logica dell'applicazione ToDo.
 * Gestisce il ciclo di vita dell'interfaccia, il login, le operazioni sulle bacheche,
 * la condivisione dei ToDo, e interazioni con il database.
 */
public class Controller {
    private static JFrame mainFrame;
    private static final UtenteDAO utenteDao = new UtenteDAO();
    public static String utenteLoggato = null;
    private static RichiestaDAOInterface richiestaDAO;

    /**
     * Avvia l'applicazione, imposta il Look & Feel e mostra la finestra di login.
     */

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

    /**
     * Imposta il nome utente attualmente loggato.
     *
     * @param username Nome utente loggato
     */
    public static void setUtenteLoggato(String username) {
        utenteLoggato = username;
    }
    /**
     * Restituisce il nome dell'utente attualmente loggato.
     *
     * @return Nome utente loggato
     */
    public static String getUtenteLoggato() {
        return utenteLoggato;
    }
    /**
     * Mostra la schermata di login all'utente.
     * Sostituisce il contenuto principale della finestra con il pannello di login.
     */
    public static void showLogin() {
        mainFrame.setContentPane(new LoginWindow().getPanel());
        refresh();
    }




    /**
     * Esegue il processo di login per l'utente specificato.
     * <p>
     * Se le credenziali sono corrette:
     * <ul>
     *     <li>Imposta l'utente come loggato</li>
     *     <li>Recupera le bacheche personali e i ToDo associati</li>
     *     <li>Recupera i ToDo condivisi con l'utente</li>
     *     <li>Gestisce eventuali richieste di condivisione pendenti</li>
     * </ul>
     * In caso contrario, il login fallisce.
     *
     * @param username Nome utente da autenticare
     * @param password Password associata all'utente
     * @return true se il login è avvenuto con successo, false altrimenti
     */

    public static boolean login(String username, String password) {
        if (utenteDao.checkLogin(username, password)) {
            setUtenteLoggato(username);
            ModelManager.reset();

            try (Connection conn = ConnessioneDatabase.getConnection()) {
                BachecaDAO bachecaDAO = new BachecaDAO(conn);
                ToDoDAO todoDAO = new ToDoDAO(conn);
                richiestaDAO = new RichiestaDAO(conn);

                List<Bacheca> bacheche = bachecaDAO.getBachechePerUtente(username);
                for (Bacheca b : bacheche) {
                    ModelManager.creaBacheca(b.getTitolo(), b.getDescrizione());
                    List<ToDo> todos = todoDAO.getToDosByBachecaAndUser(b.getTitolo().name(), username);
                    for (ToDo t : todos) {
                        ModelManager.aggiungiToDo(b.getTitolo(), t);
                    }
                }

                List<ToDo> condivisi = todoDAO.getToDosCondivisiConUtente(username);
                for (ToDo t : condivisi) {
                    TitoloBacheca titolo = t.getBachecaTitolo();
                    if (!ModelManager.getBacheche().containsKey(titolo)) {
                        ModelManager.creaBacheca(titolo, "Bacheca condivisa");
                    }
                    ModelManager.aggiungiToDo(titolo, t);
                }

                List<Richiesta> richieste = richiestaDAO.getRichiestePerUtente(username);
                for (Richiesta richiesta : richieste) {
                    ToDo todoRichiesto = todoDAO.getToDoById(richiesta.getIdToDo());
                    if (todoRichiesto == null) continue;

                    int scelta = JOptionPane.showConfirmDialog(
                            null,
                            "Vuoi accettare il ToDo condiviso da " + richiesta.getMittente() +
                                    " nella bacheca '" + richiesta.getTitoloBacheca() + "'?\nTitolo: " + todoRichiesto.getTitolo(),
                            "Richiesta di condivisione",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (scelta == JOptionPane.YES_OPTION) {
                        richiestaDAO.aggiornaStato(richiesta.getId(), "accettata");

                        TitoloBacheca titoloBacheca = TitoloBacheca.valueOf(richiesta.getTitoloBacheca());

                        if (!ModelManager.getBacheche().containsKey(titoloBacheca)) {
                            ModelManager.creaBacheca(titoloBacheca, "Bacheca condivisa");
                            bachecaDAO.inserisciSeNonEsiste(titoloBacheca, "Bacheca condivisa", username);
                        }

                        todoRichiesto.setUsername(username);
                        todoRichiesto.setBachecaTitolo(titoloBacheca);

                        todoRichiesto.setAutore(richiesta.getMittente());

                        todoDAO.insert(todoRichiesto, titoloBacheca.name(), username);

                        ModelManager.aggiungiToDo(titoloBacheca, todoRichiesto);
                        RichiestaDAO condivDao = new RichiestaDAO(conn);
                        condivDao.inserisciCondivisione(todoRichiesto.getId(), username);

                    } else {
                        richiestaDAO.aggiornaStato(richiesta.getId(), "rifiutata");
                    }
                }

            } catch (Exception e) {
                System.err.println("Errore nel login: " + e.getMessage());
                return false;
            }


            return true;
        }

        return false;
    }
    /**
     * Gestisce la visualizzazione e la modifica degli utenti con cui è condiviso un determinato ToDo.
     * <p>
     * Se l'utente loggato è l'autore del ToDo:
     * <ul>
     *     <li>Mostra un elenco degli utenti con cui è stato condiviso</li>
     *     <li>Permette di rimuovere la condivisione con uno di essi</li>
     *     <li>Aggiorna lo stato della richiesta nel database e la lista interna degli utenti</li>
     * </ul>
     * Se l'utente loggato non è l'autore:
     * <ul>
     *     <li>Mostra un messaggio con l'elenco degli utenti con cui il ToDo è condiviso (escludendo il visualizzatore)</li>
     * </ul>
     *
     * @param todo             Oggetto ToDo di cui gestire le condivisioni
     * @param parentComponent  Componente padre per i dialog (es. la finestra attiva), usato per ancorare le finestre modali
     */

    public static void gestisciUtentiCondivisi(ToDo todo, Component parentComponent) {
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            RichiestaDAOInterface richiestaDAO = new RichiestaDAO(conn);
            ToDoDAOInterface todoDAO = new ToDoDAO(conn);

            List<String> utenti = richiestaDAO.getUtentiCondivisi(todo.getTitolo(), todo.getAutore());
            String autore = todo.getAutore();
            String username = getUtenteLoggato();

            if (username.equals(autore)) {
                if (utenti.isEmpty()) {
                    JOptionPane.showMessageDialog(parentComponent, "Non hai condiviso questo ToDo con nessuno.");
                    return;
                }

                JList<String> listaUtenti = new JList<>(utenti.toArray(new String[0]));
                listaUtenti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                JScrollPane scroll = new JScrollPane(listaUtenti);
                scroll.setPreferredSize(new Dimension(300, 150));

                int scelta = JOptionPane.showConfirmDialog(
                        parentComponent,
                        scroll,
                        "Rimuovi un utente dalla condivisione",
                        JOptionPane.OK_CANCEL_OPTION
                );

                if (scelta == JOptionPane.OK_OPTION) {
                    String utenteSelezionato = listaUtenti.getSelectedValue();
                    if (utenteSelezionato != null) {
                        boolean ok = todoDAO.rimuoviPartecipazione(todo.getTitolo(), todo.getAutore(), utenteSelezionato);

                        if (ok) {
                            richiestaDAO.aggiornaStatoRichiesta(todo.getId(), utenteSelezionato, "rifiutata");

                            List<String> lista = todo.getListaUtenti();
                            if (lista != null) {
                                lista.remove(utenteSelezionato);
                                todo.setListaUtenti(lista);
                                todoDAO.aggiornaListaUtenti(todo);
                            }

                            JOptionPane.showMessageDialog(parentComponent, "Utente rimosso dalla condivisione.");
                        } else {
                            JOptionPane.showMessageDialog(parentComponent, "Nessuna riga eliminata nel DB.");
                        }
                    }
                }

            } else {
                utenti.remove(username);
                if (!utenti.contains(autore)) {
                    utenti.add(autore);
                }

                String elenco = utenti.isEmpty()
                        ? "Nessun altro utente con cui è condiviso."
                        : String.join("\n", utenti);

                JOptionPane.showMessageDialog(parentComponent, "Condiviso con:\n" + elenco);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentComponent, "Errore durante il recupero utenti: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Invia una richiesta di condivisione di un ToDo a un altro utente.
     * Solo l'autore del ToDo può inviare la richiesta.
     *
     * @param todo         Oggetto ToDo da condividere
     * @param destinatario Username dell'utente destinatario della richiesta
     */


    public static void inviaRichiestaCondivisione(ToDo todo, String destinatario) {
        if (!Controller.getUtenteLoggato().equals(todo.getAutore())) {
            JOptionPane.showMessageDialog(null, "Solo l'autore può condividere questo ToDo.");
            return;
        }
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            RichiestaDAOInterface richiestaDAO = new RichiestaDAO(conn);

            ToDoDAOInterface todoDAO = new ToDoDAO(conn);

            if (todoDAO.esisteToDoCondiviso(todo.getTitolo(), todo.getAutore(), destinatario)) {
                JOptionPane.showMessageDialog(null, "Hai già condiviso questo ToDo con " + destinatario);
                return;
            }

            if (richiestaDAO.esisteRichiesta(todo.getId(), Controller.getUtenteLoggato(), destinatario)) {
                JOptionPane.showMessageDialog(null, "Hai già inviato una richiesta a questo utente per questo ToDo.");
                return;
            }

            Richiesta richiesta = new Richiesta(
                    todo.getId(),
                    Controller.getUtenteLoggato(),
                    destinatario,
                    todo.getBachecaTitolo().name()
            );

            richiestaDAO.inserisciRichiesta(richiesta);
            JOptionPane.showMessageDialog(null, "Richiesta inviata a " + destinatario);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Errore durante l'invio richiesta: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Mostra la finestra principale dell'applicazione contenente le bacheche e i ToDo.
     * Sostituisce il contenuto attuale del frame principale con il pannello BachecaWindow.
     */


    public static void showBachecaWindow() {
        mainFrame.setContentPane(new BachecaWindow().getPanel());
        refresh();
    }
    /**
     * Crea una nuova bacheca nel modello e la salva nel database se non già esistente.
     *
     * @param titolo      Titolo della bacheca (enum TitoloBacheca)
     * @param descrizione Descrizione della bacheca
     * @return true se la bacheca è stata creata correttamente, false se già esistente
     */

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
    /**
     * Mostra una finestra di dialogo per modificare la descrizione di una bacheca esistente.
     * Aggiorna sia il modello interno che il database, se confermato.
     *
     * @param titolo Titolo della bacheca da modificare
     */

    public static void showEditBachecaDialog(TitoloBacheca titolo) {
        String oldDescr = ModelManager.getBacheche().get(titolo).getDescrizione();
        String nuovaDescr = JOptionPane.showInputDialog(mainFrame, "Modifica descrizione per “" + titolo + "”:", oldDescr);

        if (nuovaDescr != null && !nuovaDescr.trim().isEmpty()) {
            boolean ok = ModelManager.aggiornaDescrizione(titolo, nuovaDescr.trim());
            if (ok) {
                try (Connection conn = ConnessioneDatabase.getConnection()) {
                    BachecaDAOInterface dao = new BachecaDAO(conn);
                    dao.aggiornaDescrizione(titolo, nuovaDescr.trim(), getUtenteLoggato());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(mainFrame, "Errore durante salvataggio nel DB: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Bacheca non trovata.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }

        showBachecaWindow();
    }
    /**
     * Mostra una finestra di conferma per l'eliminazione di una bacheca e tutti i suoi ToDo.
     * Se confermato, rimuove la bacheca dal database e dal modello.
     *
     * @param titolo Titolo della bacheca da eliminare
     */

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
    /**
     * Restituisce la mappa delle bacheche correnti presenti nel modello.
     *
     * @return Mappa con titolo bacheca come chiave e oggetto Bacheca come valore
     */

    public static Map<TitoloBacheca, Bacheca> getBacheche() {
        return ModelManager.getBacheche();
    }
    /**
     * Restituisce la lista dei ToDo associati ad una specifica bacheca.
     *
     * @param titolo Titolo della bacheca
     * @return Lista dei ToDo associati alla bacheca
     */

    public static List<ToDo> getToDosByBacheca(TitoloBacheca titolo) {
        return ModelManager.getToDoPerBacheca(titolo);
    }
    /**
     * Mostra il form per creare un nuovo ToDo all'interno della bacheca specificata.
     *
     * @param tipo Titolo della bacheca in cui inserire il nuovo ToDo
     */

    public static void showToDoWindow(TitoloBacheca tipo) {
        ToDoFormWindow v = new ToDoFormWindow();
        initToDoFormController(v, tipo, null);
    }
    /**
     * Mostra il form di modifica per un ToDo esistente.
     *
     * @param tipo Titolo della bacheca a cui appartiene il ToDo
     * @param todo Oggetto ToDo da modificare
     */

    public static void showEditToDoWindow(TitoloBacheca tipo, ToDo todo) {
        ToDoFormWindow v = new ToDoFormWindow(todo);
        initToDoFormController(v, tipo, todo);
    }
    /**
     * Inizializza il form per la creazione o modifica di un ToDo,
     * impostando gli eventi dei pulsanti "Salva" e "Annulla".
     *
     * @param view          Vista del form ToDo
     * @param tipo          Titolo della bacheca a cui appartiene il ToDo
     * @param todoOriginal  Oggetto ToDo da modificare, oppure null se nuovo
     */

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
    /**
     * Elimina un ToDo specifico dal modello e dal database.
     *
     * @param tipo Titolo della bacheca da cui eliminare il ToDo
     * @param todo Oggetto ToDo da eliminare
     */

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
    /**
     * Sposta un ToDo verso l'alto o verso il basso nella lista della bacheca,
     * aggiornando l'ordine sia nel modello che nel database.
     *
     * @param titolo Titolo della bacheca contenente il ToDo
     * @param todo   Oggetto ToDo da spostare
     * @param delta  Direzione di spostamento: -1 per su, +1 per giù
     */

    public static void spostaToDo(TitoloBacheca titolo, ToDo todo, int delta) {
        List<ToDo> lista = ModelManager.getToDoPerBacheca(titolo);
        int currentIndex = lista.indexOf(todo);
        int newIndex = currentIndex + delta;

        if (newIndex >= 0 && newIndex < lista.size()) {
            Collections.swap(lista, currentIndex, newIndex);
            riallineaPosizioni(lista);

            try (Connection conn = ConnessioneDatabase.getConnection()) {
                ToDoDAO dao = new ToDoDAO(conn);
                for (int i = 0; i < lista.size(); i++) {
                    lista.get(i).setPosizione(i);
                }
                dao.aggiornaPosizioni(lista);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Mostra all'utente tutte le richieste di condivisione in sospeso.
     * Permette di accettare o rifiutare ciascuna richiesta. In caso di accettazione,
     * il ToDo viene aggiunto alla bacheca dell'utente.
     */

    public static void mostraDialogRichieste() {
        List<Richiesta> richieste = ModelManager.getRichiestePendenti();
        if (richieste == null || richieste.isEmpty()) return;

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            ToDoDAO todoDAO = new ToDoDAO(conn);
            RichiestaDAO richiestaDAO = new RichiestaDAO(conn);
            BachecaDAO bachecaDAO = new BachecaDAO(conn);

            for (Richiesta richiesta : richieste) {
                ToDo todoRichiesto = todoDAO.getToDoById(richiesta.getIdToDo());
                if (todoRichiesto == null) continue;

                int scelta = JOptionPane.showConfirmDialog(
                        null,
                        "Vuoi accettare il ToDo condiviso da " + richiesta.getMittente() +
                                " nella bacheca '" + richiesta.getTitoloBacheca() + "'?\nTitolo: " + todoRichiesto.getTitolo(),
                        "Richiesta di condivisione",
                        JOptionPane.YES_NO_OPTION
                );

                if (scelta == JOptionPane.YES_OPTION) {
                    richiestaDAO.aggiornaStato(richiesta.getId(), "accettata");

                    TitoloBacheca titoloBacheca = TitoloBacheca.valueOf(richiesta.getTitoloBacheca());
                    if (!ModelManager.getBacheche().containsKey(titoloBacheca)) {
                        ModelManager.creaBacheca(titoloBacheca, "Bacheca condivisa");
                        bachecaDAO.inserisciSeNonEsiste(titoloBacheca, "Bacheca condivisa", utenteLoggato);
                    }

                    todoRichiesto.setUsername(utenteLoggato);
                    todoRichiesto.setBachecaTitolo(titoloBacheca);
                    todoRichiesto.setAutore(richiesta.getMittente());

                    todoDAO.insert(todoRichiesto, titoloBacheca.name(), utenteLoggato);
                    ModelManager.aggiungiToDo(titoloBacheca, todoRichiesto);

                    RichiestaDAO condivDao = new RichiestaDAO(conn);
                    condivDao.inserisciCondivisione(todoRichiesto.getId(), utenteLoggato);

                } else {
                    richiestaDAO.aggiornaStato(richiesta.getId(), "rifiutata");
                }
            }
            ModelManager.setRichiestePendenti(Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Sposta un ToDo da una bacheca ad un'altra, aggiornando modello e database.
     *
     * @param origine      Bacheca di origine del ToDo
     * @param destinazione Bacheca di destinazione
     * @param todo         Oggetto ToDo da spostare
     */

    public static void spostaToDoInAltraBacheca(TitoloBacheca origine, TitoloBacheca destinazione, ToDo todo) {
        ModelManager.rimuoviToDo(origine, todo);
        ModelManager.aggiungiToDo(destinazione, todo);

        todo.setBachecaTitolo(destinazione);
        todo.setUsername(getUtenteLoggato());

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            ToDoDAO dao = new ToDoDAO(conn);
            dao.aggiornaBacheca(todo, destinazione); // ✅ Salva nel DB
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore nello spostamento del ToDo nel DB: " + e.getMessage());
        }
    }


    /**
     * Riassegna le posizioni ai ToDo nella lista, mantenendo l'ordine attuale.
     *
     * @param lista Lista di ToDo da aggiornare
     */

    private static void riallineaPosizioni(List<ToDo> lista) {
        for (int i = 0; i < lista.size(); i++) {
            lista.get(i).setPosizione(i);
        }
    }
    /**
     * Aggiorna graficamente il frame principale, forzando la validazione e il repaint.
     */

    private static void refresh() {
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    /**
     * Restituisce la lista di tutti gli utenti registrati nel sistema,
     * escluso l'utente attualmente loggato.
     *
     * @return Lista di username disponibili per la condivisione
     */

    public static List<String> getTuttiUtenti() {
        try {
            return utenteDao.getAllUsernamesExcept(utenteLoggato);
        } catch (Exception e) {
            System.err.println("Errore durante il recupero degli utenti: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
