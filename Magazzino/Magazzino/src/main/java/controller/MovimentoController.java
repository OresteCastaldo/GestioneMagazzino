package controller;

import database.MovimentoDAO;
import database.ProdottoDAO;
import database.JpaUtil;
import entity.Movimento;
import entity.Prodotto;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller dedicato alla gestione dei movimenti di magazzino.
 * Ispirato alla struttura di IDS_Magazzino.MovimentoController,
 * adattato allo stack Hibernate/JPA del progetto.
 *
 * Coordina le operazioni di carico e scarico, aggiornando
 * automaticamente le quantità dei prodotti coinvolti.
 */
public class MovimentoController {

    private MovimentoDAO movimentoDAO;
    private ProdottoDAO prodottoDAO;

    public MovimentoController() {
        this.movimentoDAO = new MovimentoDAO();
        this.prodottoDAO = new ProdottoDAO();
    }

    /**
     * Registra un movimento di carico o scarico.
     * Aggiorna automaticamente la quantità disponibile del prodotto associato.
     * @param movimento il movimento da registrare
     */
    public void registraMovimento(Movimento movimento) {
        String prodottoId = movimento.getProdottoId();
        if (prodottoId != null) {
            Prodotto p = prodottoDAO.trovaPerId(prodottoId);
            if (p != null) {
                // Collega la vera entità al movimento
                movimento.setProdotto(p);

                int nuovaQta;
                if (movimento.getTipologia() != null
                        && movimento.getTipologia().equalsIgnoreCase("CARICO")) {
                    nuovaQta = p.getQuantitaDisponibile() + movimento.getQuantita();
                } else {
                    nuovaQta = p.getQuantitaDisponibile() - movimento.getQuantita();
                }
                p.setQuantitaDisponibile(nuovaQta);
                prodottoDAO.aggiorna(p);
            }
        }
        movimentoDAO.salva(movimento);
    }

    /**
     * Restituisce i movimenti filtrati per un determinato prodotto.
     * @param prodottoId l'ID del prodotto
     * @return la lista dei movimenti associati al prodotto
     */
    public List<Movimento> getMovimentiFiltrati(String prodottoId) {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            return em.createQuery(
                "SELECT m FROM Movimento m WHERE m.prodotto.codiceId = :prodottoId", Movimento.class)
                .setParameter("prodottoId", prodottoId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Restituisce i movimenti filtrati tramite ricerca flessibile
     * (cerca prodotti per ID, nome o scaffale, poi recupera i movimenti associati).
     * @param termine il termine di ricerca
     * @return la lista aggregata di tutti i movimenti trovati
     */
    public List<Movimento> getMovimentiFiltratiFlessibili(String termine) {
        List<Prodotto> prodotti = prodottoDAO.ricercaMultiplaFlessibile(termine);
        List<Movimento> risultati = new ArrayList<>();
        if (prodotti != null) {
            for (Prodotto p : prodotti) {
                List<Movimento> movs = getMovimentiFiltrati(p.getCodiceId());
                if (movs != null) {
                    risultati.addAll(movs);
                }
            }
        }
        return risultati;
    }
}
