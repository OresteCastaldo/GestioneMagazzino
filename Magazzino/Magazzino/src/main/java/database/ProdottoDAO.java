package database;

import entity.Prodotto;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Classe DAO per la gestione della persistenza dell'entità Prodotto.
 */
public class ProdottoDAO {

    public void salva(Prodotto prodotto) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(prodotto);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void aggiorna(Prodotto prodotto) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(prodotto);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Prodotto trovaPerId(String codiceId) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            return em.find(Prodotto.class, codiceId);
        } finally {
            em.close();
        }
    }

    public void elimina(String codiceId) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Prodotto prodotto = em.find(Prodotto.class, codiceId);
            if (prodotto != null) {
                em.remove(prodotto);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Prodotto> trovaTutti() {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Prodotto p", Prodotto.class).getResultList();
        } finally {
            em.close();
        }
    }
}
