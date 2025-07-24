package org.example.dao;

import org.example.interfaceDAO.RichiestaDAOInterface;
import org.example.model.Richiesta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO per la gestione delle richieste di condivisione dei ToDo.
 * Implementa l'interfaccia {@link RichiestaDAOInterface} e fornisce operazioni per inserire, recuperare e aggiornare richieste.
 */
public class RichiestaDAO implements RichiestaDAOInterface {

    /** Connessione al database */
    private final Connection conn;

    /**
     * Costruttore che inizializza il DAO con una connessione al database.
     * @param conn Connessione valida al database
     */
    public RichiestaDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserisce una nuova richiesta di condivisione nel database.
     * @param richiesta Oggetto Richiesta da salvare
     */
    @Override
    public void inserisciRichiesta(Richiesta richiesta) {
        String sql = "INSERT INTO richieste_todo (id_todo, mittente, destinatario, titolo_bacheca) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, richiesta.getIdToDo());
            ps.setString(2, richiesta.getMittente());
            ps.setString(3, richiesta.getDestinatario());
            ps.setString(4, richiesta.getTitoloBacheca());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante inserimento richiesta: " + e.getMessage());
        }
    }

    /**
     * Verifica se esiste già una richiesta pendente per uno specifico ToDo tra mittente e destinatario.
     * @param idTodo ID del ToDo condiviso
     * @param mittente Mittente della richiesta
     * @param destinatario Destinatario della richiesta
     * @return true se esiste una richiesta in attesa, false altrimenti
     */
    @Override
    public boolean esisteRichiesta(int idTodo, String mittente, String destinatario) {
        String sql = "SELECT COUNT(*) FROM richieste_todo WHERE id_todo = ? AND mittente = ? AND destinatario = ? AND stato = 'in_attesa'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTodo);
            stmt.setString(2, mittente);
            stmt.setString(3, destinatario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Aggiorna lo stato di una richiesta identificata per ID.
     * @param richiestaId ID della richiesta
     * @param nuovoStato Nuovo stato da impostare (es. "accettata", "rifiutata")
     */
    @Override
    public void aggiornaStato(int richiestaId, String nuovoStato) {
        String sql = "UPDATE richieste_todo SET stato = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuovoStato);
            ps.setInt(2, richiestaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce l'elenco di richieste pendenti per un utente specifico.
     * @param username Nome utente destinatario delle richieste
     * @return Lista di richieste pendenti
     */
    @Override
    public List<Richiesta> getRichiestePerUtente(String username) {
        List<Richiesta> richieste = new ArrayList<>();
        String sql = "SELECT * FROM richieste_todo WHERE destinatario = ? AND stato = 'in_attesa'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Richiesta r = new Richiesta(
                            rs.getInt("id"),
                            rs.getInt("id_todo"),
                            rs.getString("mittente"),
                            rs.getString("destinatario"),
                            rs.getString("titolo_bacheca"),
                            rs.getString("stato"),
                            rs.getTimestamp("data_richiesta")
                    );
                    richieste.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return richieste;
    }

    /**
     * Inserisce un record di condivisione accettata nella tabella delle condivisioni.
     * @param todoId ID del ToDo condiviso
     * @param destinatario Utente con cui è stato condiviso il ToDo
     * @throws SQLException In caso di errore durante l'inserimento
     */
    public void inserisciCondivisione(int todoId, String destinatario) throws SQLException {
        String sql = "INSERT INTO condivisioni (id_todo, condiviso_con) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, todoId);
            stmt.setString(2, destinatario);
            stmt.executeUpdate();
        }
    }

    /**
     * Aggiorna lo stato di una richiesta in base all'ID del ToDo e al destinatario.
     * @param idToDo ID del ToDo
     * @param destinatario Destinatario della richiesta
     * @param stato Nuovo stato da impostare
     * @throws SQLException In caso di errore SQL
     */
    @Override
    public void aggiornaStatoRichiesta(int idToDo, String destinatario, String stato) throws SQLException {
        String sql = "UPDATE richieste_todo SET stato = ? WHERE id_todo = ? AND destinatario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stato);
            stmt.setInt(2, idToDo);
            stmt.setString(3, destinatario);
            stmt.executeUpdate();
        }
    }

    /**
     * Restituisce l'elenco degli utenti con cui un determinato ToDo è stato condiviso e accettato.
     * @param titolo Titolo del ToDo condiviso
     * @param autore Autore del ToDo
     * @return Lista di nomi utente con cui il ToDo è stato condiviso
     * @throws SQLException In caso di errore SQL
     */
    @Override
    public List<String> getUtentiCondivisi(String titolo, String autore) throws SQLException {
        List<String> utenti = new ArrayList<>();
        String sql = "SELECT destinatario FROM richieste_todo WHERE id_todo IN (" +
                "  SELECT id FROM todo WHERE titolo = ? AND autore = ?" +
                ") AND stato = 'accettata'";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titolo);
            stmt.setString(2, autore);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    utenti.add(rs.getString("destinatario"));
                }
            }
        }

        // Aggiungi anche l'autore alla lista (se vuoi visualizzarlo)
        if (!utenti.contains(autore)) {
            utenti.add(autore);
        }

        return utenti;
    }
}

