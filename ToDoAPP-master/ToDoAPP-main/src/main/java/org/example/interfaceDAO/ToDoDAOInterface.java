package org.example.interfaceDAO;

import org.example.model.TitoloBacheca;
import org.example.model.ToDo;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO per la gestione dei ToDo nel database.
 * Definisce i metodi principali per CRUD, condivisione e riorganizzazione dei ToDo.
 */
public interface ToDoDAOInterface {

    /**
     * Inserisce un nuovo ToDo nel database.
     *
     * @param todo          Oggetto {@link ToDo} da inserire
     * @param bachecaTitolo Titolo della bacheca a cui è associato il ToDo
     * @param username      Nome dell'utente che inserisce il ToDo
     * @throws SQLException In caso di errore SQL
     */
    void insert(ToDo todo, String bachecaTitolo, String username) throws SQLException;

    /**
     * Aggiorna i dati di un ToDo esistente.
     *
     * @param todo Oggetto {@link ToDo} aggiornato
     * @throws SQLException In caso di errore SQL
     */
    void update(ToDo todo) throws SQLException;

    /**
     * Elimina un ToDo dal database per uno specifico utente.
     *
     * @param todo     Oggetto {@link ToDo} da eliminare
     * @param username Nome utente che possiede il ToDo
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti
     * @throws SQLException In caso di errore SQL
     */
    boolean delete(ToDo todo, String username) throws SQLException;

    /**
     * Recupera tutti i ToDo appartenenti a una bacheca e un utente specifico.
     *
     * @param bachecaTitolo Titolo della bacheca
     * @param username      Nome utente proprietario dei ToDo
     * @return Lista di ToDo
     * @throws SQLException In caso di errore SQL
     */
    List<ToDo> getToDosByBachecaAndUser(String bachecaTitolo, String username) throws SQLException;

    /**
     * Restituisce un ToDo a partire dal suo ID.
     *
     * @param id ID del ToDo
     * @return Oggetto {@link ToDo} se trovato, null altrimenti
     * @throws Exception In caso di errore
     */
    ToDo getToDoById(int id) throws Exception;

    /**
     * Rimuove la partecipazione di un utente a un ToDo condiviso.
     *
     * @param titolo    Titolo del ToDo
     * @param autore    Nome dell'autore del ToDo
     * @param username  Utente da rimuovere dalla condivisione
     * @return true se la rimozione è avvenuta con successo, false altrimenti
     * @throws SQLException In caso di errore SQL
     */
    boolean rimuoviPartecipazione(String titolo, String autore, String username) throws SQLException;

    /**
     * Recupera tutti i ToDo condivisi con un determinato utente.
     *
     * @param username Nome dell'utente destinatario
     * @return Lista di ToDo condivisi
     * @throws Exception In caso di errore
     */
    List<ToDo> getToDosCondivisiConUtente(String username) throws Exception;

    /**
     * Aggiorna la bacheca associata a un ToDo.
     *
     * @param todo          Oggetto {@link ToDo} da aggiornare
     * @param nuovaBacheca  Nuova bacheca di destinazione
     * @throws SQLException In caso di errore SQL
     */
    void aggiornaBacheca(ToDo todo, TitoloBacheca nuovaBacheca) throws SQLException;

    /**
     * Aggiorna la posizione dei ToDo all'interno della bacheca.
     *
     * @param todos Lista di ToDo ordinati secondo la nuova posizione
     * @throws SQLException In caso di errore SQL
     */
    void aggiornaPosizioni(List<ToDo> todos) throws SQLException;

    /**
     * Aggiorna l'elenco degli utenti con cui un ToDo è condiviso.
     *
     * @param todo Oggetto {@link ToDo} da aggiornare
     * @throws Exception In caso di errore
     */
    void aggiornaListaUtenti(ToDo todo) throws Exception;

    /**
     * Verifica se esiste già un ToDo condiviso tra autore e destinatario.
     *
     * @param titolo   Titolo del ToDo
     * @param autore   Nome dell'autore
     * @param username Utente destinatario
     * @return true se il ToDo condiviso esiste già, false altrimenti
     */
    boolean esisteToDoCondiviso(String titolo, String autore, String username);
}
