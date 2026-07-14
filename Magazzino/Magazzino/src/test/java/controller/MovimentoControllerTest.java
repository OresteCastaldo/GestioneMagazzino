package controller;

import database.MovimentoDAO;
import database.ProdottoDAO;
import dto.MovimentoDTO;
import entity.Movimento;
import entity.Prodotto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

/**
 * Test per MovimentoController.
 * Testa registraMovimento() e getStoricoConFiltri() utilizzando Mockito per mock dei DAO.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MovimentoController Tests")
public class MovimentoControllerTest {

    @Mock
    private MovimentoDAO movimentoDAO;

    @Mock
    private ProdottoDAO prodottoDAO;

    private MovimentoController controller;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        controller = new MovimentoController();
        // Inietta i mock tramite reflection
        injectMock(controller, "movimentoDAO", movimentoDAO);
        injectMock(controller, "prodottoDAO", prodottoDAO);
    }

    /**
     * Metodo helper per iniettare i mock tramite reflection
     */
    private void injectMock(Object target, String fieldName, Object mock) throws NoSuchFieldException, IllegalAccessException {
        Field field = MovimentoController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, mock);
    }

    //  Test per registraMovimento()

    @Test
    @DisplayName("registraMovimento - Errore: ID prodotto mancante")
    public void testRegistraMovimento_IdProdottoMancante() {
        // Arrange
        Movimento movimento = new Movimento();
        movimento.setProdottoId(null);

        // Act & Assert
        try {
            controller.registraMovimento(movimento);
            // Se arriva qui significa che l'eccezione non è stata lanciata (test fallito)
        } catch (IllegalArgumentException e) {
            // ✅ Eccezione catturata come previsto
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("registraMovimento - Errore: Prodotto non trovato")
    public void testRegistraMovimento_ProdottoNonTrovato() {
        // Arrange
        Movimento movimento = new Movimento();
        movimento.setProdottoId("PRO999");
        when(prodottoDAO.trovaPerId("PRO999")).thenReturn(null);

        // Act & Assert
        try {
            controller.registraMovimento(movimento);
            // Se arriva qui significa che l'eccezione non è stata lanciata (test fallito)
        } catch (IllegalArgumentException e) {
            // ✅ Eccezione catturata come previsto
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("registraMovimento - Errore: Scarico con quantità insufficiente")
    public void testRegistraMovimento_ScaricQuantitaInsuffiente() {
        // Arrange
        Prodotto prodotto = new Prodotto();
        prodotto.setCodiceId("PRO001");
        prodotto.setQuantitaDisponibile(5);
        prodotto.setSogliaMinDisponibile(10);

        Movimento movimento = new Movimento();
        movimento.setProdottoId("PRO001");
        movimento.setQuantita(10);  // Quantità > disponibile
        movimento.setTipologia("SCARICO");

        when(prodottoDAO.trovaPerId("PRO001")).thenReturn(prodotto);

        // Act & Assert
        try {
            controller.registraMovimento(movimento);
            // Se arriva qui significa che l'eccezione non è stata lanciata (test fallito)
        } catch (IllegalArgumentException e) {
            // ✅ Eccezione catturata come previsto
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("registraMovimento - Scarico completato senza scattare sotto scorta")
    public void testRegistraMovimento_ScaricSenzaSottoScorta() {
        // Arrange
        Prodotto prodotto = new Prodotto();
        prodotto.setCodiceId("PRO001");
        prodotto.setQuantitaDisponibile(100);
        prodotto.setSogliaMinDisponibile(10);
        prodotto.setSottoScorta(false);

        Movimento movimento = new Movimento();
        movimento.setProdottoId("PRO001");
        movimento.setQuantita(20);
        movimento.setTipologia("SCARICO");
        movimento.setData(new Date());

        when(prodottoDAO.trovaPerId("PRO001")).thenReturn(prodotto);

        // Act
        boolean sottoScorta = controller.registraMovimento(movimento);

        // Assert
        assertFalse(sottoScorta, "Prodotto non deve essere sotto scorta");
        assertEquals(80, prodotto.getQuantitaDisponibile(), "Quantità deve essere 80");
        assertFalse(prodotto.isSottoScorta(), "Flag sottoScorta deve restare false");
    }

    @Test
    @DisplayName("registraMovimento - Carico completato, prodotto non è sotto scorta")
    public void testRegistraMovimento_CaricoSenzaSottoScorta() {
        // Arrange
        Prodotto prodotto = new Prodotto();
        prodotto.setCodiceId("PRO001");
        prodotto.setQuantitaDisponibile(50);
        prodotto.setSogliaMinDisponibile(20);
        prodotto.setSottoScorta(false);

        Movimento movimento = new Movimento();
        movimento.setProdottoId("PRO001");
        movimento.setQuantita(30);
        movimento.setTipologia("CARICO");
        movimento.setData(new Date());

        when(prodottoDAO.trovaPerId("PRO001")).thenReturn(prodotto);

        // Act
        boolean sottoScorta = controller.registraMovimento(movimento);

        // Assert
        assertFalse(sottoScorta, "Prodotto non deve scattare sotto scorta");
        assertEquals(80, prodotto.getQuantitaDisponibile(), "Quantità deve essere 80");
        assertFalse(prodotto.isSottoScorta(), "Flag sottoScorta deve restare false");
    }

    @Test
    @DisplayName("registraMovimento - Carico completato, prodotto resta sotto scorta")
    public void testRegistraMovimento_CaricoConSottoScorta() {
        // Arrange
        Prodotto prodotto = new Prodotto();
        prodotto.setCodiceId("PRO001");
        prodotto.setQuantitaDisponibile(5);
        prodotto.setSogliaMinDisponibile(20);
        prodotto.setSottoScorta(true);

        Movimento movimento = new Movimento();
        movimento.setProdottoId("PRO001");
        movimento.setQuantita(10);
        movimento.setTipologia("CARICO");
        movimento.setData(new Date());

        when(prodottoDAO.trovaPerId("PRO001")).thenReturn(prodotto);

        // Act
        boolean sottoScorta = controller.registraMovimento(movimento);

        // Assert
        assertTrue(sottoScorta, "Prodotto deve scattare sotto scorta");
        assertEquals(15, prodotto.getQuantitaDisponibile(), "Quantità deve essere 15");
        assertTrue(prodotto.isSottoScorta(), "Flag sottoScorta deve restare true (15 < 20)");
    }

    // ===== Test per getStoricoConFiltri() =====

    @Test
    @DisplayName("getStoricoConFiltri - Nessun filtro applicato")
    public void testGetStoricoConFiltri_NessunFiltro() {
        // Arrange
        List<Movimento> movimenti = new ArrayList<>();
        Movimento m1 = new Movimento();
        m1.setId(1L);
        movimenti.add(m1);

        when(movimentoDAO.ricercaStoricoConFiltri("PRO001", null, null, null))
                .thenReturn(movimenti);

        // Act
        List<Movimento> risultato = controller.getStoricoConFiltri("PRO001", null, null, "Tutti");

        // Assert
        assertNotNull(risultato);
        assertEquals(1, risultato.size());
    }

    @Test
    @DisplayName("getStoricoConFiltri - Filtro solo su dataInizio")
    public void testGetStoricoConFiltri_FiltroDataInizio() {
        // Arrange
        Date dataInizio = new Date(1000000000000L);  // Data di partenza
        
        // Movimento con data INFERIORE a dataInizio (NON deve essere ritornato)
        Movimento m1 = new Movimento();
        m1.setId(1L);
        m1.setData(new Date(900000000000L));
        
        // Movimento con data SUPERIORE a dataInizio (deve essere ritornato)
        Movimento m2 = new Movimento();
        m2.setId(2L);
        m2.setData(new Date(1100000000000L));
        
        List<Movimento> movimenti = new ArrayList<>();
        movimenti.add(m2);  // Solo il movimento con data superiore

        when(movimentoDAO.ricercaStoricoConFiltri(eq("PRO001"), any(Date.class), isNull(), isNull()))
                .thenReturn(movimenti);

        // Act
        List<Movimento> risultato = controller.getStoricoConFiltri("PRO001", dataInizio, null, "Tutti");

        // Assert
        assertNotNull(risultato);
        assertEquals(1, risultato.size());
        assertEquals(2L, risultato.get(0).getId(), "Deve contenere solo il movimento con data superiore a dataInizio");
    }

    @Test
    @DisplayName("getStoricoConFiltri - Filtro solo su dataFine")
    public void testGetStoricoConFiltri_FiltroDataFine() {
        // Arrange
        Date dataFine = new Date(2000000000000L);  // Data di fine
        
        // Movimento con data INFERIORE a dataFine (deve essere ritornato)
        Movimento m1 = new Movimento();
        m1.setId(1L);
        m1.setData(new Date(1900000000000L));
        
        // Movimento con data SUPERIORE a dataFine (NON deve essere ritornato)
        Movimento m2 = new Movimento();
        m2.setId(2L);
        m2.setData(new Date(2100000000000L));
        
        List<Movimento> movimenti = new ArrayList<>();
        movimenti.add(m1);  // Solo il movimento con data inferiore

        when(movimentoDAO.ricercaStoricoConFiltri(eq("PRO001"), isNull(), any(Date.class), isNull()))
                .thenReturn(movimenti);

        // Act
        List<Movimento> risultato = controller.getStoricoConFiltri("PRO001", null, dataFine, "Tutti");

        // Assert
        assertNotNull(risultato);
        assertEquals(1, risultato.size());
        assertEquals(1L, risultato.get(0).getId(), "Deve contenere solo il movimento con data inferiore a dataFine");
    }

    @Test
    @DisplayName("getStoricoConFiltri - Filtro solo su tipoMovimento")
    public void testGetStoricoConFiltri_FiltroTipoMovimento() {
        // Arrange
        List<Movimento> movimenti = new ArrayList<>();
        Movimento m1 = new Movimento();
        m1.setId(1L);
        m1.setTipologia("CARICO");
        movimenti.add(m1);

        when(movimentoDAO.ricercaStoricoConFiltri("PRO001", null, null, "CARICO"))
                .thenReturn(movimenti);

        // Act
        List<Movimento> risultato = controller.getStoricoConFiltri("PRO001", null, null, "CARICO");

        // Assert
        assertNotNull(risultato);
        assertEquals(1, risultato.size());
        assertEquals("CARICO", risultato.get(0).getTipologia());
    }
}
