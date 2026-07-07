package boundary;

import controller.GestoreMagazzino;
import entity.Movimento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Form Boundary per la visualizzazione dello storico dei movimenti.
 * Comunica esclusivamente con il GestoreMagazzino (Facade).
 */
public class StoricoMovimentiForm extends JPanel {

    private GestoreMagazzino gestore;
    private MainFrame mainFrame;

    private JTextField txtFiltroProdotto;
    private JButton btnFiltra;
    private JButton btnTornaDashboard;
    private JTable tabellaMovimenti;
    private DefaultTableModel tableModel;

    public StoricoMovimentiForm(GestoreMagazzino gestore, MainFrame mainFrame) {
        this.gestore = gestore;
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Titolo
        JLabel lblTitolo = new JLabel("Storico Movimenti", SwingConstants.CENTER);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));

        // Pannello filtro
        JPanel panelFiltro = new JPanel(new FlowLayout());
        JLabel lblFiltro = new JLabel("Codice Prodotto:");
        txtFiltroProdotto = new JTextField(15);
        btnFiltra = new JButton("Filtra");
        panelFiltro.add(lblFiltro);
        panelFiltro.add(txtFiltroProdotto);
        panelFiltro.add(btnFiltra);

        // Pannello top
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(lblTitolo, BorderLayout.NORTH);
        panelTop.add(panelFiltro, BorderLayout.SOUTH);
        add(panelTop, BorderLayout.NORTH);

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
        btnTornaDashboard.addActionListener(e -> mainFrame.mostraSchermata("dashboard"));
    }

    private void filtraMovimenti() {
        String prodottoId = txtFiltroProdotto.getText().trim();
        if (prodottoId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci il codice prodotto per filtrare.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Pulisci la tabella
        tableModel.setRowCount(0);

        List<Movimento> movimenti = gestore.ApplicaFiltro(prodottoId);
        if (movimenti == null || movimenti.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun movimento trovato per il prodotto: " + prodottoId, "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

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
