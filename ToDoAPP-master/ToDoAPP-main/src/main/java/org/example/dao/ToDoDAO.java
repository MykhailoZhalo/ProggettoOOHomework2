package org.example.dao;

import org.example.controller.Controller;
import org.example.db.ConnessioneDatabase;
import org.example.model.TitoloBacheca;
import org.example.model.ToDo;
import org.example.interfaceDAO.ToDoDAOInterface;

import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Classe DAO che implementa l'interfaccia {@link ToDoDAOInterface} per gestire le operazioni sui ToDo nel database.
 * Gestisce creazione, aggiornamento, eliminazione, condivisione e recupero dei ToDo.
 */
public class  ToDoDAO implements ToDoDAOInterface {

    private final Connection conn;

    /**
     * Costruttore che inizializza il DAO con la connessione al database.
     * @param conn Connessione attiva al database
     */
    public ToDoDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Aggiorna un ToDo nel database, mantenendo l'autore e i partecipanti originali se l'utente non è l'autore.
     * @param todo Il ToDo da aggiornare
     * @throws SQLException In caso di errore SQL
     */
    @Override
    public void update(ToDo todo) throws SQLException {
        String autoreOriginale = null;
        String listaUtentiOriginale = null;

        String selectSql = "SELECT autore, lista_utenti FROM todo WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
            ps.setInt(1, todo.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    autoreOriginale = rs.getString("autore");
                    listaUtentiOriginale = rs.getString("lista_utenti");
                }
            }
        }

        String utenteCorrente = Controller.getUtenteLoggato();
        boolean isAutore = utenteCorrente != null && utenteCorrente.equals(autoreOriginale);

        if (!isAutore) {
            todo.setAutore(autoreOriginale);
            todo.setListaUtenti(listaUtentiOriginale != null ? Arrays.asList(listaUtentiOriginale.split(",")) : null);
        }

        String updateSql = "UPDATE todo SET titolo = ?, descrizione = ?, scadenza = ?, url = ?, colore_sfondo = ?, immagine_path = ?, lista_utenti = ?, autore = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setString(1, todo.getTitolo());
            ps.setString(2, todo.getDescrizione());
            ps.setDate(3, new java.sql.Date(todo.getScadenza().getTime()));
            ps.setString(4, todo.getUrl());
            ps.setString(5, todo.getColoreSfondo());
            ps.setString(6, todo.getImmaginePath());

            String utentiConcat = todo.getListaUtenti() != null ? String.join(",", todo.getListaUtenti()) : null;
            ps.setString(7, utentiConcat);
            ps.setString(8, todo.getAutore());
            ps.setInt(9, todo.getId());

            ps.executeUpdate();
        }
    }

    /**
     * Verifica se esiste già un ToDo condiviso con un utente specifico.
     */
    @Override
    public boolean esisteToDoCondiviso(String titolo, String autore, String username) {
        String sql = "SELECT COUNT(*) FROM todo WHERE titolo = ? AND autore = ? AND username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titolo);
            stmt.setString(2, autore);
            stmt.setString(3, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserisce un nuovo ToDo nel database.
     */
    @Override
    public void insert(ToDo todo, String titoloBacheca, String username) {
        String sql = "INSERT INTO todo (titolo, descrizione, scadenza, url, colore_sfondo, immagine_path, bacheca_titolo, username, autore, lista_utenti) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, todo.getTitolo());
            stmt.setString(2, todo.getDescrizione());
            stmt.setDate(3, new java.sql.Date(todo.getScadenza().getTime()));
            stmt.setString(4, todo.getUrl());
            stmt.setString(5, todo.getColoreSfondo());
            stmt.setString(6, todo.getImmaginePath());
            stmt.setString(7, titoloBacheca);
            stmt.setString(8, username);
            stmt.setString(9, todo.getAutore());

            String utentiConcat = todo.getListaUtenti() != null ? String.join(",", todo.getListaUtenti()) : "";
            stmt.setString(10, utentiConcat.isBlank() ? null : utentiConcat);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    todo.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento del ToDo: " + e.getMessage());
        }
    }

    /**
     * Elimina un ToDo dal database.
     */
    @Override
    public boolean delete(ToDo todo, String username) {
        if (todo.getBachecaTitolo() == null) return false;

        String sql = "DELETE FROM todo WHERE titolo = ? AND bacheca_titolo = ? AND username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, todo.getTitolo());
            stmt.setString(2, todo.getBachecaTitolo().name());
            stmt.setString(3, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la cancellazione del ToDo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Restituisce un ToDo dato il suo ID.
     */
    @Override
    public ToDo getToDoById(int id) throws SQLException {
        String sql = "SELECT * FROM todo WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ToDo todo = new ToDo(
                            rs.getString("titolo"),
                            rs.getString("descrizione"),
                            rs.getDate("scadenza")
                    );
                    todo.setId(rs.getInt("id"));
                    todo.setUrl(rs.getString("url"));
                    todo.setColoreSfondo(rs.getString("colore_sfondo"));
                    todo.setImmaginePath(rs.getString("immagine_path"));
                    todo.setUsername(rs.getString("username"));
                    todo.setAutore(rs.getString("autore"));

                    String bacheca = rs.getString("bacheca_titolo");
                    if (bacheca != null) {
                        todo.setBachecaTitolo(TitoloBacheca.valueOf(bacheca));
                    }

                    String utentiStr = rs.getString("lista_utenti");
                    if (utentiStr != null && !utentiStr.isBlank()) {
                        todo.setListaUtenti(Arrays.asList(utentiStr.split(",")));
                    }
                    return todo;
                }
            }
        }
        return null;
    }

    /**
     * Aggiorna la lista degli utenti con cui il ToDo è condiviso.
     */
    @Override
    public void aggiornaListaUtenti(ToDo todo) throws Exception {
        String sql = "UPDATE todo SET lista_utenti = ? WHERE id = ?";
        String lista = (todo.getListaUtenti() != null && !todo.getListaUtenti().isEmpty())
                ? String.join(",", todo.getListaUtenti()) : null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (lista != null) {
                ps.setString(1, lista);
            } else {
                ps.setNull(1, Types.VARCHAR);
            }
            ps.setInt(2, todo.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Restituisce l'ID di un ToDo dato titolo e autore.
     */
    public int getToDoIdByTitoloAutore(String titolo, String autore) throws SQLException {
        String sql = "SELECT id FROM todo WHERE titolo = ? AND autore = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, titolo);
            ps.setString(2, autore);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("ToDo non trovato per titolo/autore.");
    }

    /**
     * Restituisce l'elenco dei ToDo condivisi con un utente specifico.
     */
    @Override
    public List<ToDo> getToDosCondivisiConUtente(String username) {
        List<ToDo> condivisi = new ArrayList<>();
        String sql = "SELECT * FROM todo WHERE lista_utenti IS NOT NULL AND lista_utenti LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + username + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ToDo todo = new ToDo(
                            rs.getString("titolo"),
                            rs.getString("descrizione"),
                            rs.getDate("scadenza")
                    );
                    todo.setId(rs.getInt("id"));
                    todo.setBachecaTitolo(TitoloBacheca.valueOf(rs.getString("bacheca_titolo")));
                    todo.setUrl(rs.getString("url"));
                    todo.setImmaginePath(rs.getString("immagine_path"));
                    todo.setColoreSfondo(rs.getString("colore_sfondo"));
                    todo.setAutore(rs.getString("autore"));
                    todo.setUsername(username);

                    String lista = rs.getString("lista_utenti");
                    if (lista != null && !lista.isEmpty()) {
                        todo.setListaUtenti(Arrays.asList(lista.split(",")));
                    }
                    condivisi.add(todo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return condivisi;
    }

    /**
     * Rimuove un utente da un ToDo condiviso.
     */
    @Override
    public boolean rimuoviPartecipazione(String titolo, String autore, String username) throws SQLException {
        String sql = "DELETE FROM todo WHERE titolo = ? AND autore = ? AND username = ?";
        String sqlCondiv = "DELETE FROM condivisioni WHERE condiviso_con = ? AND id_todo = ?";

        int rowsAffected = 0;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, titolo);
            ps.setString(2, autore);
            ps.setString(3, username);
            rowsAffected = ps.executeUpdate();
        }

        try (PreparedStatement ps2 = conn.prepareStatement(sqlCondiv)) {
            ps2.setString(1, username);
            ps2.setInt(2, getToDoIdByTitoloAutore(titolo, autore));
            ps2.executeUpdate();
        }

        return rowsAffected > 0;
    }

    /**
     * Restituisce tutti i ToDo associati a una bacheca e un utente specifico.
     */
    @Override
    public List<ToDo> getToDosByBachecaAndUser(String bachecaTitolo, String username) {
        List<ToDo> todos = new ArrayList<>();
        String sql = "SELECT * FROM todo WHERE bacheca_titolo = ? AND username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bachecaTitolo);
            stmt.setString(2, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ToDo todo = new ToDo(
                            rs.getString("titolo"),
                            rs.getString("descrizione"),
                            new Date(rs.getDate("scadenza").getTime())
                    );
                    todo.setId(rs.getInt("id"));
                    todo.setUrl(rs.getString("url"));
                    todo.setColoreSfondo(rs.getString("colore_sfondo"));
                    todo.setImmaginePath(rs.getString("immagine_path"));
                    todo.setBachecaTitolo(TitoloBacheca.valueOf(rs.getString("bacheca_titolo")));
                    todo.setAutore(rs.getString("autore"));
                    todo.setPosizione(rs.getInt("posizione"));
                    todos.add(todo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        todos.sort(Comparator.comparingInt(ToDo::getPosizione));
        return todos;
    }

    /**
     * Aggiorna la bacheca di appartenenza di un ToDo.
     */
    @Override
    public void aggiornaBacheca(ToDo todo, TitoloBacheca nuovaBacheca) throws SQLException {
        String sql = "UPDATE todo SET bacheca_titolo = ? WHERE id = ? AND username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuovaBacheca.name());
            stmt.setInt(2, todo.getId());
            stmt.setString(3, todo.getUsername());
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                System.err.println("⚠️ Nessuna riga aggiornata nel DB per ToDo ID " + todo.getId());
            }
        }
    }

    /**
     * Aggiorna la posizione dei ToDo nella lista.
     */
    @Override
    public void aggiornaPosizioni(List<ToDo> todos) throws SQLException {
        String sql = "UPDATE todo SET posizione = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (ToDo todo : todos) {
                ps.setInt(1, todo.getPosizione());
                ps.setInt(2, todo.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
