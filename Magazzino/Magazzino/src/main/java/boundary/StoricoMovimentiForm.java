package boundary;

import controller.MovimentoController;
import entity.Movimento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Form Boundary per la visualizzazione dello storico dei movimenti.
 * Comunica esclusivamente con il MovimentoController.
 * Supporta filtri opzionali per Data Inizio, Data Fine e Tipo Movimento.
 */
public class StoricoMovimentiForm extends JPanel {

    private MovimentoController movCtrl;
    private MainFrame mainFrame;

    private JTextField txtFiltroProdotto;
    private JTextField txtDataInizio;
    private JTextField txtDataFine;
    private JComboBox<String> cmbTipoMovimento;
    private JButton btnFiltra;
    private JButton btnApplicaFiltri;
    private JButton btnResettaFiltri;
    private JButton btnTornaDashboard;
    private JTable tabellaMovimenti;
    private DefaultTableModel tableModel;

    // Memorizza il codice prodotto corrente per applicare i filtri avanzati
    private String codiceProdottoCorrente;

    public StoricoMovimentiForm(MovimentoController movCtrl, MainFrame mainFrame) {
        this.movCtrl = movCtrl;
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Titolo
        JLabel lblTitolo = new JLabel("Storico Movimenti", SwingConstants.CENTER);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitolo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Ristrutturazione del Pannello Ricerca Principale
        JPanel panelRicerca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblFiltro = new JLabel("Cerca prodotto per ID, Nome o Scaffale:");
        txtFiltroProdotto = new JTextField(15);
        btnFiltra = new JButton("Cerca");
        panelRicerca.add(lblFiltro);
        panelRicerca.add(txtFiltroProdotto);
        panelRicerca.add(btnFiltra);

        // 3. Ristrutturazione del Pannello "Filtri Opzionali"
        JPanel panelFiltriAvanzati = new JPanel(new GridBagLayout());
        panelFiltriAvanzati.setBorder(BorderFactory.createTitledBorder("Filtri Opzionali"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Riga 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelFiltriAvanzati.add(new JLabel("Data Inizio (gg/MM/aaaa):"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDataInizio = new JTextField(10);
        panelFiltriAvanzati.add(txtDataInizio, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelFiltriAvanzati.add(new JLabel("Data Fine (gg/MM/aaaa):"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtDataFine = new JTextField(10);
        panelFiltriAvanzati.add(txtDataFine, gbc);

        // Riga 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFiltriAvanzati.add(new JLabel("Tipo:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbTipoMovimento = new JComboBox<>(new String[]{"Tutti", "CARICO", "SCARICO"});
        panelFiltriAvanzati.add(cmbTipoMovimento, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        btnApplicaFiltri = new JButton("Applica Filtri");
        panelFiltriAvanzati.add(btnApplicaFiltri, gbc);
        
        gbc.gridx = 3; gbc.weightx = 0;
        btnResettaFiltri = new JButton("Resetta Filtri");
        panelFiltriAvanzati.add(btnResettaFiltri, gbc);

        // Disabilita i filtri avanzati finché non si cerca un prodotto
        abilitaFiltriAvanzati(false);

        // 1. Ristrutturazione del Layout Superiore (panelTop)
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.add(lblTitolo);
        panelTop.add(Box.createVerticalStrut(10)); // Spazio extra
        panelTop.add(panelRicerca);
        panelTop.add(panelFiltriAvanzati);
        add(panelTop, BorderLayout.NORTH);

        // 4. Mantenimento del resto della UI
        // Tabella
        String[] colonne = {"ID", "Prodotto", "Quantità", "Tipologia", "Data"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabellaMovimenti = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabellaMovimenti);
        add(scrollPane, BorderLayout.CENTER);

        // Pulsante torna
        btnTornaDashboard = new JButton("Torna alla Dashboard");
        JPanel panelBottom = new JPanel(new FlowLayout());
        panelBottom.add(btnTornaDashboard);
        add(panelBottom, BorderLayout.SOUTH);

        // Action Listeners
        btnFiltra.addActionListener(e -> filtraMovimenti());
        btnApplicaFiltri.addActionListener(e -> applicaFiltriAvanzati());
        btnResettaFiltri.addActionListener(e -> resettaFiltri());
        btnTornaDashboard.addActionListener(e -> mainFrame.mostraSchermata("dashboard"));
    }

    /**
     * Abilita o disabilita i componenti dei filtri avanzati.
     */
    private void abilitaFiltriAvanzati(boolean abilitato) {
        txtDataInizio.setEnabled(abilitato);
        txtDataFine.setEnabled(abilitato);
        cmbTipoMovimento.setEnabled(abilitato);
        btnApplicaFiltri.setEnabled(abilitato);
        btnResettaFiltri.setEnabled(abilitato);
    }

    /**
     * Ricerca principale per prodotto (ID, Nome o Scaffale).
     * Se trova risultati, abilita i filtri avanzati.
     */
    private void filtraMovimenti() {
        String termine = txtFiltroProdotto.getText().trim();
        if (termine.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci un termine per cercare.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (termine.length() > 50) {
            JOptionPane.showMessageDialog(this, "La lunghezza del termine di ricerca non può superare i 50 caratteri.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Pulisci la tabella
        tableModel.setRowCount(0);

        List<Movimento> movimenti = movCtrl.getMovimentiFiltratiFlessibili(termine);
        if (movimenti == null || movimenti.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun movimento trovato per il termine: " + termine, "Info", JOptionPane.INFORMATION_MESSAGE);
            codiceProdottoCorrente = null;
            abilitaFiltriAvanzati(false);
            return;
        }

        // Recupera il codice prodotto dal primo movimento trovato
        codiceProdottoCorrente = movimenti.get(0).getProdottoId();

        // Abilita i filtri avanzati
        abilitaFiltriAvanzati(true);

        // Popola la tabella
        popolaTabella(movimenti);
    }

    /**
     * Applica i filtri avanzati (date e tipo) alla ricerca corrente.
     */
    private void applicaFiltriAvanzati() {
        if (codiceProdottoCorrente == null || codiceProdottoCorrente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Effettua prima una ricerca prodotto.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dataInizioStr = txtDataInizio.getText().trim();
        String dataFineStr = txtDataFine.getText().trim();
        String tipoMovimento = (String) cmbTipoMovimento.getSelectedItem();

        if (!dataInizioStr.isEmpty() && !dataFineStr.isEmpty()) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                java.util.Date dataInizio = sdf.parse(dataInizioStr);
                java.util.Date dataFine = sdf.parse(dataFineStr);
                
                if (dataFine.before(dataInizio)) {
                    JOptionPane.showMessageDialog(this, "La data di fine non può essere precedente a quella di inizio.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (java.text.ParseException e) {
                // Il controller gestirà l'eccezione lanciando un IllegalArgumentException 
                // in caso di formato invalido, che verrà mostrata nel blocco catch sottostante
            }
        }

        try {
            List<Movimento> movimenti = movCtrl.getStoricoConFiltri(
                    codiceProdottoCorrente, dataInizioStr, dataFineStr, tipoMovimento);

            tableModel.setRowCount(0);

            if (movimenti == null || movimenti.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nessun movimento trovato per i criteri applicati.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            popolaTabella(movimenti);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore Formato Data", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Resetta i filtri avanzati e ricarica tutti i movimenti del prodotto corrente.
     */
    private void resettaFiltri() {
        txtDataInizio.setText("");
        txtDataFine.setText("");
        cmbTipoMovimento.setSelectedIndex(0);

        if (codiceProdottoCorrente != null && !codiceProdottoCorrente.isEmpty()) {
            tableModel.setRowCount(0);
            List<Movimento> movimenti = movCtrl.getMovimentiFiltratiFlessibili(txtFiltroProdotto.getText().trim());
            if (movimenti != null && !movimenti.isEmpty()) {
                popolaTabella(movimenti);
            }
        }
    }

    /**
     * Popola la tabella con i movimenti forniti.
     */
    private void popolaTabella(List<Movimento> movimenti) {
        for (Movimento m : movimenti) {
            Object[] riga = {
                m.getId(),
                m.getProdottoId() != null ? m.getProdottoId() : "N/A",
                m.getQuantita(),
                m.getTipologia(),
                m.getData()
            };
            tableModel.addRow(riga);
        }
    }
}
