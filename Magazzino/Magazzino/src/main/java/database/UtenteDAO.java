package database;

import entity.Utente;
import jakarta.persistence.EntityManager;

/**
 * Classe DAO per la gestione della persistenza dell'entità Utente.
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

    public Utente trovaPerEmail(String email) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            return em.find(Utente.class, email);
        } finally {
            em.close();
        }
    }
}
