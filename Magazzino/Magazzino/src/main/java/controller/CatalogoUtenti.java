package controller;

import database.UtenteDAO;
import entity.Utente;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller per la gestione degli utenti (Operatori e Responsabili).
 */
public class CatalogoUtenti {

    private List<Utente> listaUtenti;
    private UtenteDAO utenteDAO;

    public CatalogoUtenti() {
        this.listaUtenti = new ArrayList<>();
        this.utenteDAO = new UtenteDAO();
    }

    public Utente TrovaUtente(String email) {
        return utenteDAO.trovaPerEmail(email);
    }

    public boolean CheckMail(String email) {
        return utenteDAO.trovaPerEmail(email) != null;
    }

    public void AggiungiUtente(Utente utente) {
        utenteDAO.salva(utente);
        listaUtenti.add(utente);
    }
}
