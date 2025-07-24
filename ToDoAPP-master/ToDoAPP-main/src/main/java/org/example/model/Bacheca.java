package org.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta una bacheca personale dell'utente,
 * contenente un elenco di attività (ToDo) associate a un titolo e una descrizione.
 */
public class Bacheca {

    /**
     * Titolo identificativo della bacheca (enum).
     */
    private TitoloBacheca titolo;

    /**
     * Descrizione testuale della bacheca.
     */
    private String descrizione;

    /**
     * Elenco dei ToDo associati a questa bacheca.
     */
    private List<ToDo> toDoList;

    /**
     * Username del proprietario della bacheca.
     */
    private String username;

    /**
     * Costruttore della bacheca.
     *
     * @param titolo      Titolo della bacheca
     * @param descrizione Descrizione della bacheca
     */
    public Bacheca(TitoloBacheca titolo, String descrizione) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.toDoList = new ArrayList<>();
    }

    /**
     * Restituisce l'username del proprietario della bacheca.
     *
     * @return Username proprietario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Imposta l'username del proprietario della bacheca.
     *
     * @param username Nome utente da assegnare
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Imposta una nuova descrizione per la bacheca.
     *
     * @param descrizione Testo della nuova descrizione
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce il titolo della bacheca.
     *
     * @return Titolo della bacheca
     */
    public TitoloBacheca getTitolo() {
        return titolo;
    }

    /**
     * Restituisce la descrizione della bacheca.
     *
     * @return Descrizione della bacheca
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Restituisce la lista di ToDo associati a questa bacheca.
     *
     * @return Lista di ToDo
     */
    public List<ToDo> getToDoList() {
        return toDoList;
    }

    /**
     * Aggiunge un ToDo alla lista associata a questa bacheca.
     *
     * @param todo Oggetto ToDo da aggiungere
     */
    public void aggiungiToDo(ToDo todo) {
        toDoList.add(todo);
    }
}
