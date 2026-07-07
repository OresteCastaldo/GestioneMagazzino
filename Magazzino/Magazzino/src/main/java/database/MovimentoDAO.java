package database;

import entity.Movimento;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Classe DAO per la gestione della persistenza dell'entità Movimento.
 */
public class MovimentoDAO {

    public void salva(Movimento movimento) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(movimento);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Movimento trovaPerId(Long id) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            return em.find(Movimento.class, id);
        } finally {
            em.close();
        }
    }

    public List<Movimento> trovaTutti() {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Movimento m", Movimento.class).getResultList();
        } finally {
            em.close();
        }
    }
    

}
