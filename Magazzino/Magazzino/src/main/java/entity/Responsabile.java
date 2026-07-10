package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/**
 * Entità che rappresenta il responsabile del magazzino.
 * Tabella dedicata con join sulla chiave primaria di Utente.
 */
@Entity
@Table(name = "responsabile")
@PrimaryKeyJoinColumn(name = "id_responsabile")
public class Responsabile extends Utente {

    public Responsabile() {
        super();
    }
}
