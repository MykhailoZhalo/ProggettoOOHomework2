package org.example.dao;

import org.example.db.ConnessioneDatabase;
import org.example.interfaceDAO.UtenteDAOInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO per la gestione degli utenti nel database.
 * Implementa le operazioni di registrazione, login e recupero degli username.
 */
public class UtenteDAO implements UtenteDAOInterface {

    /**
     * Registra un nuovo utente nel database.
     *
     * @param username Nome utente da registrare
     * @param password Password dell'utente
     * @return true se la registrazione è andata a buon fine, false altrimenti
     */
    @Override
    public boolean registraUtente(String username, String password) {
        String sql = "INSERT INTO utenti (username, password) VALUES (?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Errore durante la registrazione: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica le credenziali di accesso dell'utente.
     *
     * @param username Nome utente
     * @param password Password
     * @return true se le credenziali sono corrette, false altrimenti
     */
    @Override
    public boolean loginUtente(String username, String password) {
        String sql = "SELECT * FROM utenti WHERE username = ? AND password = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Se trova una riga, le credenziali sono valide

        } catch (SQLException e) {
            System.err.println("Errore durante il login: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo di comodo per effettuare il check del login, reindirizza a {@link #loginUtente(String, String)}.
     *
     * @param username Nome utente
     * @param password Password
     * @return true se login valido, false altrimenti
     */
    @Override
    public boolean checkLogin(String username, String password) {
        return loginUtente(username, password);
    }

    /**
     * Restituisce tutti gli username degli utenti registrati, escludendo un utente specifico.
     *
     * @param exclude Username da escludere dalla lista
     * @return Lista di username
     * @throws SQLException In caso di errore SQL
     */
    @Override
    public List<String> getAllUsernamesExcept(String exclude) throws SQLException {
        List<String> utenti = new ArrayList<>();
        String sql = "SELECT username FROM utenti WHERE username <> ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, exclude);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                utenti.add(rs.getString("username"));
            }
        }

        return utenti;
    }
}
