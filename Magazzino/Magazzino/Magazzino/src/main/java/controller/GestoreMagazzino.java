package controller;

import entity.Utente;
import entity.Prodotto;
import entity.Movimento;
import java.util.List;

/**
 * Gestore principale (Facade) che coordina le operazioni del sistema.
 */
public class GestoreMagazzino {

    private CatalogoUtenti catUtenti;
    private CatalogoMovimento catMovimento;
    private CatalogoProdotto catProdotto;
    private Utente UtenteCorrente;
    private SistemaNotifiche sistemaNotifiche;

    public GestoreMagazzino() {
        this.catUtenti = new CatalogoUtenti();
        this.catMovimento = new CatalogoMovimento();
        this.catProdotto = new CatalogoProdotto();
        this.sistemaNotifiche = new SistemaNotifiche();
    }

    public void VisualizzaStoricoMovimento(String prodottoId) {
        List<Movimento> filtrati = ApplicaFiltro(prodottoId);
        for (Movimento m : filtrati) {
            mostraSingoloMovimento(m);
        }
    }

    public List<Movimento> ApplicaFiltro(String prodottoId) {
        return catMovimento.RichiediMovimentiFiltrati(prodottoId);
    }

    public List<Movimento> ApplicaFiltroFlessibile(String termine) {
        List<Prodotto> prodotti = catProdotto.RicercaMultiplaFlessibile(termine);
        java.util.List<Movimento> risultati = new java.util.ArrayList<>();
        if (prodotti != null) {
            for (Prodotto p : prodotti) {
                List<Movimento> movs = catMovimento.RichiediMovimentiFiltrati(p.getCodiceId());
                if (movs != null) {
                    risultati.addAll(movs);
                }
            }
        }
        return risultati;
    }

    private void mostraSingoloMovimento(Movimento m) {
        System.out.println("Movimento: " + m.getId());
    }

    public void RegistraMovimento(Movimento movimento) {
        if (movimento.getTipologia() != null && movimento.getTipologia().equalsIgnoreCase("CARICO")) {
            calcolaIncremento(movimento);
        } else {
            calcolaDecremento(movimento);
        }
        catMovimento.SalvaMovimento(movimento);
    }

    private void calcolaIncremento(Movimento m) {
        String prodottoId = m.getProdottoId();
        if (prodottoId != null) {
            Prodotto p = CercaProdotto(prodottoId);
            if (p != null) {
                int nuovaQta = p.getQuantitaDisponibile() + m.getQuantita();
                catProdotto.AggiornaQuantita(p.getCodiceId(), nuovaQta);
            }
        }
    }

    private void calcolaDecremento(Movimento m) {
        String prodottoId = m.getProdottoId();
        if (prodottoId != null) {
            Prodotto p = CercaProdotto(prodottoId);
            if (p != null) {
                int nuovaQta = p.getQuantitaDisponibile() - m.getQuantita();
                catProdotto.AggiornaQuantita(p.getCodiceId(), nuovaQta);
            }
        }
    }

    public void InserisciProdotto(Prodotto prodotto) {
        catProdotto.InserisciInCatalogo(prodotto);
    }

    public String validaDatiProdotto(String codiceId, String nome, String descrizione, String categoria, String scaffale, String sogliaMinTxt, String quantitaTxt) {
        return catProdotto.validaDatiProdotto(codiceId, nome, descrizione, categoria, scaffale, sogliaMinTxt, quantitaTxt);
    }

    public Prodotto CercaProdotto(String codiceId) {
        if (catProdotto.VerificaEsistenzaProdotto(codiceId)) {
            return catProdotto.TrovaProdotto(codiceId);
        }
        return null;
    }

    public Prodotto CercaProdottoFlessibile(String termine) {
        return catProdotto.RicercaFlessibile(termine);
    }

    public void ChiudiFinestra() {
        System.exit(0);
    }

    public void salvaModifiche(Prodotto prodotto) {
        catProdotto.ValidaEModificaProdotto(prodotto);
    }

    public void EliminaProdotto(String codiceId) {
        catProdotto.EliminaProdotto(codiceId);
    }


    private void setCurrenteUtente(Utente utente) {
        this.UtenteCorrente = utente;
    }

    public Utente getUtenteCorrente() {
        return UtenteCorrente;
    }

    public void Registrazione(Utente utente) {
        if (ValidaDatiUtente(utente) && !catUtenti.CheckMail(utente.getEmail())) {
            catUtenti.AggiungiUtente(utente);
        }
    }

    public boolean Autenticazione(String email) {
        Utente u = catUtenti.TrovaUtente(email);
        if (u != null) {
            setCurrenteUtente(u);
            return true;
        }
        return false;
    }

    private void MostraSingoloProdotto(Prodotto p) {
    }

    private void MostraModuloPrecompilato(String codiceId) {
    }

    private boolean ValidaDatiUtente(Utente utente) {
        return utente != null && utente.getEmail() != null;
    }
}
