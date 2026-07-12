package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Entità che rappresenta un operatore, ovvero l'utente che crea i movimenti di magazzino.
 * Tabella dedicata con join sulla chiave primaria di Utente.
 */
@Entity
@Table(name = "operatore")
@PrimaryKeyJoinColumn(name = "id_operatore")
public class Operatore extends Utente {

    @OneToMany(mappedBy = "operatore")
    private List<Movimento> movimentiCreati;

    public Operatore() {
        super();
        this.movimentiCreati = new ArrayList<>();
    }
}
