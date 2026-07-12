package dto;

/**
 * Data Transfer Object per il trasporto dei dati di un Movimento
 * dal livello Boundary al livello Controller e viceversa.
 * Non contiene alcuna dipendenza dal livello Entity o Database.
 */

public class MovimentoDTO {

    private Long id;
    private String codiceProdotto;
    private int quantita;
    private String tipologia;
    private java.util.Date data;

    public MovimentoDTO() {
    }

    /**
     * Costruttore compatto per la creazione di un movimento da parte della GUI
     * (id e data non servono in input, vengono assegnati dal controller).
     */
    public MovimentoDTO(String codiceProdotto, int quantita, String tipologia) {
        this.codiceProdotto = codiceProdotto;
        this.quantita = quantita;
        this.tipologia = tipologia;
    }

    /**
     * Costruttore completo per la restituzione dei dati dalla persistenza alla GUI.
     */
    public MovimentoDTO(Long id, String codiceProdotto, int quantita, String tipologia, java.util.Date data) {
        this.id = id;
        this.codiceProdotto = codiceProdotto;
        this.quantita = quantita;
        this.tipologia = tipologia;
        this.data = data;
    }

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public void setCodiceProdotto(String codiceProdotto) {
        this.codiceProdotto = codiceProdotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public java.util.Date getData() {
        return data;
    }

    public void setData(java.util.Date data) {
        this.data = data;
    }
}
