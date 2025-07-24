package org.example.model;

/**
 * Classe che rappresenta un utente del sistema.
 * Contiene informazioni essenziali per l'autenticazione.
 */
public class User {

    /**
     * Nome utente (identificativo univoco dell’utente).
     */
    private String username;

    /**
     * Password dell’utente (in chiaro o eventualmente da criptare).
     */
    private String password;

    /**
     * Costruttore per creare un oggetto utente con credenziali.
     *
     * @param username Nome utente
     * @param password Password dell’utente
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Restituisce il nome utente.
     *
     * @return username dell’utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce la password dell’utente.
     * Nota: in ambienti reali è consigliato non restituire mai la password in chiaro.
     *
     * @return password dell’utente
     */
    public String getPassword() {
        return password;
    }
}
