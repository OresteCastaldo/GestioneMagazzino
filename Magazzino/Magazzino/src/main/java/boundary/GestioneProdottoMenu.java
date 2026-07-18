package boundary;

import javax.swing.*;
import java.awt.*;

/**
 * Form Boundary che funge da sottomenu per la Gestione dei Prodotti.
 * Accessibile solo al Responsabile.
 * Non necessita di alcun Controller, naviga solo tra schermate.
 */
public class GestioneProdottoMenu extends JPanel {

    private MainFrame mainFrame;

    public GestioneProdottoMenu(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Titolo
        JLabel lblTitolo = new JLabel("Sottomenu - Gestione Prodotto", SwingConstants.CENTER);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 0;
        add(lblTitolo, gbc);

        // Inserimento Prodotto
        JButton btnInserimento = new JButton("Inserimento Prodotto");
        btnInserimento.addActionListener(e -> mainFrame.mostraSchermataProdotto("INSERIMENTO"));
        gbc.gridy = 1;
        add(btnInserimento, gbc);

        // Ricerca Prodotto
        JButton btnRicerca = new JButton("Ricerca Prodotto");
        btnRicerca.addActionListener(e -> mainFrame.mostraSchermataProdotto("RICERCA"));
        gbc.gridy = 2;
        add(btnRicerca, gbc);

        // Torna alla Dashboard
        JButton btnTorna = new JButton("Torna alla Dashboard");
        btnTorna.addActionListener(e -> mainFrame.mostraSchermata("dashboard"));
        gbc.gridy = 3;
        add(btnTorna, gbc);
    }
}
