package dto;

/**
 * Data Transfer Object per il trasporto dei dati di sessione dell'utente
 * dal livello Controller al livello Boundary.
 * Nasconde completamente la gerarchia Entity (Utente, Operatore, Responsabile)
 * e delega la logica instanceof al Controller.
 */
public class UtenteDTO {

    private String nome;
    private String cognome;
    private String email;
    private String ruolo;

    public UtenteDTO() {
    }

    public UtenteDTO(String nome, String cognome, String email, String ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.ruolo = ruolo;
    }

    // --- Helper per il controllo del ruolo ---

    /**
     * Verifica se l'utente ha il ruolo di Responsabile.
     * @return true se il ruolo è RESPONSABILE
     */
    public boolean isResponsabile() {
        return "RESPONSABILE".equals(ruolo);
    }

    // --- Getters e Setters ---

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
