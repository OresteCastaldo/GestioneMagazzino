package controller;

import database.MovimentoDAO;
import database.ProdottoDAO;
import database.JpaUtil;
import dto.MovimentoDTO;
import entity.Movimento;
import entity.Prodotto;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
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
    public boolean registraMovimento(Movimento movimento) throws IllegalArgumentException {
        String codice = movimento.getProdottoId();
        
        if (codice == null || codice.length() != 6) {
            throw new IllegalArgumentException("Formato codice non corretto: deve essere di 6 caratteri");
        }
        
        if (!codice.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Il codice prodotto può contenere solo caratteri alfanumerici");
        }
        
        if (movimento.getQuantita() <= 0) {
            throw new IllegalArgumentException("La quantità del movimento deve essere maggiore di zero");
        }

        boolean sottoScortaTriggered = false;
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
                    if (movimento.getQuantita() > p.getQuantitaDisponibile()) {
                        throw new IllegalArgumentException("Quantità insufficiente in magazzino!");
                    }
                    nuovaQta = p.getQuantitaDisponibile() - movimento.getQuantita();
                }
                p.setQuantitaDisponibile(nuovaQta);
                p.setSottoScorta(p.getQuantitaDisponibile() < p.getSogliaMinDisponibile());
                prodottoDAO.aggiorna(p);
                
                // Segnala se il prodotto si trova sotto scorta dopo il movimento (qualsiasi tipo)
                if (p.isSottoScorta()) {
                    sottoScortaTriggered = true;
                }
            }
        }
        movimentoDAO.salva(movimento);
        return sottoScortaTriggered;
    }

    /**
     * Registra un movimento a partire da un DTO proveniente dal livello Boundary.
     * Converte il DTO in un'Entity, risolve il Prodotto associato e delega
     * la registrazione al metodo standard registraMovimento.
     * @param dto il MovimentoDTO con i dati provenienti dalla grafica
     * @return true se il prodotto risulta sotto scorta dopo il movimento
     * @throws IllegalArgumentException se il prodotto non viene trovato o i dati non sono validi
     */
    public boolean registraMovimentoDaDTO(MovimentoDTO dto) throws IllegalArgumentException {
        // a. Verifica l'esistenza del prodotto tramite il codice nel DTO
        Prodotto prodotto = prodottoDAO.trovaPerId(dto.getCodiceProdotto());
        if (prodotto == null) {
            throw new IllegalArgumentException("Prodotto non trovato con codice: " + dto.getCodiceProdotto());
        }

        // b. Crea una nuova istanza dell'Entity Movimento
        Movimento movimento = new Movimento();

        // c. Popola l'Entity con i dati del DTO
        movimento.setQuantita(dto.getQuantita());
        movimento.setTipologia(dto.getTipologia());
        movimento.setData(new Date());
        movimento.setProdottoId(prodotto.getCodiceId());

        // d. Delega al metodo standard di registrazione e restituisce il risultato
        return registraMovimento(movimento);
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

    /**
     * Ricerca flessibile per la GUI: restituisce List<MovimentoDTO> invece di List<Movimento>.
     * La conversione Entity→DTO avviene interamente qui, nel Controller.
     * @param termine il termine di ricerca (ID, nome o scaffale)
     * @return la lista di MovimentoDTO corrispondenti
     */
    public List<MovimentoDTO> getMovimentiFiltratiFlessibiliDTO(String termine) {
        List<Movimento> entita = getMovimentiFiltratiFlessibili(termine);
        return convertiInDTO(entita);
    }

    /**
     * Restituisce i movimenti per un prodotto con filtri opzionali.
     * Gestisce la conversione sicura delle date da stringa (formato gg/MM/yyyy).
     * @param codiceProdotto ID del prodotto (obbligatorio)
     * @param dataInizioStr data inizio come stringa gg/MM/yyyy (opzionale)
     * @param dataFineStr data fine come stringa gg/MM/yyyy (opzionale)
     * @param tipoMovimento tipo CARICO/SCARICO (opzionale, null/"Tutti" = tutti)
     * @return la lista dei movimenti che soddisfano i criteri
     * @throws IllegalArgumentException se una data inserita ha un formato non valido
     */
    public List<Movimento> getStoricoConFiltri(String codiceProdotto, String dataInizioStr,
            String dataFineStr, String tipoMovimento) throws IllegalArgumentException {

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        java.util.Date dataInizio = null;
        java.util.Date dataFine = null;

        if (dataInizioStr != null && !dataInizioStr.trim().isEmpty()) {
            try {
                dataInizio = sdf.parse(dataInizioStr.trim());
                // Forza l'orario alle 00:00:00 per includere l'intero giorno di inizio
                java.util.Calendar calInizio = java.util.Calendar.getInstance();
                calInizio.setTime(dataInizio);
                calInizio.set(java.util.Calendar.HOUR_OF_DAY, 0);
                calInizio.set(java.util.Calendar.MINUTE, 0);
                calInizio.set(java.util.Calendar.SECOND, 0);
                calInizio.set(java.util.Calendar.MILLISECOND, 0);
                dataInizio = calInizio.getTime();
            } catch (java.text.ParseException e) {
                throw new IllegalArgumentException("Formato Data Inizio non valido. Usa il formato gg/MM/aaaa.");
            }
        }

        if (dataFineStr != null && !dataFineStr.trim().isEmpty()) {
            try {
                dataFine = sdf.parse(dataFineStr.trim());
                // Imposta la data fine alla fine della giornata (23:59:59)
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(dataFine);
                cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
                cal.set(java.util.Calendar.MINUTE, 59);
                cal.set(java.util.Calendar.SECOND, 59);
                dataFine = cal.getTime();
            } catch (java.text.ParseException e) {
                throw new IllegalArgumentException("Formato Data Fine non valido. Usa il formato gg/MM/aaaa.");
            }
        }

        // Se il tipo è "Tutti" o vuoto, passa null al DAO
        String tipo = null;
        if (tipoMovimento != null && !tipoMovimento.trim().isEmpty() && !tipoMovimento.equals("Tutti")) {
            tipo = tipoMovimento.trim();
        }

        return movimentoDAO.ricercaStoricoConFiltri(codiceProdotto, dataInizio, dataFine, tipo);
    }

    /**
     * Versione DTO di getStoricoConFiltri per il livello Boundary.
     * La conversione Entity→DTO avviene interamente qui, nel Controller.
     * @param codiceProdotto ID del prodotto (obbligatorio)
     * @param dataInizioStr data inizio gg/MM/yyyy (opzionale)
     * @param dataFineStr data fine gg/MM/yyyy (opzionale)
     * @param tipoMovimento CARICO/SCARICO/Tutti (opzionale)
     * @return la lista di MovimentoDTO che soddisfano i criteri
     * @throws IllegalArgumentException se una data ha formato non valido
     */
    public List<MovimentoDTO> getStoricoConFiltriDTO(String codiceProdotto, String dataInizioStr,
            String dataFineStr, String tipoMovimento) throws IllegalArgumentException {
        List<Movimento> entita = getStoricoConFiltri(codiceProdotto, dataInizioStr, dataFineStr, tipoMovimento);
        return convertiInDTO(entita);
    }

    /**
     * Metodo interno per convertire una lista di Entity Movimento in una lista di MovimentoDTO.
     * @param movimenti la lista di entity da convertire
     * @return la lista di DTO corrispondenti
     */
    private List<MovimentoDTO> convertiInDTO(List<Movimento> movimenti) {
        List<MovimentoDTO> dtoList = new ArrayList<>();
        if (movimenti != null) {
            for (Movimento m : movimenti) {
                MovimentoDTO dto = new MovimentoDTO(
                    m.getId(),
                    m.getProdottoId() != null ? m.getProdottoId() : "N/A",
                    m.getQuantita(),
                    m.getTipologia(),
                    m.getData()
                );
                dtoList.add(dto);
            }
        }
        return dtoList;
    }
}
