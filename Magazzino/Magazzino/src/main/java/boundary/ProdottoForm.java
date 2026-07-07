package boundary;

import controller.GestoreMagazzino;
import entity.Prodotto;

import javax.swing.*;
import java.awt.*;

/**
 * Form Boundary per l'inserimento e la modifica di un prodotto.
 * Comunica esclusivamente con il GestoreMagazzino (Facade).
 */
public class ProdottoForm extends JPanel {

    private GestoreMagazzino gestore;
    private MainFrame mainFrame;

    private JTextField txtCodiceId;
    private JTextField txtNome;
    private JTextField txtDescrizione;
    private JTextField txtCategoria;
    private JTextField txtScaffale;
    private JTextField txtSogliaMin;
    private JTextField txtQuantita;
    private JButton btnCerca;
    private JButton btnInserisci;
    private JButton btnModifica;
    private JButton btnElimina;
    private JButton btnTornaDashboard;
    
    private String modalita = "";

    public ProdottoForm(GestoreMagazzino gestore, MainFrame mainFrame) {
        this.gestore = gestore;
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titolo
        JLabel lblTitolo = new JLabel("Gestione Prodotto", SwingConstants.CENTER);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitolo, gbc);

        gbc.gridwidth = 1;

        // Codice ID
        addCampo("Codice ID:", txtCodiceId = new JTextField(20), gbc, 1);
        // Nome
        addCampo("Nome:", txtNome = new JTextField(20), gbc, 2);
        // Descrizione
        addCampo("Descrizione:", txtDescrizione = new JTextField(20), gbc, 3);
        // Categoria
        addCampo("Categoria:", txtCategoria = new JTextField(20), gbc, 4);
        // Scaffale
        addCampo("Scaffale:", txtScaffale = new JTextField(20), gbc, 5);
        // Soglia Minima
        addCampo("Soglia Minima:", txtSogliaMin = new JTextField(20), gbc, 6);
        // Quantità
        addCampo("Quantità:", txtQuantita = new JTextField(20), gbc, 7);

        // Pannello pulsanti
        JPanel panelPulsanti = new JPanel(new FlowLayout());
        btnCerca = new JButton("Cerca");
        btnInserisci = new JButton("Inserisci");
        btnModifica = new JButton("Salva Modifiche");
        btnElimina = new JButton("Elimina");
        btnTornaDashboard = new JButton("Torna alla Dashboard");

        panelPulsanti.add(btnCerca);
        panelPulsanti.add(btnInserisci);
        panelPulsanti.add(btnModifica);
        panelPulsanti.add(btnElimina);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        add(panelPulsanti, gbc);

        gbc.gridy = 9;
        add(btnTornaDashboard, gbc);

        // Action Listeners
        btnCerca.addActionListener(e -> cercaProdotto());
        btnInserisci.addActionListener(e -> inserisciProdotto());
        btnModifica.addActionListener(e -> modificaProdotto());
        btnElimina.addActionListener(e -> eliminaProdotto());
        btnTornaDashboard.addActionListener(e -> mainFrame.mostraSchermata("dashboard"));

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                aggiornaVisibilita();
            }
        });
    }

    public void setModalita(String modalita) {
        this.modalita = modalita;
        pulisciCampi();
        aggiornaVisibilita();
    }

    private void aggiornaVisibilita() {
        entity.Utente u = gestore.getUtenteCorrente();
        boolean isResponsabile = (u instanceof entity.Responsabile);

        if (!isResponsabile) {
            btnCerca.setVisible(true);
            btnInserisci.setVisible(false);
            btnModifica.setVisible(false);
            btnElimina.setVisible(false);

            // L'operatore può solo visualizzare (ma può inserire ID per cercare)
            txtCodiceId.setEditable(true);
            txtNome.setEditable(false);
            txtDescrizione.setEditable(false);
            txtCategoria.setEditable(false);
            txtScaffale.setEditable(false);
            txtSogliaMin.setEditable(false);
            txtQuantita.setEditable(false);
        } else {
            txtCodiceId.setEditable(true);
            txtNome.setEditable(true);
            txtDescrizione.setEditable(true);
            txtCategoria.setEditable(true);
            txtScaffale.setEditable(true);
            txtSogliaMin.setEditable(true);
            txtQuantita.setEditable(true);

            if ("INSERIMENTO".equals(modalita)) {
                btnCerca.setVisible(false);
                btnInserisci.setVisible(true);
                btnModifica.setVisible(false);
                btnElimina.setVisible(false);
            } else if ("RICERCA".equals(modalita)) {
                btnCerca.setVisible(true);
                btnInserisci.setVisible(false);
                btnModifica.setVisible(true);
                btnElimina.setVisible(true);
            } else {
                btnCerca.setVisible(true);
                btnInserisci.setVisible(true);
                btnModifica.setVisible(true);
                btnElimina.setVisible(true);
            }
        }
    }

    private void cercaProdotto() {
        String codiceId = txtCodiceId.getText().trim();
        if (codiceId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci il Codice ID per cercare.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Prodotto p = gestore.CercaProdotto(codiceId);
        if (p != null) {
            txtNome.setText(p.getNome());
            txtDescrizione.setText(p.getDescrizione());
            txtCategoria.setText(p.getCategoria());
            txtScaffale.setText(p.getScaffale());
            txtSogliaMin.setText(String.valueOf(p.getSogliaMinDisponibile()));
            txtQuantita.setText(String.valueOf(p.getQuantitaDisponibile()));
        } else {
            JOptionPane.showMessageDialog(this, "Prodotto non trovato.", "Errore", JOptionPane.ERROR_MESSAGE);
            pulisciCampi();
        }
    }

    private void addCampo(String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }

    private void inserisciProdotto() {
        try {
            Prodotto p = creaProdottoDaForm();
            gestore.InserisciProdotto(p);
            JOptionPane.showMessageDialog(this, "Prodotto inserito con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            pulisciCampi();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Soglia e Quantità devono essere numeri interi.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificaProdotto() {
        try {
            Prodotto p = creaProdottoDaForm();
            gestore.salvaModifiche(p);
            JOptionPane.showMessageDialog(this, "Prodotto modificato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Soglia e Quantità devono essere numeri interi.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminaProdotto() {
        String codiceId = txtCodiceId.getText().trim();
        if (codiceId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci il Codice ID del prodotto da eliminare.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int conferma = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare il prodotto?", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);
        if (conferma == JOptionPane.YES_OPTION) {
            gestore.EliminaProdotto(codiceId);
            JOptionPane.showMessageDialog(this, "Prodotto eliminato.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            pulisciCampi();
        }
    }

    private Prodotto creaProdottoDaForm() {
        Prodotto p = new Prodotto();
        String codiceId = txtCodiceId.getText().trim();
        if (!codiceId.isEmpty()) {
            p.setCodiceId(codiceId);
        }
        p.setNome(txtNome.getText().trim());
        p.setDescrizione(txtDescrizione.getText().trim());
        p.setCategoria(txtCategoria.getText().trim());
        p.setScaffale(txtScaffale.getText().trim());
        p.setSogliaMinDisponibile(Integer.parseInt(txtSogliaMin.getText().trim()));
        p.setQuantitaDisponibile(Integer.parseInt(txtQuantita.getText().trim()));
        return p;
    }

    private void pulisciCampi() {
        txtCodiceId.setText("");
        txtNome.setText("");
        txtDescrizione.setText("");
        txtCategoria.setText("");
        txtScaffale.setText("");
        txtSogliaMin.setText("");
        txtQuantita.setText("");
    }
}
