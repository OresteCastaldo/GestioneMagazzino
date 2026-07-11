package controller;

import database.UtenteDAO;
import entity.Responsabile;
import entity.Utente;

/**
 * Controller dedicato alla gestione dell'autenticazione e della sessione utente.
 * Valida l'accesso verificando email e password.
 */
public class LoginController {

    private UtenteDAO utenteDAO;
    private Utente utenteCorrente;

    public LoginController() {
        this.utenteDAO = new UtenteDAO();
    }

    /**
     * Effettua il login verificando email e password tramite UtenteDAO (Hibernate).
     * @param email l'email dell'utente
     * @param password la password dell'utente
     * @return true se l'utente è stato trovato e la password corrisponde
     */
    public boolean effettuaLogin(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        Utente u = utenteDAO.trovaPerEmailEPassword(email.trim(), password.trim());
        if (u != null) {
            this.utenteCorrente = u;
            return true;
        }
        return false;
    }

    /**
     * Restituisce l'utente attualmente loggato nel sistema.
     * @return l'utente corrente, o null se nessuno è loggato
     */
    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    /**
     * Restituisce il ruolo dell'utente corrente come stringa.
     * Nasconde internamente il controllo instanceof alla Boundary.
     * @return "RESPONSABILE" o "OPERATORE", o null se nessuno è loggato
     */
    public String getRuoloUtenteCorrente() {
        if (utenteCorrente == null) {
            return null;
        }
        return (utenteCorrente instanceof Responsabile) ? "RESPONSABILE" : "OPERATORE";
    }

    /**
     * Esegue il logout resettando l'utente corrente.
     */
    public void logout() {
        this.utenteCorrente = null;
    }
}
