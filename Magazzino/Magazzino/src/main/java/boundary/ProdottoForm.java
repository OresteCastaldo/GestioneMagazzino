package boundary;

import controller.LoginController;
import controller.ProdottoController;
import entity.Prodotto;

import javax.swing.*;
import java.awt.*;

/**
 * Form Boundary per l'inserimento e la modifica di un prodotto.
 * Comunica con LoginController (sessione utente) e ProdottoController (logica prodotti).
 */
public class ProdottoForm extends JPanel {

    private LoginController loginCtrl;
    private ProdottoController prodCtrl;
    private MainFrame mainFrame;

    private JTextField txtRicerca;
    private JTextField txtCodiceId;
    private JTextField txtNome;
    private JTextField txtDescrizione;
    private JComboBox<String> cmbCategoria;
    private JTextField txtScaffale;
    private JTextField txtSogliaMin;
    private JTextField txtQuantita;

    private JLabel lblTitolo;
    private JPanel panelRicerca;
    private JLabel lblCodiceId;

    private JButton btnCerca;
    private JButton btnInserisci;
    private JButton btnModifica;
    private JButton btnSalvaModifiche;
    private JButton btnElimina;
    private JButton btnTornaDashboard;
    
    private String modalita = "";

    public ProdottoForm(LoginController loginCtrl, ProdottoController prodCtrl, MainFrame mainFrame) {
        this.loginCtrl = loginCtrl;
        this.prodCtrl = prodCtrl;
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titolo
        lblTitolo = new JLabel("Gestione Prodotto", SwingConstants.CENTER);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitolo, gbc);

        // Barra di Ricerca in alto
        panelRicerca = new JPanel(new FlowLayout());
        panelRicerca.add(new JLabel("Cerca (ID, Nome o Scaffale):"));
        txtRicerca = new JTextField(20);
        panelRicerca.add(txtRicerca);
        btnCerca = new JButton("Cerca");
        panelRicerca.add(btnCerca);

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(panelRicerca, gbc);

        gbc.gridwidth = 1;

        // Campi Anagrafici
        lblCodiceId = new JLabel("Codice ID:");
        gbc.gridx = 0; gbc.gridy = 2; add(lblCodiceId, gbc);
        txtCodiceId = new JTextField(20);
        gbc.gridx = 1; add(txtCodiceId, gbc);

        addCampo("Nome:", txtNome = new JTextField(20), gbc, 3);
        addCampo("Descrizione:", txtDescrizione = new JTextField(20), gbc, 4);
        
        JLabel lblCategoria = new JLabel("Categoria:");
        gbc.gridx = 0; gbc.gridy = 5; add(lblCategoria, gbc);
        cmbCategoria = new JComboBox<>(new String[]{"Seleziona...", "Frutta", "Verdura", "Carne", "Bevande", "Surgelati"});
        gbc.gridx = 1; add(cmbCategoria, gbc);

        addCampo("Scaffale:", txtScaffale = new JTextField(20), gbc, 6);
        addCampo("Soglia Minima:", txtSogliaMin = new JTextField(20), gbc, 7);
        addCampo("Quantità:", txtQuantita = new JTextField(20), gbc, 8);

        // Pannello pulsanti inferiori
        JPanel panelPulsanti = new JPanel(new FlowLayout());
        btnInserisci = new JButton("Inserisci");
        btnModifica = new JButton("Modifica");
        btnSalvaModifiche = new JButton("Salva Modifiche");
        btnElimina = new JButton("Elimina");
        btnTornaDashboard = new JButton("Torna alla Dashboard");

        panelPulsanti.add(btnInserisci);
        panelPulsanti.add(btnModifica);
        panelPulsanti.add(btnSalvaModifiche);
        panelPulsanti.add(btnElimina);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        add(panelPulsanti, gbc);

        gbc.gridy = 10;
        add(btnTornaDashboard, gbc);

