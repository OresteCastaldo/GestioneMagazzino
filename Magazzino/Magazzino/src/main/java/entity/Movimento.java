package entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Entità che rappresenta un movimento di carico o scarico di un prodotto.
 * Relazione N a 1 verso Operatore e N a 1 verso Prodotto.
 */
@Entity
@Table(name = "movimento")
public class Movimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimento")
    private Long id;

    @Column(name = "quantita")
    private int quantita;

    @Column(name = "data")
    private Date data;

    @Column(name = "tipologia")
    private String tipologia;

    @ManyToOne
    @JoinColumn(name = "id_prodotto")
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "id_operatore")
    private Operatore operatore;

    // Campo Transient per mantenere la compatibilità con le Boundary
    // che settano l'ID prodotto come stringa prima che il Controller risolva l'entità.
    @Transient
    private String prodottoId;

    public Movimento() {
    }

    //  Get e Set

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public Operatore getOperatore() {
        return operatore;
    }

    public void setOperatore(Operatore operatore) {
        this.operatore = operatore;
    }

    // --- Metodi di retrocompatibilità per il layer Boundary ---

    public String getProdottoId() {
        if (this.prodotto != null) {
            return this.prodotto.getCodiceId();
        }
        return prodottoId;
    }

    public void setProdottoId(String prodottoId) {
        this.prodottoId = prodottoId;
    }
}
