package dto;

/**
 * Data Transfer Object per il trasporto dei dati anagrafici di un Responsabile
 * dal livello Controller al livello Boundary.
 * Non contiene alcuna dipendenza dal livello Entity o Database.
 */
public class ResponsabileDTO {

    private Long idUtente;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String ruolo;

    public ResponsabileDTO() {
    }

    public ResponsabileDTO(Long idUtente, String nome, String cognome, String email, String password, String ruolo) {
        this.idUtente = idUtente;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
