package org.example.dao;

import org.example.db.ConnessioneDatabase;
import org.example.model.ToDo;
import org.example.interfaceDAO.ToDoDAOInterface;

import java.sql.Connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDoDAO implements ToDoDAOInterface {

    private final Connection conn;

    public ToDoDAO(Connection conn) {
        this.conn = conn;
    }
    public void update(ToDo todo) throws SQLException {
        String sql = "UPDATE todo SET titolo = ?, descrizione = ?, scadenza = ?, url = ?, immagine_path = ?, colore_sfondo = ?, lista_utenti = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, todo.getTitolo());
            ps.setString(2, todo.getDescrizione());
            ps.setDate(3, new java.sql.Date(todo.getScadenza().getTime()));
            ps.setString(4, todo.getUrl());
            ps.setString(5, todo.getImmaginePath());
            ps.setString(6, todo.getColoreSfondo());

            // Salva lista utenti come stringa separata da virgole
            List<String> utenti = todo.getListaUtenti();
            String utentiConcat = utenti != null ? String.join(",", utenti) : "";
            ps.setString(7, utentiConcat);

            // Supponendo che tu abbia l'id del ToDo da aggiornare
            ps.setInt(8, todo.getPosizione()); // o usa un campo ID se lo hai

            ps.executeUpdate();
        }
    }

    @Override
    public void insert(ToDo todo, String titoloBacheca, String username) {
        String sql = "INSERT INTO todo (titolo, descrizione, scadenza, url, colore_sfondo, immagine_path, bacheca_titolo, username) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, todo.getTitolo());
            stmt.setString(2, todo.getDescrizione());
            stmt.setDate(3, new java.sql.Date(todo.getScadenza().getTime()));
            stmt.setString(4, todo.getUrl());
            stmt.setString(5, todo.getColoreSfondo());
            stmt.setString(6, todo.getImmaginePath());
            stmt.setString(7, titoloBacheca);
            stmt.setString(8, username);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(ToDo todo, String username) {
        // Verifica che il campo bachecaTitolo sia stato settato
        if (todo.getBachecaTitolo() == null) {
            System.err.println("Errore: bachecaTitolo non impostato nel ToDo, impossibile eliminare.");
            return false;
        }

        String sql = "DELETE FROM todo WHERE titolo = ? AND bacheca_titolo = ? AND username = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, todo.getTitolo());
            stmt.setString(2, todo.getBachecaTitolo().name());  // enum come stringa
            stmt.setString(3, username);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Errore durante la cancellazione del ToDo: " + e.getMessage());
            return false;
        }
    }



    @Override
    public List<ToDo> getToDosByBachecaAndUser(String bachecaTitolo, String username) {
        List<ToDo> todos = new ArrayList<>();
        String sql = "SELECT * FROM todo WHERE bacheca_titolo = ? AND username = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bachecaTitolo);
            stmt.setString(2, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ToDo todo = new ToDo(
                            rs.getString("titolo"),
                            rs.getString("descrizione"),
                            new Date(rs.getDate("scadenza").getTime())
                    );
                    todo.setUrl(rs.getString("url"));
                    todo.setColoreSfondo(rs.getString("colore_sfondo"));
                    todo.setImmaginePath(rs.getString("immagine_path"));

                    todos.add(todo);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;
    }
}