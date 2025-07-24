package org.example.interfaceDAO;

import org.example.model.Richiesta;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia per la gestione delle richieste di condivisione dei ToDo.
 * Definisce i metodi per inserire, aggiornare e recuperare richieste dal database.
 */
public interface RichiestaDAOInterface {

    /**
     * Inserisce una nuova richiesta di condivisione nel database.
     *
     * @param richiesta Oggetto {@link Richiesta} contenente i dati della richiesta
     */
    void inserisciRichiesta(Richiesta richiesta);

    /**
     * Verifica se esiste già una richiesta in attesa per un determinato ToDo.
     *
     * @param idTodo      ID del ToDo
     * @param mittente    Utente che ha inviato la richiesta
     * @param destinatario Utente che dovrebbe riceverla
     * @return true se la richiesta esiste già ed è in attesa, false altrimenti
     */
    boolean esisteRichiesta(int idTodo, String mittente, String destinatario);

    /**
     * Aggiorna lo stato di una richiesta di condivisione (es. accettata, rifiutata).
     *
     * @param richiestaId ID della richiesta da aggiornare
     * @param nuovoStato  Nuovo stato da assegnare (es. "accettata", "rifiutata")
     */
    void aggiornaStato(int richiestaId, String nuovoStato);

    /**
     * Restituisce tutte le richieste in attesa per un determinato utente.
     *
     * @param username Nome utente destinatario delle richieste
     * @return Lista di richieste {@link Richiesta}
     */
    List<Richiesta> getRichiestePerUtente(String username);

    /**
     * Restituisce l’elenco degli utenti con cui un determinato ToDo è stato condiviso.
     *
     * @param titolo Titolo del ToDo
     * @param autore Nome dell’autore del ToDo
     * @return Lista di username con cui il ToDo è stato condiviso
     * @throws SQLException In caso di errore SQL
     */
    List<String> getUtentiCondivisi(String titolo, String autore) throws SQLException;

    /**
     * Aggiorna lo stato di una richiesta per uno specifico ToDo e destinatario.
     *
     * @param idToDo     ID del ToDo coinvolto nella richiesta
     * @param destinatario Utente destinatario della richiesta
     * @param stato      Nuovo stato della richiesta
     * @throws SQLException In caso di errore SQL
     */
    void aggiornaStatoRichiesta(int idToDo, String destinatario, String stato) throws SQLException;
}
