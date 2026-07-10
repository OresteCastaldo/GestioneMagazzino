package controller;

import database.UtenteDAO;
import entity.Utente;

/**
 * Controller dedicato alla gestione dell'autenticazione e della sessione utente.
 * Ispirato alla struttura di IDS_Magazzino.LoginController,
 * adattato allo stack Hibernate/JPA del progetto.
 */
public class LoginController {

    private UtenteDAO utenteDAO;
    private Utente utenteCorrente;

    public LoginController() {
        this.utenteDAO = new UtenteDAO();
    }

    /**
     * Effettua il login cercando l'utente per email tramite UtenteDAO (Hibernate).
     * @param email l'email dell'utente
     * @return true se l'utente è stato trovato e autenticato
     */
    public boolean effettuaLogin(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        Utente u = utenteDAO.trovaPerEmail(email.trim());
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
     * Esegue il logout resettando l'utente corrente.
     */
    public void logout() {
        this.utenteCorrente = null;
    }
}
