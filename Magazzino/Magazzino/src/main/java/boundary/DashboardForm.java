package boundary;

import controller.LoginController;

import javax.swing.*;
import java.awt.*;

/**
 * Form Boundary che funge da dashboard principale post-login.
 * Fornisce la navigazione verso le diverse funzionalità del sistema.
 * Comunica con il LoginController per ottenere i dati dell'utente corrente.
 */
public class DashboardForm extends JPanel {

    private LoginController loginCtrl;
    private MainFrame mainFrame;

    public DashboardForm(LoginController loginCtrl, MainFrame mainFrame) {
        this.loginCtrl = loginCtrl;
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                aggiornaPulsanti();
            }
        });
    }

    private void aggiornaPulsanti() {
        removeAll();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Titolo
        JLabel lblTitolo = new JLabel("Dashboard - Gestione Magazzino", SwingConstants.CENTER);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 0;
        add(lblTitolo, gbc);

        entity.Utente u = loginCtrl.getUtenteCorrente();
        boolean isOperatore = (u instanceof entity.Operatore);

        if (isOperatore) {
            JButton btnMovimento = new JButton("Registra Movimento");
            btnMovimento.addActionListener(e -> mainFrame.mostraSchermata("movimento"));
            gbc.gridy++; add(btnMovimento, gbc);

            JButton btnRicercaProdotto = new JButton("Ricerca Prodotto");
            btnRicercaProdotto.addActionListener(e -> mainFrame.mostraSchermata("prodotto"));
            gbc.gridy++; add(btnRicercaProdotto, gbc);

            JButton btnElencoProdotti = new JButton("Visualizza Elenco Prodotti");
            btnElencoProdotti.addActionListener(e -> JOptionPane.showMessageDialog(this, "Al momento non disponibile"));
            gbc.gridy++; add(btnElencoProdotti, gbc);

            JButton btnStoricoGen = new JButton("Visualizza Storico Movimenti Generali");
            btnStoricoGen.addActionListener(e -> JOptionPane.showMessageDialog(this, "Al momento non disponibile"));
            gbc.gridy++; add(btnStoricoGen, gbc);

        } else {
            JButton btnProdotti = new JButton("Gestione Prodotto");
            btnProdotti.addActionListener(e -> mainFrame.mostraSchermata("gestione_prodotto_menu"));
            gbc.gridy++; add(btnProdotti, gbc);

            JButton btnStorico = new JButton("Visualizza Storico Movimenti");
            btnStorico.addActionListener(e -> mainFrame.mostraSchermata("storico"));
            gbc.gridy++; add(btnStorico, gbc);
        }

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> mainFrame.mostraSchermata("login"));
        gbc.gridy++; add(btnLogout, gbc);

        JButton btnEsci = new JButton("Esci");
        btnEsci.addActionListener(e -> System.exit(0));
        gbc.gridy++; add(btnEsci, gbc);

        revalidate();
        repaint();
    }
}
