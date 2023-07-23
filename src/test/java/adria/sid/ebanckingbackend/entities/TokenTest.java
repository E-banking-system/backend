package adria.sid.ebanckingbackend.entities;

import static org.junit.jupiter.api.Assertions.*;

import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.ennumerations.TokenType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("test");
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }

    @AfterEach
    void tearDown() {
        if (em != null) em.close();
        if (emf != null) emf.close();
    }

    @Test
    void testTokenCreation() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(ERole.USER);

        Token token = new Token(0,"test-token", TokenType.BEARER, false, false, user);
        assertNotNull(token);
        assertEquals("test-token", token.getToken());
        assertEquals(TokenType.BEARER, token.getTokenType());
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());
        assertEquals(user, token.getUser());
    }

    @Test
    void testTokenSettersAndGetters() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(ERole.USER);

        Token token = new Token();
        token.setToken("test-token");
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(false);
        token.setExpired(false);
        token.setUser(user);

        assertEquals("test-token", token.getToken());
        assertEquals(TokenType.BEARER, token.getTokenType());
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());
        assertEquals(user, token.getUser());
    }

    @Test
    void testTokenToString() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(ERole.USER);

        Token token = new Token(0,"test-token", TokenType.BEARER, false, false, user);
        String expectedToString = "Token(id=null, token='test-token', tokenType=BEARER, revoked=false, expired=false, user=UserEntity(id=null, email='test@example.com', password='password', notifications=null, comptes=null, role=USER, virements=null, messages=null, tokens=null))";
        assertEquals(expectedToString, token.toString());
    }

    @Test
    void testTokenPersistence() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(ERole.USER);

        Token token = new Token(0,"test-token", TokenType.BEARER, false, false, user);

        try {
            tx.begin();
            em.persist(user);
            em.persist(token);
            tx.commit();

            Token foundToken = em.find(Token.class, token.getId());
            assertNotNull(foundToken);
            assertEquals(token.getToken(), foundToken.getToken());
            assertEquals(token.getTokenType(), foundToken.getTokenType());
            assertEquals(token.isRevoked(), foundToken.isRevoked());
            assertEquals(token.isExpired(), foundToken.isExpired());
            assertEquals(token.getUser(), foundToken.getUser());
        } catch (RollbackException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            fail("Error persisting entities: " + e.getMessage());
        }
    }
}
