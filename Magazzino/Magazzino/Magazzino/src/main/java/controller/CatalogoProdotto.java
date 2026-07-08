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

    public Prodotto RicercaFlessibile(String termine) {
        return prodottoDAO.ricercaFlessibile(termine);
    }

    public List<Prodotto> RicercaMultiplaFlessibile(String termine) {
        return prodottoDAO.ricercaMultiplaFlessibile(termine);
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
        String id;
        java.util.Random rnd = new java.util.Random();
        do {
            int number = rnd.nextInt(1000000); // Da 0 a 999999
            id = String.format("%06d", number);
        } while (prodottoDAO.trovaPerId(id) != null);
        return id;
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

    public String validaDatiProdotto(String codiceId, String nome, String descrizione, String categoria, String scaffale, String sogliaMinTxt, String quantitaTxt) {
        // 1. Validazione Nome
        if (nome == null || nome.trim().isEmpty()) {
            return "Nome non valido";
        }
        if (nome.trim().length() > 50) return "Nome troppo lungo (max 50 caratteri)";
        if (prodottoDAO.esisteNome(nome.trim(), codiceId)) return "Nome prodotto già in uso";

        // 2. Validazione Scaffale
        if (scaffale == null || scaffale.trim().isEmpty()) return "Il campo Scaffale è obbligatorio";
        if (scaffale.trim().length() > 6) return "Scaffale troppo lungo (max 6 caratteri)";
        if (prodottoDAO.esisteScaffale(scaffale.trim(), codiceId)) return "Scaffale prodotto già in uso";

        // 3. Validazione Descrizione
        if (descrizione != null && descrizione.trim().length() > 500) return "Descrizione troppo lunga (max 500 caratteri)";

        // 4. Validazione Categoria
        if (categoria == null || categoria.trim().isEmpty() || categoria.equals("Seleziona...")) return "Selezionare una Categoria";
        java.util.List<String> categorieValide = java.util.Arrays.asList("Frutta", "Verdura", "Carne", "Bevande", "Surgelati");
        if (!categorieValide.contains(categoria.trim())) return "Categoria selezionata non valida";

        // 5. Validazione Soglia Minima
        if (sogliaMinTxt != null && !sogliaMinTxt.trim().isEmpty()) {
            try {
                int soglia = Integer.parseInt(sogliaMinTxt.trim());
                if (soglia < 0) return "La soglia minima non può essere negativa";
            } catch (NumberFormatException e) {
                return "Inserire un numero intero per la soglia";
            }
        }

        // 6. Validazione Quantità
        if (quantitaTxt != null && !quantitaTxt.trim().isEmpty()) {
            try {
                int quantita = Integer.parseInt(quantitaTxt.trim());
                if (quantita < 0) return "La quantità non può essere negativa";
            } catch (NumberFormatException e) {
                return "Inserire un numero intero per la quantità";
            }
        }

        return null; // Tutti i controlli passati
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
