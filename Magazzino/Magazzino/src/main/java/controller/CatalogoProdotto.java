package controller;

import database.ProdottoDAO;
import entity.Prodotto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Controller per la gestione dell'anagrafica dei prodotti.
 */
public class CatalogoProdotto {

    private List<Prodotto> listaProdotti;
    private ProdottoDAO prodottoDAO;

    public CatalogoProdotto() {
        this.listaProdotti = new ArrayList<>();
        this.prodottoDAO = new ProdottoDAO();
    }

    public boolean VerificaEsistenzaProdotto(String codiceId) {
        return prodottoDAO.trovaPerId(codiceId) != null;
    }

    public Prodotto TrovaProdotto(String codiceId) {
        return prodottoDAO.trovaPerId(codiceId);
    }

    public void AggiornaQuantita(String codiceId, int nuovaQuantita) {
        Prodotto p = prodottoDAO.trovaPerId(codiceId);
        if (p != null) {
            p.setQuantitaDisponibile(nuovaQuantita);
            VerificaSogliaMinima(p);
            prodottoDAO.aggiorna(p);
        }
    }

    private void VerificaSogliaMinima(Prodotto p) {
        if (p.getQuantitaDisponibile() < p.getSogliaMinDisponibile()) {
            ContrassegnaSottoScorta(p);
        }
    }

    private void ContrassegnaSottoScorta(Prodotto p) {
        // Esempio: Innesca il SistemaNotifiche
    }

    private String generaIdUnivoco() {
        return UUID.randomUUID().toString();
    }

    public void InserisciInCatalogo(Prodotto prodotto) {
        if (prodotto.getCodiceId() == null || prodotto.getCodiceId().isEmpty()) {
            prodotto.setCodiceId(generaIdUnivoco());
        }
        prodottoDAO.salva(prodotto);
        listaProdotti.add(prodotto);
    }

    public void ValidaEModificaProdotto(Prodotto prodotto) {
        AggiornaDatiProdotto(prodotto);
    }

    private void AggiornaDatiProdotto(Prodotto prodotto) {
        prodottoDAO.aggiorna(prodotto);
    }

    public void EliminaProdotto(String codiceId) {
        prodottoDAO.elimina(codiceId);
        RimuoviDaCatalogo(codiceId);
    }

    private void RimuoviDaCatalogo(String codiceId) {
        listaProdotti.removeIf(p -> p.getCodiceId().equals(codiceId));
    }
}
