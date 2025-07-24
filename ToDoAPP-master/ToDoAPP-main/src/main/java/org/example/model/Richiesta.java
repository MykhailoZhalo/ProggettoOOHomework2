package org.example.model;

import java.sql.Timestamp;

/**
 * Rappresenta una richiesta di condivisione di un ToDo tra utenti.
 * Ogni richiesta ha uno stato, un mittente, un destinatario e un riferimento al ToDo condiviso.
 */
public class Richiesta {

    /**
     * Identificativo univoco della richiesta.
     */
    private int id;

    /**
     * ID del ToDo a cui la richiesta fa riferimento.
     */
    private int idToDo;

    /**
     * Username dell'utente che ha inviato la richiesta.
     */
    private String mittente;

    /**
     * Username dell'utente destinatario della richiesta.
     */
    private String destinatario;

    /**
     * Titolo della bacheca associata al ToDo.
     */
    private String titoloBacheca;

    /**
     * Stato della richiesta: "in_attesa", "accettata" o "rifiutata".
     */
    private String stato;

    /**
     * Timestamp della data in cui è stata creata la richiesta.
     */
    private Timestamp dataRichiesta;

    /**
     * Costruttore base per la creazione di una nuova richiesta (stato iniziale = "in_attesa").
     *
     * @param idToDo        ID del ToDo da condividere
     * @param mittente      Utente che invia la richiesta
     * @param destinatario  Utente destinatario della richiesta
     * @param titoloBacheca Titolo della bacheca dove si trova il ToDo
     */
    public Richiesta(int idToDo, String mittente, String destinatario, String titoloBacheca) {
        this.idToDo = idToDo;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.titoloBacheca = titoloBacheca;
        this.stato = "in_attesa";
        this.dataRichiesta = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Costruttore completo, usato per ricostruire l'oggetto dal database.
     *
     * @param id            ID della richiesta
     * @param idToDo        ID del ToDo
     * @param mittente      Mittente della richiesta
     * @param destinatario  Destinatario della richiesta
     * @param titoloBacheca Titolo della bacheca
     * @param stato         Stato attuale della richiesta
     * @param dataRichiesta Data e ora della richiesta
     */
    public Richiesta(int id, int idToDo, String mittente, String destinatario, String titoloBacheca, String stato, Timestamp dataRichiesta) {
        this.id = id;
        this.idToDo = idToDo;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.titoloBacheca = titoloBacheca;
        this.stato = stato;
        this.dataRichiesta = dataRichiesta;
    }

    // --- Getter standard ---

    /**
     * @return ID della richiesta
     */
    public int getId() {
        return id;
    }

    /**
     * @return ID del ToDo condiviso
     */
    public int getIdToDo() {
        return idToDo;
    }

    /**
     * @return Username del mittente
     */
    public String getMittente() {
        return mittente;
    }

    /**
     * @return Username del destinatario
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * @return Titolo della bacheca di origine
     */
    public String getTitoloBacheca() {
        return titoloBacheca;
    }

    /**
     * @return Stato della richiesta
     */
    public String getStato() {
        return stato;
    }

    /**
     * @return Timestamp della richiesta
     */
    public Timestamp getDataRichiesta() {
        return dataRichiesta;
    }

    /**
     * Rappresentazione testuale dell'oggetto Richiesta.
     *
     * @return Stringa descrittiva della richiesta
     */
    @Override
    public String toString() {
        return "Richiesta{" +
                "id=" + id +
                ", idToDo=" + idToDo +
                ", mittente='" + mittente + '\'' +
                ", destinatario='" + destinatario + '\'' +
                ", titoloBacheca='" + titoloBacheca + '\'' +
                ", stato='" + stato + '\'' +
                ", dataRichiesta=" + dataRichiesta +
                '}';
    }
}
