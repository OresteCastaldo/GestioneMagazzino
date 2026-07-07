package boundary;

import controller.GestoreMagazzino;
import entity.Movimento;
import entity.Operatore;
import entity.Prodotto;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * Form Boundary per la registrazione di un movimento di carico o scarico.
 * Comunica esclusivamente con il GestoreMagazzino (Facade).
 */
public class MovimentoForm extends JPanel {

    private GestoreMagazzino gestore;
    private MainFrame mainFrame;

    private JTextField txtCodiceProdotto;
    private JTextField txtQuantita;
    private JComboBox<String> cmbTipologia;
    private JButton btnRegistra;
    private JButton btnTornaDashboard;

    public MovimentoForm(GestoreMagazzino gestore, MainFrame mainFrame) {
        this.gestore = gestore;
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titolo
        JLabel lblTitolo = new JLabel("Registra Movimento", SwingConstants.CENTER);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitolo, gbc);

        gbc.gridwidth = 1;

        // Codice Prodotto
        JLabel lblCodice = new JLabel("Codice Prodotto:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblCodice, gbc);

        txtCodiceProdotto = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(txtCodiceProdotto, gbc);

        // Quantità
        JLabel lblQuantita = new JLabel("Quantità:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblQuantita, gbc);

        txtQuantita = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(txtQuantita, gbc);

        // Tipologia
        JLabel lblTipologia = new JLabel("Tipologia:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblTipologia, gbc);

        cmbTipologia = new JComboBox<>(new String[]{"CARICO", "SCARICO"});
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(cmbTipologia, gbc);

        // Pulsanti
        btnRegistra = new JButton("Registra Movimento");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(btnRegistra, gbc);

        btnTornaDashboard = new JButton("Torna alla Dashboard");
        gbc.gridy = 5;
        add(btnTornaDashboard, gbc);

        // Action Listeners
        btnRegistra.addActionListener(e -> registraMovimento());
        btnTornaDashboard.addActionListener(e -> mainFrame.mostraSchermata("dashboard"));
    }

    private void registraMovimento() {
        String codiceProdotto = txtCodiceProdotto.getText().trim();
        String quantitaStr = txtQuantita.getText().trim();
        String tipologia = (String) cmbTipologia.getSelectedItem();

        if (codiceProdotto.isEmpty() || quantitaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantita = Integer.parseInt(quantitaStr);

            Prodotto prodotto = gestore.CercaProdotto(codiceProdotto);
            if (prodotto == null) {
                JOptionPane.showMessageDialog(this, "Prodotto non trovato con codice: " + codiceProdotto, "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Movimento movimento = new Movimento();
            movimento.setQuantita(quantita);
            movimento.setData(new Date());
            movimento.setTipologia(tipologia);
            movimento.setProdottoId(prodotto.getCodiceId());

            gestore.RegistraMovimento(movimento);
            JOptionPane.showMessageDialog(this, "Movimento registrato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);

            // Pulisci i campi
            txtCodiceProdotto.setText("");
            txtQuantita.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La quantità deve essere un numero intero.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}
