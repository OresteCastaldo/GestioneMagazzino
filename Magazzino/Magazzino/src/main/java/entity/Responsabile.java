package entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Entità che rappresenta il responsabile del magazzino, che gestisce i prodotti.
 */
@Entity
@DiscriminatorValue("RESPONSABILE")
public class Responsabile extends Utente {

    @ManyToMany
    @JoinTable(
        name = "gestione_prodotti",
        joinColumns = @JoinColumn(name = "responsabile_email"),
        inverseJoinColumns = @JoinColumn(name = "prodotto_id")
    )
    private List<Prodotto> prodottiGestiti;

    public Responsabile() {
        super();
        this.prodottiGestiti = new ArrayList<>();
    }

    public List<Prodotto> getProdottiGestiti() {
        return prodottiGestiti;
    }

    public void setProdottiGestiti(List<Prodotto> prodottiGestiti) {
        this.prodottiGestiti = prodottiGestiti;
    }
}
