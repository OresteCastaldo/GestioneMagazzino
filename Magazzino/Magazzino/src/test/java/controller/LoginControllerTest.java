package controller;

import entity.Operatore;
import entity.Responsabile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per LoginController.
 * Testa i metodi getRuoloUtenteCorrente() e getEmailUtenteCorrente().
 */
@DisplayName("LoginController Tests")
public class LoginControllerTest {

    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        loginController = new LoginController();
    }

    //  Test per getRuoloUtenteCorrente()

    @Test
    @DisplayName("getRuoloUtenteCorrente - deve ritornare null quando utente corrente é null")
    public void testGetRuoloUtenteCorrente_UtenteNull() {
        // Arrange
        
        // Act
        String ruolo = loginController.getRuoloUtenteCorrente();
        
        // Assert
        assertNull(ruolo, "Deve restituire null quando nessun utente è loggato");
    }

    @Test
    @DisplayName("getRuoloUtenteCorrente - deve ritornare RESPONSABILE quando utente corrente é Responsabile")
    public void testGetRuoloUtenteCorrente_Responsabile() {
        // Arrange
        Responsabile responsabile = new Responsabile();
        loginController.setUtenteCorrenteForTest(responsabile);
        
        // Act
        String ruolo = loginController.getRuoloUtenteCorrente();
        
        // Assert
        assertEquals("RESPONSABILE", ruolo);
    }

    @Test
    @DisplayName("getRuoloUtenteCorrente - deve ritornare OPERATORE quando utente corrente é Operatore")
    public void testGetRuoloUtenteCorrente_Operatore() {
        // Arrange
        Operatore operatore = new Operatore();
        loginController.setUtenteCorrenteForTest(operatore);
        
        // Act
        String ruolo = loginController.getRuoloUtenteCorrente();
        
        // Assert
        assertEquals("OPERATORE", ruolo);
    }

    //  Test per getEmailUtenteCorrente()

    @Test
    @DisplayName("getEmailUtenteCorrente - deve ritornare null quando utente corrente é null")
    public void testGetEmailUtenteCorrente_UtenteNull() {
        // Arrange
        
        // Act
        String email = loginController.getEmailUtenteCorrente();
        
        // Assert
        assertNull(email, "Deve restituire null quando nessun utente è loggato");
    }

    @Test
    @DisplayName("getEmailUtenteCorrente - deve ritornare email quando utente corrente non é null")
    public void testGetEmailUtenteCorrente_UtenteNotNull() {
        // Arrange
        Operatore operatore = new Operatore();
        operatore.setEmail("operatore@magazzino.it");
        loginController.setUtenteCorrenteForTest(operatore);
        
        // Act
        String email = loginController.getEmailUtenteCorrente();
        
        // Assert
        assertEquals("operatore@magazzino.it", email);
    }
}
