package adria.sid.ebanckingbackend.ennumerations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EtatCompteTest {

    @Test
    void testEnumValues() {
        EtatCompte suspendu = EtatCompte.SUSPENDU;
        EtatCompte active = EtatCompte.ACTIVE;
        EtatCompte bloque = EtatCompte.BLOCKE;

        assertNotNull(suspendu);
        assertNotNull(active);
        assertNotNull(bloque);
    }

    @Test
    void testEnumToString() {
        EtatCompte suspendu = EtatCompte.SUSPENDU;
        EtatCompte active = EtatCompte.ACTIVE;
        EtatCompte bloque = EtatCompte.BLOCKE;

        assertEquals("SUSPENDU", suspendu.toString());
        assertEquals("ACTIVE", active.toString());
        assertEquals("BLOCKE", bloque.toString());
    }

    @Test
    void testEnumValueOf() {
        EtatCompte suspendu = EtatCompte.valueOf("SUSPENDU");
        EtatCompte active = EtatCompte.valueOf("ACTIVE");
        EtatCompte bloque = EtatCompte.valueOf("BLOCKE");

        assertEquals(EtatCompte.SUSPENDU, suspendu);
        assertEquals(EtatCompte.ACTIVE, active);
        assertEquals(EtatCompte.BLOCKE, bloque);
    }

    @Test
    void testEnumOrdinal() {
        EtatCompte suspendu = EtatCompte.SUSPENDU;
        EtatCompte active = EtatCompte.ACTIVE;
        EtatCompte bloque = EtatCompte.BLOCKE;

        assertEquals(0, suspendu.ordinal());
        assertEquals(1, active.ordinal());
        assertEquals(2, bloque.ordinal());
    }
}
