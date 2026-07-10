package controller;

import database.UtenteDAO;
import entity.Utente;

/**
 * Controller dedicato alla registrazione di nuovi utenti nel sistema.
 * Ispirato alla struttura di IDS_Magazzino.RegistrationController,
 * adattato allo stack Hibernate/JPA del progetto.
 *
 * Esegue la validazione dei dati inseriti prima di procedere col salvataggio.
 */
public class RegistrationController {

    private UtenteDAO utenteDAO;

    public RegistrationController() {
        this.utenteDAO = new UtenteDAO();
    }

    /**
     * Valida i dati e registra un nuovo utente nel sistema tramite Hibernate.
     * @param utente l'utente da registrare (Operatore o Responsabile)
     * @return true se la registrazione è andata a buon fine
     * @throws Exception se la validazione fallisce o l'utente risulta già registrato
     */
    public boolean registraNuovoUtente(Utente utente) throws Exception {
        // Validazione dati
        if (utente == null || utente.getEmail() == null || utente.getEmail().trim().isEmpty()) {
            throw new Exception("Dati utente non validi.");
        }
        if (utente.getNome() == null || utente.getNome().trim().isEmpty()) {
            throw new Exception("Il nome non può essere vuoto.");
        }
        if (utente.getCognome() == null || utente.getCognome().trim().isEmpty()) {
            throw new Exception("Il cognome non può essere vuoto.");
        }

        // Verifica email duplicata
        if (utenteDAO.trovaPerEmail(utente.getEmail().trim()) != null) {
            throw new Exception("Questa email è già associata ad un utente registrato.");
        }

        // Persistenza con Hibernate
        utenteDAO.salva(utente);
        return true;
    }
}
