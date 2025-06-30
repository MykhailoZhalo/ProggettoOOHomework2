package org.example.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDo {
    private String titolo;
    private String descrizione;
    private Date scadenza;
    private boolean completato;
    private int posizione;
    private String url;
    private String coloreSfondo;
    private String immaginePath;
    private String autore;
    private List<String> listaUtenti;

    public ToDo(String titolo, String descrizione, Date scadenza) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.scadenza = scadenza;
        this.completato = false;
        this.listaUtenti = new ArrayList<>();
    }

    // Getter e setter

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

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
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

    public List<String> getListaUtenti() {
        return listaUtenti;
    }

    public void setListaUtenti(List<String> listaUtenti) {
        this.listaUtenti = listaUtenti;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public boolean isUtenteAutore(String username) {
        return autore != null && autore.equals(username);
    }

    public boolean isUtentePartecipante(String username) {
        return listaUtenti.contains(username) && !isUtenteAutore(username);
    }

    @Override
    public String toString() {
        return titolo + " (Scadenza: " + scadenza + ")";
    }
}
