package controller;

import database.UtenteDAO;
import dto.OperatoreDTO;
import dto.ResponsabileDTO;
import entity.Operatore;
import entity.Responsabile;
import entity.Utente;

/**
 * Controller dedicato alla registrazione di nuovi utenti nel sistema.
 * Esegue la validazione dei dati inseriti prima di procedere col salvataggio.
 * La PK (id_utente) è ora autogenerata da Hibernate (@GeneratedValue),
 * quindi non serve più generare UUID manualmente.
 */
public class RegistrationController {

    private UtenteDAO utenteDAO;

    public RegistrationController() {
        this.utenteDAO = new UtenteDAO();
    }

    /**
     * Registra un nuovo operatore a partire dal suo DTO.
     * Mappa i dati sull'entità concreta e delega il salvataggio.
     * parametro dto i dati dell'operatore provenienti dalla GUI
     * return true se la registrazione ha successo
     * lancia Eccezione in caso di validazione fallita
     */
    public boolean registraNuovoOperatore(OperatoreDTO dto) throws Exception {
        Operatore op = new Operatore();
        op.setNome(dto.getNome());
        op.setCognome(dto.getCognome());
        op.setEmail(dto.getEmail());
        op.setPassword(dto.getPassword());
        op.setRuolo("OPERATORE");
        return registraNuovoUtente(op);
    }

    /**
     * Registra un nuovo responsabile a partire dal suo DTO.
     * Mappa i dati sull'entità concreta e delega il salvataggio.
     * parametro dto i dati del responsabile provenienti dalla GUI
     * retrurn true se la registrazione ha successo
     * Lancia Eccezione in caso di validazione fallita
     */
    public boolean registraNuovoResponsabile(ResponsabileDTO dto) throws Exception {
        Responsabile resp = new Responsabile();
        resp.setNome(dto.getNome());
        resp.setCognome(dto.getCognome());
        resp.setEmail(dto.getEmail());
        resp.setPassword(dto.getPassword());
        resp.setRuolo("RESPONSABILE");
        return registraNuovoUtente(resp);
    }

    /**
     * Valida i dati e registra un nuovo utente nel sistema tramite Hibernate.
     * Reso privato per nascondere le entità al livello Boundary.
     * parametro utente l'utente da registrare (Operatore o Responsabile)
     * return true se la registrazione è andata a buon fine
     * Lancia Eccezione se la validazione fallisce o l'utente risulta già registrato
     */
    private boolean registraNuovoUtente(Utente utente) throws Exception {
        // Verifica email duplicata
        if (utenteDAO.trovaPerEmail(utente.getEmail().trim()) != null) {
            throw new Exception("Questa email è già associata ad un utente registrato.");
        }

        // Persistenza con Hibernate (id_utente è autogenerato da @GeneratedValue)
        utenteDAO.salva(utente);
        return true;
    }
}
