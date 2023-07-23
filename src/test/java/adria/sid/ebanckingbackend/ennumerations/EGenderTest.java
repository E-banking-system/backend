package adria.sid.ebanckingbackend.ennumerations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EGenderTest {

    @Test
    void testEnumValues() {
        EGender male = EGender.MALE;
        EGender female = EGender.FEMALE;

        assertNotNull(male);
        assertNotNull(female);
    }

    @Test
    void testEnumToString() {
        EGender male = EGender.MALE;
        EGender female = EGender.FEMALE;

        assertEquals("MALE", male.toString());
        assertEquals("FEMALE", female.toString());
    }

    @Test
    void testEnumValueOf() {
        EGender male = EGender.valueOf("MALE");
        EGender female = EGender.valueOf("FEMALE");

        assertEquals(EGender.MALE, male);
        assertEquals(EGender.FEMALE, female);
    }

    @Test
    void testEnumOrdinal() {
        EGender male = EGender.MALE;
        EGender female = EGender.FEMALE;

        assertEquals(0, male.ordinal());
        assertEquals(1, female.ordinal());
    }
}
