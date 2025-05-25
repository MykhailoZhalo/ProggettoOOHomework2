package org.example.model;


import java.util.Date;
import java.util.List;

public class ToDo {
    private int ID;
    private String titolo;
    private String descrizione;
    private Date scadenza;
    private boolean completato;
    private int posizione;
    private String immagine;
    private String URL;
    private String coloreSfondo;
    private String coloreTitolo;
    private Mansione mansione;
    private List<String> listaUtenti;

    public ToDo(String titolo, String descrizione, Date scadenza) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.scadenza = scadenza;
        this.completato = false;
    }

    // ===== Getters =====
    public int getID() { return ID; }
    public String getTitolo() { return titolo; }
    public String getDescrizione() { return descrizione; }
    public Date getScadenza() { return scadenza; }
    public boolean isCompletato() { return completato; }
    public int getPosizione() { return posizione; }
    public String getImmagine() { return immagine; }
    public String getURL() { return URL; }
    public String getColoreSfondo() { return coloreSfondo; }
    public String getColoreTitolo() { return coloreTitolo; }
    public Mansione getMansione() { return mansione; }
    public List<String> getListaUtenti() { return listaUtenti; }

    // ===== Setters =====
    public void setID(int ID) { this.ID = ID; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public void setScadenza(Date scadenza) { this.scadenza = scadenza; }
    public void setCompletato(boolean completato) { this.completato = completato; }
    public void setPosizione(int posizione) { this.posizione = posizione; }
    public void setImmagine(String immagine) { this.immagine = immagine; }
    public void setURL(String URL) { this.URL = URL; }
    public void setColoreSfondo(String coloreSfondo) { this.coloreSfondo = coloreSfondo; }
    public void setColoreTitolo(String coloreTitolo) { this.coloreTitolo = coloreTitolo; }
    public void setMansione(Mansione mansione) { this.mansione = mansione; }
    public void setListaUtenti(List<String> listaUtenti) { this.listaUtenti = listaUtenti; }
}
