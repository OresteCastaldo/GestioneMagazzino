package dto;

/**
 * Data Transfer Object per il trasporto dei dati di un Movimento
 * dal livello Boundary al livello Controller.
 * Non contiene alcuna dipendenza dal livello Entity o Database.
 */
public class MovimentoDTO {

    private String codiceProdotto;
    private int quantita;
    private String tipologia;

    public MovimentoDTO() {
    }

    public MovimentoDTO(String codiceProdotto, int quantita, String tipologia) {
        this.codiceProdotto = codiceProdotto;
        this.quantita = quantita;
        this.tipologia = tipologia;
    }

    // --- Getters e Setters ---

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
}
