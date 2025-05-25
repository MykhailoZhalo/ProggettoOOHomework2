package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Bacheca {
    private TitoloBacheca titolo;
    private String descrizione;
    private List<ToDo> toDoList;

    public Bacheca(TitoloBacheca titolo, String descrizione) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.toDoList = new ArrayList<>();
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public TitoloBacheca getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public List<ToDo> getToDoList() {
        return toDoList;
    }

    public void aggiungiToDo(ToDo todo) {
        toDoList.add(todo);
    }
}
