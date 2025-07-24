package org.example.interfaceDAO;

import org.example.model.Bacheca;
import org.example.model.TitoloBacheca;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia per la gestione delle operazioni DAO relative alla bacheca.
 * Fornisce metodi per inserire, recuperare, aggiornare e eliminare bacheche.
 */
public interface BachecaDAOInterface {

    /**
     * Inserisce una nuova bacheca per l'utente se non esiste già.
     *
     * @param titolo      Titolo della bacheca
     * @param descrizione Descrizione della bacheca
     * @param username    Nome utente proprietario della bacheca
     * @throws SQLException In caso di errore SQL
     */
    void inserisciSeNonEsiste(TitoloBacheca titolo, String descrizione, String username) throws SQLException;

    /**
     * Restituisce l'elenco delle bacheche associate a un utente.
     *
     * @param username Nome utente
     * @return Lista di bacheche dell'utente
     * @throws SQLException In caso di errore SQL
     */
    List<Bacheca> getBachechePerUtente(String username) throws SQLException;

    /**
     * Elimina una bacheca e tutti i ToDo associati per un dato utente.
     *
     * @param titolo   Titolo della bacheca da eliminare
     * @param username Nome utente proprietario della bacheca
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti
     * @throws SQLException In caso di errore SQL
     */
    boolean elimina(TitoloBacheca titolo, String username) throws SQLException;

    /**
     * Aggiorna la descrizione di una bacheca per un dato utente.
     *
     * @param titolo           Titolo della bacheca da aggiornare
     * @param nuovaDescrizione Nuova descrizione da assegnare
     * @param username         Nome utente proprietario della bacheca
     * @throws SQLException In caso di errore SQL
     */
    void aggiornaDescrizione(TitoloBacheca titolo, String nuovaDescrizione, String username) throws SQLException;
}
