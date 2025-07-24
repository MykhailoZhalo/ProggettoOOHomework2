package org.example.model;

import java.util.*;

/**
 * Classe di utilità per la gestione centralizzata del modello dati (bacheche, ToDo e richieste).
 * Funziona da "in-memory store" dell'applicazione per rappresentare lo stato corrente.
 */
public class ModelManager {

    /**
     * Mappa che contiene tutte le bacheche dell'utente, indicizzate per titolo.
     */
    private static final Map<TitoloBacheca, Bacheca> bacheche = new EnumMap<>(TitoloBacheca.class);

    /**
     * Crea una nuova bacheca se non esiste già.
     *
     * @param titolo      Titolo della bacheca
     * @param descrizione Descrizione della bacheca
     * @return true se la bacheca è stata creata, false se esisteva già
     */
    public static boolean creaBacheca(TitoloBacheca titolo, String descrizione) {
        if (bacheche.containsKey(titolo)) return false;
        bacheche.put(titolo, new Bacheca(titolo, descrizione));
        return true;
    }

    /**
     * Restituisce tutte le bacheche attualmente gestite.
     *
     * @return Mappa delle bacheche
     */
    public static Map<TitoloBacheca, Bacheca> getBacheche() {
        return bacheche;
    }

    /**
     * Aggiorna la descrizione di una bacheca esistente.
     *
     * @param titolo      Titolo della bacheca
     * @param descrizione Nuova descrizione da impostare
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public static boolean aggiornaDescrizione(TitoloBacheca titolo, String descrizione) {
        Bacheca b = bacheche.get(titolo);
        if (b != null) {
            b.setDescrizione(descrizione);
            return true;
        }
        return false;
    }

    /**
     * Elimina una bacheca dal sistema.
     *
     * @param titolo Titolo della bacheca da rimuovere
     * @return true se è stata rimossa, false se non esisteva
     */
    public static boolean eliminaBacheca(TitoloBacheca titolo) {
        return bacheche.remove(titolo) != null;
    }

    /**
     * Restituisce la lista di ToDo associata a una bacheca.
     *
     * @param titolo Titolo della bacheca
     * @return Lista di ToDo o una lista vuota se la bacheca non esiste
     */
    public static List<ToDo> getToDoPerBacheca(TitoloBacheca titolo) {
        Bacheca bacheca = bacheche.get(titolo);
        return bacheca != null ? bacheca.getToDoList() : new ArrayList<>();
    }

    /**
     * Aggiunge un ToDo a una bacheca specificata.
     *
     * @param titolo Titolo della bacheca
     * @param todo   Oggetto ToDo da aggiungere
     */
    public static void aggiungiToDo(TitoloBacheca titolo, ToDo todo) {
        Bacheca b = bacheche.get(titolo);
        if (b != null) {
            b.aggiungiToDo(todo);
        }
    }

    /**
     * Rimuove un ToDo da una bacheca specificata.
     *
     * @param titolo Titolo della bacheca
     * @param todo   Oggetto ToDo da rimuovere
     */
    public static void rimuoviToDo(TitoloBacheca titolo, ToDo todo) {
        Bacheca b = bacheche.get(titolo);
        if (b != null) {
            b.getToDoList().remove(todo);
        }
    }

    /**
     * Resetta completamente lo stato del model manager (tutte le bacheche rimosse).
     */
    public static void reset() {
        bacheche.clear();
    }

    /**
     * Lista temporanea di richieste pendenti da gestire.
     */
    private static List<Richiesta> richiestePendenti = new ArrayList<>();

    /**
     * Imposta l'elenco delle richieste pendenti (in attesa di approvazione).
     *
     * @param richieste Lista delle richieste
     */
    public static void setRichiestePendenti(List<Richiesta> richieste) {
        richiestePendenti = richieste;
    }

    /**
     * Restituisce l'elenco delle richieste pendenti.
     *
     * @return Lista delle richieste in attesa
     */
    public static List<Richiesta> getRichiestePendenti() {
        return richiestePendenti;
    }
}
