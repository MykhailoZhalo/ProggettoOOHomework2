package org.example.interfaceDAO;
import java.sql.SQLException;
import org.example.model.ToDo;
import java.util.List;

public interface ToDoDAOInterface {
    void insert(ToDo todo, String bachecaTitolo, String username) throws SQLException;
    void update(ToDo todo) throws SQLException;
    boolean delete(ToDo todo, String username) throws SQLException; // 👈 Metodo da aggiungere
    List<ToDo> getToDosByBachecaAndUser(String bachecaTitolo, String username) throws SQLException;
}

