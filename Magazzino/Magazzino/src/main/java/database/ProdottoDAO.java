package database;

import entity.Prodotto;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Classe DAO per la gestione della persistenza dell'entita Prodotto.
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

    public Prodotto ricercaFlessibile(String termine) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            List<Prodotto> risultati = em.createQuery("SELECT p FROM Prodotto p WHERE p.codiceId = :termine OR p.nome = :termine OR p.scaffale = :termine", Prodotto.class)
                     .setParameter("termine", termine)
                     .getResultList();
            return risultati.isEmpty() ? null : risultati.get(0);
        } finally {
            em.close();
        }
    }

    public List<Prodotto> ricercaMultiplaFlessibile(String termine) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Prodotto p WHERE p.codiceId = :termine OR p.nome = :termine OR p.scaffale = :termine", Prodotto.class)
                     .setParameter("termine", termine)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean esisteNome(String nome, String codiceEscluso) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            String q = "SELECT count(p) FROM Prodotto p WHERE p.nome = :nome";
            if (codiceEscluso != null && !codiceEscluso.isEmpty()) q += " AND p.codiceId <> :codId";
            var query = em.createQuery(q, Long.class).setParameter("nome", nome);
            if (codiceEscluso != null && !codiceEscluso.isEmpty()) query.setParameter("codId", codiceEscluso);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public boolean esisteScaffale(String scaffale, String codiceEscluso) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            String q = "SELECT count(p) FROM Prodotto p WHERE p.scaffale = :scaffale";
            if (codiceEscluso != null && !codiceEscluso.isEmpty()) q += " AND p.codiceId <> :codId";
            var query = em.createQuery(q, Long.class).setParameter("scaffale", scaffale);
            if (codiceEscluso != null && !codiceEscluso.isEmpty()) query.setParameter("codId", codiceEscluso);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public void elimina(String codiceId) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            
            // 1. Elimina preventivamente i movimenti correlati nella STESSA transazione
            MovimentoDAO movimentoDAO = new MovimentoDAO();
            movimentoDAO.eliminaMovimentiPerProdotto(codiceId, em);
            
            // 2. Elimina il prodotto
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
