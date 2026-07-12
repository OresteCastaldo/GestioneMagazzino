package dto;

/**
 * Data Transfer Object per il trasporto dei dati di un Prodotto
 * tra il livello Controller e il livello Boundary.
 * Non contiene alcuna dipendenza dal livello Entity o Database.
 */
public class ProdottoDTO {

    private String codiceId;
    private String nome;
    private String descrizione;
    private String categoria;
    private String scaffale;
    private int sogliaMinDisponibile;
    private int quantitaDisponibile;
    private boolean sottoScorta;

    public ProdottoDTO() {
    }

    public ProdottoDTO(String codiceId, String nome, String descrizione, String categoria,
                       String scaffale, int sogliaMinDisponibile, int quantitaDisponibile, boolean sottoScorta) {
        this.codiceId = codiceId;
        this.nome = nome;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.scaffale = scaffale;
        this.sogliaMinDisponibile = sogliaMinDisponibile;
        this.quantitaDisponibile = quantitaDisponibile;
        this.sottoScorta = sottoScorta;
    }

    // --- Getters e Setters ---

    public String getCodiceId() {
        return codiceId;
    }

    public void setCodiceId(String codiceId) {
        this.codiceId = codiceId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getScaffale() {
        return scaffale;
    }

    public void setScaffale(String scaffale) {
        this.scaffale = scaffale;
    }

    public int getSogliaMinDisponibile() {
        return sogliaMinDisponibile;
    }

    public void setSogliaMinDisponibile(int sogliaMinDisponibile) {
        this.sogliaMinDisponibile = sogliaMinDisponibile;
    }

    public int getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(int quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public boolean isSottoScorta() {
        return sottoScorta;
    }

    public void setSottoScorta(boolean sottoScorta) {
        this.sottoScorta = sottoScorta;
    }
}
