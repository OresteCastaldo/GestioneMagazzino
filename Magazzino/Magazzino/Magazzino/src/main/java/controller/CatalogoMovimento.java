package controller;

import database.MovimentoDAO;
import database.JpaUtil;
import entity.Movimento;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller per la gestione e lo storico dei movimenti.
 */
public class CatalogoMovimento {

    private List<Movimento> listaMovimenti;
    private MovimentoDAO movimentoDAO;

    public CatalogoMovimento() {
        this.listaMovimenti = new ArrayList<>();
        this.movimentoDAO = new MovimentoDAO();
    }

    public Movimento RichiediMovimento(Long id) {
        return movimentoDAO.trovaPerId(id);
    }

    private List<Movimento> EstraiMovimentiAssociati(String prodottoId) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Movimento m WHERE m.prodottoId = :prodottoId", Movimento.class)
                     .setParameter("prodottoId", prodottoId)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Movimento> RichiediMovimentiFiltrati(String prodottoId) {
        List<Movimento> associati = EstraiMovimentiAssociati(prodottoId);
        return ElaboraFiltraggio(associati);
    }

    private List<Movimento> ElaboraFiltraggio(List<Movimento> movimenti) {
        // Implementazione base del filtraggio
        return movimenti;
    }

    public void SalvaMovimento(Movimento movimento) {
        movimentoDAO.salva(movimento);
        listaMovimenti.add(movimento);
    }
}
