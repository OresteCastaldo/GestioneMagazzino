package controller;

import database.ProdottoDAO;
import dto.ProdottoDTO;
import entity.Prodotto;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Controller dedicato alla gestione completa del ciclo di vita dei prodotti.
 * Ispirato alla struttura di IDS_Magazzino.ProdottoController,
 * adattato allo stack Hibernate/JPA del progetto.
 * Unifica la logica precedentemente distribuita tra CatalogoProdotto
 * e la parte prodotti di GestoreMagazzino.
 */
public class ProdottoController {

    private ProdottoDAO prodottoDAO;

    public ProdottoController() {
        this.prodottoDAO = new ProdottoDAO();
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
     * Ricerca flessibile per ID, nome o scaffale e restituisce un DTO
     * privo di dipendenze dal livello Entity. Usato dal livello Boundary.
     * @param termine il termine di ricerca
     * @return il ProdottoDTO con tutti i dati per la GUI, o null se non trovato
     */
    public ProdottoDTO cercaProdottoFlessibileDTO(String termine) {
        Prodotto p = cercaProdottoFlessibile(termine);
        if (p != null) {
            return new ProdottoDTO(
                p.getCodiceId(),
                p.getNome(),
                p.getDescrizione(),
                p.getCategoria(),
                p.getScaffale(),
                p.getSogliaMinDisponibile(),
                p.getQuantitaDisponibile(),
                p.isSottoScorta()
            );
        }
        return null;
    }

    /**
     * Inserisce un nuovo prodotto nel catalogo.
     * @param prodotto il prodotto da inserire
     */
    public void inserisciProdotto(Prodotto prodotto) {
        prodotto.setSottoScorta(prodotto.getQuantitaDisponibile() < prodotto.getSogliaMinDisponibile());
        prodottoDAO.salva(prodotto);
    }

    /**
     * Valida le regole di business del prodotto prima dell'inserimento o della modifica.
     * Presume che i dati siano già sintatticamente corretti (validazione Boundary).
     * @return null se tutti i controlli passano, altrimenti il messaggio di errore
     */
    public String validaRegoleBusinessProdotto(ProdottoDTO dto, boolean isInserimento) {
        String codiceId = dto.getCodiceId();
        String nome = dto.getNome();
        String scaffale = dto.getScaffale();
        
        if (isInserimento && prodottoDAO.trovaPerId(codiceId) != null) {
            return "Codice ID già in uso da un altro prodotto";
        }

        if (prodottoDAO.esisteNome(nome, codiceId)) {
            return "Nome prodotto già in uso";
        }

        if (prodottoDAO.esisteScaffale(scaffale, codiceId)) {
            return "Scaffale prodotto già in uso";
        }

        return null; // Tutti i controlli passati
    }

    /**
     * Salva le modifiche a un prodotto esistente tramite Hibernate merge.
     * @param prodotto il prodotto con i dati aggiornati
     */
    public void salvaModifiche(Prodotto prodotto) {
        prodotto.setSottoScorta(prodotto.getQuantitaDisponibile() < prodotto.getSogliaMinDisponibile());
        prodottoDAO.aggiorna(prodotto);
    }

    /**
     * Inserisce un nuovo prodotto a partire da un DTO proveniente dal livello Boundary.
     * Converte il DTO in un'Entity e delega l'inserimento al metodo standard.
     * @param dto il ProdottoDTO con i dati provenienti dalla grafica
     */
    public void inserisciProdottoDaDTO(ProdottoDTO dto) {
        Prodotto p = mappaDTOaEntity(dto);
        inserisciProdotto(p);
    }

    /**
     * Salva le modifiche a un prodotto esistente a partire da un DTO proveniente dal livello Boundary.
     * Converte il DTO in un'Entity e delega il salvataggio al metodo standard.
     * @param dto il ProdottoDTO con i dati aggiornati dalla grafica
     * @return true se il prodotto risulta sotto scorta dopo il salvataggio
     */
    public boolean salvaModificheDaDTO(ProdottoDTO dto) {
        Prodotto p = mappaDTOaEntity(dto);
        salvaModifiche(p);
        return p.isSottoScorta();
    }

    /**
     * Metodo interno per convertire un ProdottoDTO in un'Entity Prodotto.
     * @param dto il DTO da convertire
     * @return l'Entity Prodotto popolata con i dati del DTO
     */
    private Prodotto mappaDTOaEntity(ProdottoDTO dto) {
        Prodotto p = new Prodotto();
        p.setCodiceId(dto.getCodiceId());
        p.setNome(dto.getNome());
        p.setDescrizione(dto.getDescrizione());
        p.setCategoria(dto.getCategoria());
        p.setScaffale(dto.getScaffale());
        p.setSogliaMinDisponibile(dto.getSogliaMinDisponibile());
        p.setQuantitaDisponibile(dto.getQuantitaDisponibile());
        return p;
    }

    /**
     * Elimina un prodotto e i movimenti correlati.
     * @param codiceId il codice del prodotto da eliminare
     */
    public void eliminaProdotto(String codiceId) {
        prodottoDAO.elimina(codiceId);
    }



    // Metodo generaIdUnivoco rimosso in quanto l'ID deve essere inserito manualmente
}
