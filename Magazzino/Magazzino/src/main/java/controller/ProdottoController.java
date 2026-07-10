package controller;

import database.ProdottoDAO;
import entity.Prodotto;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Controller dedicato alla gestione completa del ciclo di vita dei prodotti.
 * Ispirato alla struttura di IDS_Magazzino.ProdottoController,
 * adattato allo stack Hibernate/JPA del progetto.
 *
 * Unifica la logica precedentemente distribuita tra CatalogoProdotto
 * e la parte prodotti di GestoreMagazzino.
 */
public class ProdottoController {

    private ProdottoDAO prodottoDAO;

    public ProdottoController() {
        this.prodottoDAO = new ProdottoDAO();
    }

    /**
     * Verifica se un prodotto con il codice specificato esiste nel database.
     * @param codiceId il codice identificativo del prodotto
     * @return true se il prodotto esiste
     */
    public boolean verificaEsistenza(String codiceId) {
        return prodottoDAO.trovaPerId(codiceId) != null;
    }

    /**
     * Cerca un prodotto per codice ID esatto.
     * @param codiceId il codice identificativo del prodotto
     * @return il prodotto trovato, o null se non esiste
     */
    public Prodotto cercaProdotto(String codiceId) {
        if (verificaEsistenza(codiceId)) {
            return prodottoDAO.trovaPerId(codiceId);
        }
        return null;
    }

    /**
     * Ricerca flessibile per ID, nome o scaffale (restituisce il primo risultato).
     * @param termine il termine di ricerca
     * @return il primo prodotto trovato, o null
     */
    public Prodotto cercaProdottoFlessibile(String termine) {
        return prodottoDAO.ricercaFlessibile(termine);
    }

    /**
     * Ricerca flessibile multipla per ID, nome o scaffale.
     * @param termine il termine di ricerca
     * @return la lista dei prodotti trovati
     */
    public List<Prodotto> ricercaMultiplaFlessibile(String termine) {
        return prodottoDAO.ricercaMultiplaFlessibile(termine);
    }

    /**
     * Inserisce un nuovo prodotto nel catalogo.
     * Se il codice ID non è impostato, ne genera uno univoco.
     * @param prodotto il prodotto da inserire
     */
    public void inserisciProdotto(Prodotto prodotto) {
        if (prodotto.getCodiceId() == null || prodotto.getCodiceId().isEmpty()) {
            prodotto.setCodiceId(generaIdUnivoco());
        }
        prodottoDAO.salva(prodotto);
    }

    /**
     * Valida i dati del prodotto prima dell'inserimento o della modifica.
     * @return null se tutti i controlli passano, altrimenti il messaggio di errore
     */
    public String validaDatiProdotto(String codiceId, String nome, String descrizione,
            String categoria, String scaffale, String sogliaMinTxt, String quantitaTxt) {
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
        if (descrizione != null && descrizione.trim().length() > 500)
            return "Descrizione troppo lunga (max 500 caratteri)";

        // 4. Validazione Categoria
        if (categoria == null || categoria.trim().isEmpty() || categoria.equals("Seleziona..."))
            return "Selezionare una Categoria";
        List<String> categorieValide = Arrays.asList("Frutta", "Verdura", "Carne", "Bevande", "Surgelati");
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

    /**
     * Salva le modifiche a un prodotto esistente tramite Hibernate merge.
     * @param prodotto il prodotto con i dati aggiornati
     */
    public void salvaModifiche(Prodotto prodotto) {
        prodottoDAO.aggiorna(prodotto);
    }

    /**
     * Elimina un prodotto e i movimenti correlati.
     * @param codiceId il codice del prodotto da eliminare
     */
    public void eliminaProdotto(String codiceId) {
        prodottoDAO.elimina(codiceId);
    }

    /**
     * Aggiorna la quantità disponibile di un prodotto.
     * @param codiceId il codice del prodotto
     * @param nuovaQuantita la nuova quantità da impostare
     */
    public void aggiornaQuantita(String codiceId, int nuovaQuantita) {
        Prodotto p = prodottoDAO.trovaPerId(codiceId);
        if (p != null) {
            p.setQuantitaDisponibile(nuovaQuantita);
            prodottoDAO.aggiorna(p);
        }
    }

    /**
     * Genera un ID univoco a 6 cifre per un nuovo prodotto.
     * @return l'ID generato, garantito univoco nel database
     */
    private String generaIdUnivoco() {
        String id;
        Random rnd = new Random();
        do {
            int number = rnd.nextInt(1000000); // Da 0 a 999999
            id = String.format("%06d", number);
        } while (prodottoDAO.trovaPerId(id) != null);
        return id;
    }
}
