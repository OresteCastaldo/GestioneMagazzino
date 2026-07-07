package boundary;

import controller.GestoreMagazzino;

import javax.swing.*;
import java.awt.*;

/**
 * Interfaccia principale (Boundary) per il sistema di Gestione Magazzino.
 * Utilizza un CardLayout per navigare tra i diversi form.
 * Comunica esclusivamente con il GestoreMagazzino (Facade).
 */
public class MainFrame extends JFrame {

    private GestoreMagazzino gestore;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel lblIntestazione;

    public MainFrame() {
        this.gestore = new GestoreMagazzino();

        setTitle("Sistema Gestione Magazzino");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Creazione di tutti i form (Boundary)
        LoginForm loginForm = new LoginForm(gestore, this);
        RegistrazioneForm registrazioneForm = new RegistrazioneForm(gestore, this);
        DashboardForm dashboardForm = new DashboardForm(gestore, this);
        ProdottoForm prodottoForm = new ProdottoForm(gestore, this);
        GestioneProdottoMenu gestioneProdottoMenu = new GestioneProdottoMenu(gestore, this);
        MovimentoForm movimentoForm = new MovimentoForm(gestore, this);
        StoricoMovimentiForm storicoForm = new StoricoMovimentiForm(gestore, this);

        setLayout(new BorderLayout());

        lblIntestazione = new JLabel("Utente loggato: Nessuno", SwingConstants.CENTER);
        lblIntestazione.setFont(new Font("Arial", Font.BOLD, 14));
        lblIntestazione.setOpaque(true);
        lblIntestazione.setBackground(Color.LIGHT_GRAY);
        lblIntestazione.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(lblIntestazione, BorderLayout.NORTH);

        // Aggiunta dei form al CardLayout
        mainPanel.add(loginForm, "login");
        mainPanel.add(registrazioneForm, "registrazione");
        mainPanel.add(dashboardForm, "dashboard");
        mainPanel.add(gestioneProdottoMenu, "gestione_prodotto_menu");
        mainPanel.add(prodottoForm, "prodotto");
        mainPanel.add(movimentoForm, "movimento");
        mainPanel.add(storicoForm, "storico");

        add(mainPanel, BorderLayout.CENTER);

        // Mostra la schermata di login all'avvio
        lblIntestazione.setVisible(false);
        cardLayout.show(mainPanel, "login");
    }

    /**
     * Aggiorna l'intestazione con i dati dell'utente loggato.
     */
    public void aggiornaIntestazione() {
        entity.Utente u = gestore.getUtenteCorrente();
        if (u != null) {
            String ruolo = (u instanceof entity.Operatore) ? "OPERATORE" : "RESPONSABILE";
            lblIntestazione.setText("Utente loggato: " + ruolo + " - " + u.getEmail());
            lblIntestazione.setVisible(true);
        } else {
            lblIntestazione.setVisible(false);
        }
    }

    public ProdottoForm getProdottoForm() {
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof ProdottoForm) {
                return (ProdottoForm) comp;
            }
        }
        return null;
    }

    public void mostraSchermataProdotto(String modalita) {
        ProdottoForm pf = getProdottoForm();
        if (pf != null) {
            pf.setModalita(modalita);
        }
        mostraSchermata("prodotto");
    }

    /**
     * Naviga verso la schermata specificata.
     * @param nomeSchermata il nome della card da mostrare
     */
    public void mostraSchermata(String nomeSchermata) {
        if (!nomeSchermata.equals("login") && !nomeSchermata.equals("registrazione")) {
            aggiornaIntestazione();
        } else {
            lblIntestazione.setVisible(false);
        }
        cardLayout.show(mainPanel, nomeSchermata);
    }
}
