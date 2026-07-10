package database;

import entity.Movimento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
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

    public void eliminaMovimentiPerProdotto(String prodottoId, EntityManager em) {
        em.createQuery("DELETE FROM Movimento m WHERE m.prodotto.codiceId = :id")
          .setParameter("id", prodottoId)
          .executeUpdate();
    }

    /**
     * Ricerca movimenti con filtri opzionali (date e tipo) per un dato prodotto.
     * La query JPQL viene composta dinamicamente in base ai parametri non nulli.
     * @param codiceProdotto ID del prodotto (obbligatorio)
     * @param dataInizio data di inizio periodo (opzionale)
     * @param dataFine data di fine periodo (opzionale)
     * @param tipoMovimento tipo CARICO/SCARICO (opzionale, null o vuoto = tutti)
     * @return lista dei movimenti che soddisfano i criteri
     */
    public List<Movimento> ricercaStoricoConFiltri(String codiceProdotto, Date dataInizio, Date dataFine, String tipoMovimento) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT m FROM Movimento m WHERE m.prodotto.codiceId = :codice");

            if (dataInizio != null) {
                jpql.append(" AND m.data >= :dataInizio");
            }
            if (dataFine != null) {
                jpql.append(" AND m.data <= :dataFine");
            }
            if (tipoMovimento != null && !tipoMovimento.trim().isEmpty()) {
                jpql.append(" AND m.tipologia = :tipo");
            }

            jpql.append(" ORDER BY m.data DESC");

            TypedQuery<Movimento> query = em.createQuery(jpql.toString(), Movimento.class);
            query.setParameter("codice", codiceProdotto);

            if (dataInizio != null) {
                query.setParameter("dataInizio", dataInizio);
            }
            if (dataFine != null) {
                query.setParameter("dataFine", dataFine);
            }
            if (tipoMovimento != null && !tipoMovimento.trim().isEmpty()) {
                query.setParameter("tipo", tipoMovimento.trim());
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }

}
