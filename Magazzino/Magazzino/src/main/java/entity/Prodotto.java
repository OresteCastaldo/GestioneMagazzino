package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


/**
 * Entità che rappresenta un prodotto in magazzino.
 */
@Entity
public class Prodotto {

    @Id
    private String codiceId;
    
    private String nome;
    private String descrizione;
    private String categoria;
    private int sogliaMinDisponibile;
    private int quantitaDisponibile;
    private String scaffale;

    public Prodotto() {
    }

    // Metodo descritto nel PlantUML
    public int getQuantitaProdotto() {
        return quantitaDisponibile;
    }

    // --- Getters and Setters standard ---

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

    public String getScaffale() {
        return scaffale;
    }

    public void setScaffale(String scaffale) {
        this.scaffale = scaffale;
    }

}
