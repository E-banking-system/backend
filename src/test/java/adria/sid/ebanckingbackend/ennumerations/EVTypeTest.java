package adria.sid.ebanckingbackend.ennumerations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EVTypeTest {

    @Test
    void testEnumValues() {
        EVType hebdomadaire = EVType.HEBDOMADAIRE;
        EVType bimensuelle = EVType.BIMENSUELLE;
        EVType mensuelle = EVType.MENSUELLE;
        EVType trimestrielle = EVType.TRIMESTRIELLE;
        EVType semestrielle = EVType.SEMESTRIELLE;

        assertNotNull(hebdomadaire);
        assertNotNull(bimensuelle);
        assertNotNull(mensuelle);
        assertNotNull(trimestrielle);
        assertNotNull(semestrielle);
    }

    @Test
    void testEnumToString() {
        EVType hebdomadaire = EVType.HEBDOMADAIRE;
        EVType bimensuelle = EVType.BIMENSUELLE;
        EVType mensuelle = EVType.MENSUELLE;
        EVType trimestrielle = EVType.TRIMESTRIELLE;
        EVType semestrielle = EVType.SEMESTRIELLE;

        assertEquals("HEBDOMADAIRE", hebdomadaire.toString());
        assertEquals("BIMENSUELLE", bimensuelle.toString());
        assertEquals("MENSUELLE", mensuelle.toString());
        assertEquals("TRIMESTRIELLE", trimestrielle.toString());
        assertEquals("SEMESTRIELLE", semestrielle.toString());
    }

    @Test
    void testEnumValueOf() {
        EVType hebdomadaire = EVType.valueOf("HEBDOMADAIRE");
        EVType bimensuelle = EVType.valueOf("BIMENSUELLE");
        EVType mensuelle = EVType.valueOf("MENSUELLE");
        EVType trimestrielle = EVType.valueOf("TRIMESTRIELLE");
        EVType semestrielle = EVType.valueOf("SEMESTRIELLE");

        assertEquals(EVType.HEBDOMADAIRE, hebdomadaire);
        assertEquals(EVType.BIMENSUELLE, bimensuelle);
        assertEquals(EVType.MENSUELLE, mensuelle);
        assertEquals(EVType.TRIMESTRIELLE, trimestrielle);
        assertEquals(EVType.SEMESTRIELLE, semestrielle);
    }

    @Test
    void testEnumOrdinal() {
        EVType hebdomadaire = EVType.HEBDOMADAIRE;
        EVType bimensuelle = EVType.BIMENSUELLE;
        EVType mensuelle = EVType.MENSUELLE;
        EVType trimestrielle = EVType.TRIMESTRIELLE;
        EVType semestrielle = EVType.SEMESTRIELLE;

        assertEquals(0, hebdomadaire.ordinal());
        assertEquals(1, bimensuelle.ordinal());
        assertEquals(2, mensuelle.ordinal());
        assertEquals(3, trimestrielle.ordinal());
        assertEquals(4, semestrielle.ordinal());
    }
}
