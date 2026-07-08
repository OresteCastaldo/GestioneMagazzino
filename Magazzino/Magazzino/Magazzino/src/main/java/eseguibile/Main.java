package eseguibile;

import boundary.MainFrame;
import javax.swing.SwingUtilities;

/**
 * Classe eseguibile per avviare l'applicazione Gestione Magazzino.
 */
public class Main {
    public static void main(String[] args) {
        // Avvia l'interfaccia grafica in modo thread-safe
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
