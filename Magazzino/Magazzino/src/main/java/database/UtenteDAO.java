package database;

import entity.Utente;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Classe DAO per la gestione della persistenza dell'entità Utente.
 * La PK è ora id_utente (numerica, autogenerata).
 * L'email è un campo unique ma non è più la chiave primaria.
 */
public class UtenteDAO {

    public void salva(Utente utente) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(utente);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Cerca un utente per email (usato per verificare duplicati in fase di registrazione).
     *  email l'email da cercare
     * ritorna  l'utente trovato, o null se non esiste
     */
    public Utente trovaPerEmail(String email) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            List<Utente> risultati = em.createQuery(
                    "SELECT u FROM Utente u WHERE u.email = :email", Utente.class)
                    .setParameter("email", email)
                    .getResultList();
            return risultati.isEmpty() ? null : risultati.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Cerca un utente per email e password (usato per l'autenticazione in fase di login).
     * email l'email dell'utente
     *  password la password dell'utente
     * ritorna l'utente trovato se le credenziali corrispondono, null altrimenti
     */
    public Utente trovaPerEmailEPassword(String email, String password) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            List<Utente> risultati = em.createQuery(
                    "SELECT u FROM Utente u WHERE u.email = :email AND u.password = :password", Utente.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getResultList();
            return risultati.isEmpty() ? null : risultati.get(0);
        } finally {
            em.close();
        }
    }
}
