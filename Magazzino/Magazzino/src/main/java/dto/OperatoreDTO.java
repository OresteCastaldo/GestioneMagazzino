package dto;

/**
 * Data Transfer Object per il trasporto dei dati anagrafici di un Operatore
 * dal livello Controller al livello Boundary.
 * Non contiene alcuna dipendenza dal livello Entity o Database.
 */
public class OperatoreDTO {

    private Long idUtente;
    private String nome;
    private String cognome;
    private String email;
    private String ruolo;

    public OperatoreDTO() {
    }

    public OperatoreDTO(Long idUtente, String nome, String cognome, String email, String ruolo) {
        this.idUtente = idUtente;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.ruolo = ruolo;
    }

    // --- Getters e Setters ---

    public Long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