        // Action Listeners
        btnCerca.addActionListener(e -> cercaProdotto());
        btnInserisci.addActionListener(e -> inserisciProdotto());
        btnModifica.addActionListener(e -> abilitaModifica());
        btnSalvaModifiche.addActionListener(e -> modificaProdotto());
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
        txtRicerca.setText("");
        aggiornaVisibilita();
    }

    private void aggiornaVisibilita() {
        entity.Utente u = loginCtrl.getUtenteCorrente();
        boolean isResponsabile = (u instanceof entity.Responsabile);

        txtRicerca.setEnabled(true);
        lblCodiceId.setVisible(true);
        txtCodiceId.setVisible(true);

        if (!isResponsabile) {
            lblTitolo.setText("Ricerca Prodotto");
            panelRicerca.setVisible(true);
            btnCerca.setVisible(true);
            btnInserisci.setVisible(false);
            btnModifica.setVisible(false);
            btnSalvaModifiche.setVisible(false);
            btnElimina.setVisible(false);
            bloccaTuttiICampi();
        } else {
            if ("INSERIMENTO".equals(modalita)) {
                lblTitolo.setText("Inserimento Nuovo Prodotto");
                panelRicerca.setVisible(false);
                btnCerca.setVisible(false);
                txtRicerca.setEnabled(false); // Disabilita ricerca in inserimento
                
                lblCodiceId.setVisible(false);
                txtCodiceId.setVisible(false);
                
                btnInserisci.setVisible(true);
                btnModifica.setVisible(false);
                btnSalvaModifiche.setVisible(false);
                btnElimina.setVisible(false);
                
                sbloccaTuttiICampi();
                txtCodiceId.setEditable(true);
            } else if ("RICERCA".equals(modalita)) {
                lblTitolo.setText("Ricerca Prodotto");
                panelRicerca.setVisible(true);
                btnCerca.setVisible(true);
                btnInserisci.setVisible(false);
                
                // Nascosti fino a quando non si cerca e si trova qualcosa
                btnModifica.setVisible(false);
                btnSalvaModifiche.setVisible(false);
                btnElimina.setVisible(false);
                
                bloccaTuttiICampi();
            } else {
                // Default fallback
                lblTitolo.setText("Gestione Prodotto");
                panelRicerca.setVisible(true);
                btnCerca.setVisible(true);
                btnInserisci.setVisible(true);
                btnModifica.setVisible(true);
                btnSalvaModifiche.setVisible(true);
                btnElimina.setVisible(true);
            }
        }
    }

    private void cercaProdotto() {
        String termine = txtRicerca.getText().trim();
        if (termine.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci un termine per cercare.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Uso la ricerca flessibile
        Prodotto p = prodCtrl.cercaProdottoFlessibile(termine);
        if (p != null) {
            txtCodiceId.setText(p.getCodiceId());
            txtNome.setText(p.getNome());
            txtDescrizione.setText(p.getDescrizione());
            
            String cat = p.getCategoria();
            if (cat != null) {
                cmbCategoria.setSelectedItem(cat);
            } else {
                cmbCategoria.setSelectedIndex(0);
            }
            
            txtScaffale.setText(p.getScaffale());
            txtSogliaMin.setText(String.valueOf(p.getSogliaMinDisponibile()));
            txtQuantita.setText(String.valueOf(p.getQuantitaDisponibile()));
            
            // Stato Iniziale di Sola Lettura
            bloccaTuttiICampi();
            
            // Mostro pulsanti di gestione per il Responsabile
            entity.Utente u = loginCtrl.getUtenteCorrente();
            if (u instanceof entity.Responsabile) {
                btnModifica.setVisible(true);
                btnSalvaModifiche.setVisible(false); // Nascosto finché non si clicca Modifica
                btnElimina.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nessun prodotto trovato.", "Errore", JOptionPane.ERROR_MESSAGE);
            pulisciCampi();
            btnModifica.setVisible(false);
            btnSalvaModifiche.setVisible(false);
            btnElimina.setVisible(false);
        }
    }
    
    private void abilitaModifica() {
        sbloccaTuttiICampi();
        txtCodiceId.setEditable(false); // Il Codice ID DEVE rimanere in sola lettura
        
        btnModifica.setVisible(false);
        btnSalvaModifiche.setVisible(true);
    }

    private void bloccaTuttiICampi() {
        txtCodiceId.setEditable(false);
        txtNome.setEditable(false);
        txtDescrizione.setEditable(false);
        cmbCategoria.setEnabled(false);
        txtScaffale.setEditable(false);
        txtSogliaMin.setEditable(false);
        txtQuantita.setEditable(false);
    }
    
    private void sbloccaTuttiICampi() {
        txtCodiceId.setEditable(true);
        txtNome.setEditable(true);
        txtDescrizione.setEditable(true);
        cmbCategoria.setEnabled(true);
        txtScaffale.setEditable(true);
        txtSogliaMin.setEditable(true);
        txtQuantita.setEditable(true);
    }

    private void addCampo(String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }

    private boolean isDatiValidi() {
        String codiceId = ("INSERIMENTO".equals(modalita)) ? null : txtCodiceId.getText().trim();
        String categoria = (cmbCategoria.getSelectedItem() != null) ? cmbCategoria.getSelectedItem().toString() : "";
        
        String errore = prodCtrl.validaDatiProdotto(
            codiceId, 
            txtNome.getText(), 
            txtDescrizione.getText(), 
            categoria, 
            txtScaffale.getText(), 
            txtSogliaMin.getText(), 
            txtQuantita.getText()
        );
        if (errore != null) {
            JOptionPane.showMessageDialog(this, errore, "Errore di Validazione", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void inserisciProdotto() {
        if (!isDatiValidi()) return;
        try {
            Prodotto p = creaProdottoDaForm();
            prodCtrl.inserisciProdotto(p);
            JOptionPane.showMessageDialog(this, "Prodotto inserito con successo!\nID Assegnato: " + p.getCodiceId(), "Successo", JOptionPane.INFORMATION_MESSAGE);
            pulisciCampi();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Soglia e Quantità devono essere numeri interi.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificaProdotto() {
        if (!isDatiValidi()) return;
        try {
            Prodotto p = creaProdottoDaForm();
            prodCtrl.salvaModifiche(p);
            JOptionPane.showMessageDialog(this, "Prodotto modificato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            
            // Ritorna in sola lettura dopo il salvataggio
            bloccaTuttiICampi();
            btnSalvaModifiche.setVisible(false);
            btnModifica.setVisible(true);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Soglia e Quantità devono essere numeri interi.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminaProdotto() {
        String codiceId = txtCodiceId.getText().trim();
        if (codiceId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun prodotto selezionato per l'eliminazione.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int conferma = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare il prodotto?", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);
        if (conferma == JOptionPane.YES_OPTION) {
            prodCtrl.eliminaProdotto(codiceId);
            JOptionPane.showMessageDialog(this, "Prodotto eliminato.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            pulisciCampi();
            btnModifica.setVisible(false);
            btnSalvaModifiche.setVisible(false);
            btnElimina.setVisible(false);
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
        
        if (cmbCategoria.getSelectedItem() != null) {
            p.setCategoria(cmbCategoria.getSelectedItem().toString());
        }
        
        p.setScaffale(txtScaffale.getText().trim());
        
        String soglia = txtSogliaMin.getText().trim();
        p.setSogliaMinDisponibile(soglia.isEmpty() ? 0 : Integer.parseInt(soglia));
        
        String qt = txtQuantita.getText().trim();
        p.setQuantitaDisponibile(qt.isEmpty() ? 0 : Integer.parseInt(qt));
        
        return p;
    }

    private void pulisciCampi() {
        txtCodiceId.setText("");
        txtNome.setText("");
        txtDescrizione.setText("");
        cmbCategoria.setSelectedIndex(0);
        txtScaffale.setText("");
        txtSogliaMin.setText("");
        txtQuantita.setText("");
    }
}
