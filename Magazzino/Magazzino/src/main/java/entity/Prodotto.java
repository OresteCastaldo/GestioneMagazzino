package entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entità che rappresenta un prodotto in magazzino.
 * Ha una relazione 1 a N con Movimento.
 */
@Entity
@Table(name = "prodotto")
public class Prodotto {

    @Id
    @Column(name = "id_prodotto")
    private String codiceId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "quantita_disponibile")
    private int quantitaDisponibile;

    @Column(name = "soglia_minima")
    private int sogliaMinDisponibile;

    @Column(name = "scaffale")
    private String scaffale;

    @Column(name = "sotto_scorta")
    private boolean sottoScorta;

    public Prodotto() {
    }

    //  Get e Set

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

    public int getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(int quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public int getSogliaMinDisponibile() {
        return sogliaMinDisponibile;
    }

    public void setSogliaMinDisponibile(int sogliaMinDisponibile) {
        this.sogliaMinDisponibile = sogliaMinDisponibile;
    }

    public String getScaffale() {
        return scaffale;
    }

    public void setScaffale(String scaffale) {
        this.scaffale = scaffale;
    }

    public boolean isSottoScorta() {
        return sottoScorta;
    }

    public void setSottoScorta(boolean sottoScorta) {
        this.sottoScorta = sottoScorta;
    }

}
