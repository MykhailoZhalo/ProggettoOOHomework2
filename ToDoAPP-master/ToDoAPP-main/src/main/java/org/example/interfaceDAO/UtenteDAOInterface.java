package org.example.interfaceDAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO per la gestione degli utenti nel sistema.
 * Fornisce metodi per registrazione, autenticazione e recupero degli username.
 */
public interface UtenteDAOInterface {

    /**
     * Registra un nuovo utente nel sistema.
     *
     * @param username Nome utente da registrare
     * @param password Password associata all'utente
     * @return true se la registrazione ha avuto successo, false altrimenti
     */
    boolean registraUtente(String username, String password);

    /**
     * Verifica la presenza dell'utente nel sistema con le credenziali specificate.
     *
     * @param username Nome utente
     * @param password Password dell'utente
     * @return true se l'utente esiste ed è autenticato correttamente, false altrimenti
     */
    boolean loginUtente(String username, String password);

    /**
     * Alias di {@link #loginUtente(String, String)} per verificare le credenziali di accesso.
     *
     * @param username Nome utente
     * @param password Password dell'utente
     * @return true se le credenziali sono valide, false altrimenti
     */
    boolean checkLogin(String username, String password);

    /**
     * Restituisce una lista di tutti gli username presenti nel sistema,
     * ad eccezione di quello fornito (es. l'utente loggato).
     *
     * @param exclude Username da escludere dai risultati
     * @return Lista di username degli altri utenti
     * @throws SQLException In caso di errore nella query al database
     */
    List<String> getAllUsernamesExcept(String exclude) throws SQLException;
}
