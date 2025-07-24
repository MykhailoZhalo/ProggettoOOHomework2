package org.example.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe che rappresenta un'attività (ToDo) all'interno di una bacheca.
 * Contiene informazioni su titolo, descrizione, scadenza, completamento, autore e utenti con cui è condivisa.
 */
public class ToDo {

    /** Identificatore univoco dell'attività (valorizzato dal database). */
    private int id;

    /** Nome utente del proprietario dell'attività (può essere diverso dall'autore se condivisa). */
    private String username;

    /** Titolo dell'attività. */
    private String titolo;

    /** Descrizione dettagliata dell'attività. */
    private String descrizione;

    /** Data di scadenza dell'attività. */
    private Date scadenza;

    /** Flag che indica se l'attività è stata completata. */
    private boolean completato;

    /** Posizione visiva dell'attività nella lista della bacheca. */
    private int posizione;

    /** Eventuale URL associato all'attività. */
    private String url;

    /** Colore di sfondo personalizzato per la visualizzazione. */
    private String coloreSfondo;

    /** Percorso di un'immagine allegata all'attività. */
    private String immaginePath;

    /** Utente che ha creato l'attività. */
    private String autore;

    /** Titolo della bacheca in cui è inserita l'attività. */
    private TitoloBacheca bachecaTitolo;

    /** Lista di username con cui l'attività è condivisa. */
    private List<String> listaUtenti;

    /**
     * Costruttore principale per creare una nuova attività.
     *
     * @param titolo      Titolo del ToDo
     * @param descrizione Descrizione del ToDo
     * @param scadenza    Scadenza dell'attività
     */
    public ToDo(String titolo, String descrizione, Date scadenza) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.scadenza = scadenza;
        this.completato = false;
        this.listaUtenti = new ArrayList<>();
    }

    // --- Getter e Setter ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    public boolean isCompletato() {
        return completato;
    }

    public void setCompletato(boolean completato) {
        this.completato = completato;
    }

    public int getPosizione() {
        return posizione;
    }

    public void setPosizione(int posizione) {
        this.posizione = posizione;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColoreSfondo() {
        return coloreSfondo;
    }

    public void setColoreSfondo(String coloreSfondo) {
        this.coloreSfondo = coloreSfondo;
    }

    public String getImmaginePath() {
        return immaginePath;
    }

    public void setImmaginePath(String immaginePath) {
        this.immaginePath = immaginePath;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public TitoloBacheca getBachecaTitolo() {
        return bachecaTitolo;
    }

    public void setBachecaTitolo(TitoloBacheca bachecaTitolo) {
        this.bachecaTitolo = bachecaTitolo;
    }

    public List<String> getListaUtenti() {
        return listaUtenti;
    }

    public void setListaUtenti(List<String> listaUtenti) {
        this.listaUtenti = listaUtenti;
    }

    /**
     * Verifica se un determinato username corrisponde all'autore del ToDo.
     *
     * @param username Nome utente da verificare
     * @return true se l'utente è l'autore, false altrimenti
     */
    public boolean isUtenteAutore(String username) {
        return autore != null && autore.equals(username);
    }

    /**
     * Rappresentazione testuale dell'attività, utile per la visualizzazione.
     *
     * @return Stringa con titolo e scadenza
     */
    @Override
    public String toString() {
        return titolo + " (Scadenza: " + scadenza + ")";
    }
}
