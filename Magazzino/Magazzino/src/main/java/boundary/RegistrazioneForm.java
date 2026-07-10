package boundary;

import controller.RegistrationController;
import entity.Operatore;
import entity.Responsabile;
import entity.Utente;

import javax.swing.*;
import java.awt.*;

/**
 * Form Boundary per la registrazione di un nuovo utente.
 * Permette di scegliere tra Operatore e Responsabile.
 * Comunica esclusivamente con il RegistrationController.
 */
public class RegistrazioneForm extends JPanel {

    private RegistrationController regCtrl;
    private MainFrame mainFrame;

    private JTextField txtNome;
    private JTextField txtCognome;
    private JTextField txtEmail;
    private JComboBox<String> cmbTipoUtente;
    private JButton btnRegistrati;
    private JButton btnTornaLogin;

    public RegistrazioneForm(RegistrationController regCtrl, MainFrame mainFrame) {
        this.regCtrl = regCtrl;
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titolo
        JLabel lblTitolo = new JLabel("Registrazione Utente", SwingConstants.CENTER);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitolo, gbc);

        // Nome
        gbc.gridwidth = 1;
        JLabel lblNome = new JLabel("Nome:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblNome, gbc);

        txtNome = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(txtNome, gbc);

        // Cognome
        JLabel lblCognome = new JLabel("Cognome:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblCognome, gbc);

        txtCognome = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(txtCognome, gbc);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(txtEmail, gbc);

        // Tipo Utente
        JLabel lblTipo = new JLabel("Tipo Utente:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(lblTipo, gbc);

        cmbTipoUtente = new JComboBox<>(new String[]{"OPERATORE", "RESPONSABILE"});
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(cmbTipoUtente, gbc);

        // Pulsante Registrati
        btnRegistrati = new JButton("Registrati");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(btnRegistrati, gbc);

        // Torna al Login
        btnTornaLogin = new JButton("Torna al Login");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(btnTornaLogin, gbc);

        // Action Listeners
        btnRegistrati.addActionListener(e -> eseguiRegistrazione());
        btnTornaLogin.addActionListener(e -> mainFrame.mostraSchermata("login"));
    }

    private void eseguiRegistrazione() {
        String nome = txtNome.getText().trim();
        String cognome = txtCognome.getText().trim();
        String email = txtEmail.getText().trim();
        String tipo = (String) cmbTipoUtente.getSelectedItem();

        if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Utente utente;
        if ("OPERATORE".equals(tipo)) {
            utente = new Operatore();
        } else {
            utente = new Responsabile();
        }
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setEmail(email);

        try {
            regCtrl.registraNuovoUtente(utente);
            JOptionPane.showMessageDialog(this, "Registrazione avvenuta con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);

            // Pulisci i campi
            txtNome.setText("");
            txtCognome.setText("");
            txtEmail.setText("");

            mainFrame.mostraSchermata("login");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
        }
    }
}
