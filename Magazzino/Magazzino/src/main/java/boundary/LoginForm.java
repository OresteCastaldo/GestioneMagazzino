package boundary;

import controller.LoginController;

import javax.swing.*;
import java.awt.*;

/**
 * Form Boundary per l'autenticazione dell'utente.
 * Comunica esclusivamente con il LoginController.
 * Include un JPasswordField per l'inserimento sicuro della password.
 */
public class LoginForm extends JPanel {

    private LoginController loginCtrl;
    private MainFrame mainFrame;

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnAccedi;
    private JButton btnRegistrati;

    public LoginForm(LoginController loginCtrl, MainFrame mainFrame) {
        this.loginCtrl = loginCtrl;
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titolo
        JLabel lblTitolo = new JLabel("Login - Gestione Magazzino", SwingConstants.CENTER);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitolo, gbc);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(txtEmail, gbc);

        // Password
        JLabel lblPassword = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(txtPassword, gbc);

        // Pulsante Accedi
        btnAccedi = new JButton("Accedi");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(btnAccedi, gbc);

        // Pulsante Registrati
        btnRegistrati = new JButton("Non hai un account? Registrati");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(btnRegistrati, gbc);

        // Action Listeners
        btnAccedi.addActionListener(e -> eseguiLogin());
        btnRegistrati.addActionListener(e -> mainFrame.mostraSchermata("registrazione"));
    }

    private void eseguiLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci l'email.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci la password.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean autenticato = loginCtrl.effettuaLogin(email, password);
        if (autenticato) {
            txtEmail.setText("");
            txtPassword.setText("");
            mainFrame.mostraSchermata("dashboard");
        } else {
            JOptionPane.showMessageDialog(this, "Email o password non corretti.", "Errore di Autenticazione", JOptionPane.ERROR_MESSAGE);
        }
    }
}
