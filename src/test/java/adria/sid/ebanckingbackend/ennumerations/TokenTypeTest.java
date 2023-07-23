package adria.sid.ebanckingbackend.ennumerations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TokenTypeTest {

    @Test
    void testEnumValues() {
        TokenType bearer = TokenType.BEARER;

        assertNotNull(bearer);
    }

    @Test
    void testEnumToString() {
        TokenType bearer = TokenType.BEARER;

        assertEquals("BEARER", bearer.toString());
    }

    @Test
    void testEnumValueOf() {
        TokenType bearer = TokenType.valueOf("BEARER");

        assertEquals(TokenType.BEARER, bearer);
    }

    @Test
    void testEnumOrdinal() {
        TokenType bearer = TokenType.BEARER;

        assertEquals(0, bearer.ordinal());
    }
}
