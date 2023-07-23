package adria.sid.ebanckingbackend.ennumerations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EPTypeTest {

    @Test
    void testEnumValues() {
        EPType morale = EPType.MORALE;
        EPType physique = EPType.PHYSIQUE;

        assertNotNull(morale);
        assertNotNull(physique);
    }

    @Test
    void testEnumToString() {
        EPType morale = EPType.MORALE;
        EPType physique = EPType.PHYSIQUE;

        assertEquals("MORALE", morale.toString());
        assertEquals("PHYSIQUE", physique.toString());
    }

    @Test
    void testEnumValueOf() {
        EPType morale = EPType.valueOf("MORALE");
        EPType physique = EPType.valueOf("PHYSIQUE");

        assertEquals(EPType.MORALE, morale);
        assertEquals(EPType.PHYSIQUE, physique);
    }

    @Test
    void testEnumOrdinal() {
        EPType morale = EPType.MORALE;
        EPType physique = EPType.PHYSIQUE;

        assertEquals(0, morale.ordinal());
        assertEquals(1, physique.ordinal());
    }
}
