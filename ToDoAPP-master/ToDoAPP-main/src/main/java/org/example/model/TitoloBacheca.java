package org.example.model;

/**
 * Enum che rappresenta i titoli disponibili per una bacheca.
 * Ogni valore ha una rappresentazione leggibile (label).
 */
public enum TitoloBacheca {

    /**
     * Bacheca dedicata all'ambito universitario.
     */
    UNIVERSITA("Università"),

    /**
     * Bacheca dedicata al lavoro o progetti professionali.
     */
    LAVORO("Lavoro"),

    /**
     * Bacheca per attività ricreative e personali.
     */
    TEMPO_LIBERO("Tempo Libero");

    /**
     * Etichetta leggibile associata al titolo.
     */
    private final String label;

    /**
     * Costruttore dell'enum.
     *
     * @param label Etichetta descrittiva da mostrare all'utente
     */
    TitoloBacheca(String label) {
        this.label = label;
    }

    /**
     * Restituisce l'etichetta leggibile associata al titolo.
     *
     * @return Stringa da mostrare all'utente
     */
    @Override
    public String toString() {
        return label;
    }

    /**
     * Restituisce il nome costante dell'enum, utile come chiave nel database.
     *
     * @return Nome della costante enum (es. "LAVORO")
     */
    public String getKey() {
        return name();
    }
}
