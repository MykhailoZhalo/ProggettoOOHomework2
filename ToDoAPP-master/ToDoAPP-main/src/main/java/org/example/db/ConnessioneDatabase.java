package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe di utilità per la gestione della connessione al database PostgreSQL.
 * Fornisce un metodo statico per ottenere una connessione al database.
 */
public class ConnessioneDatabase {

    /** URL del database PostgreSQL, incluso lo schema. */
    private static final String URL = "jdbc:postgresql://localhost:5432/UltimoDb?currentSchema=public";

    /** Nome utente per la connessione al database. */
    private static final String USER = "postgres";

    /** Password dell'utente per la connessione al database. */
    private static final String PASSWORD = "1234";

    /**
     * Restituisce una connessione attiva al database PostgreSQL.
     *
     * @return Oggetto {@link Connection} pronto per l'uso
     * @throws SQLException Se la connessione non può essere stabilita
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL non trovato", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
