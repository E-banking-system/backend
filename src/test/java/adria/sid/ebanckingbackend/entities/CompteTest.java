package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CompteTest {

    @Test
    void testGettersAndSetters() {
        // Test the getters and setters
        String compteId = UUID.randomUUID().toString();
        Compte compte = new Compte();
        compte.setId(compteId);
        compte.setNature("Savings");
        compte.setSolde(1500.0);
        compte.setRIB("123456789");
        compte.setNumCompte(String.valueOf(987654321L));
        compte.setDateCreation(new Date());
        compte.setDatePeremption(new Date());
        compte.setDerniereDateSuspention(new Date());
        compte.setDerniereDateBloquage(new Date());
        compte.setEtatCompte(EtatCompte.SUSPENDU); // Set the initial state to suspended

        assertEquals(compteId, compte.getId());
        assertEquals("Savings", compte.getNature());
        assertEquals(1500.0, compte.getSolde());
        assertEquals("123456789", compte.getRIB());
        assertEquals(987654321L, compte.getNumCompte());
        assertNotNull(compte.getDateCreation());
        assertNotNull(compte.getDatePeremption());
        assertNotNull(compte.getDerniereDateSuspention());
        assertNotNull(compte.getDerniereDateBloquage());
        assertEquals(EtatCompte.SUSPENDU, compte.getEtatCompte());
    }

    @Test
    void testActiverCompte() {
        // Test the activerCompte() method
        Compte compte = new Compte();
        compte.activerCompte();

        assertEquals(EtatCompte.ACTIVE, compte.getEtatCompte());
    }

    @Test
    void testBlockerCompte() {
        // Test the blockerCompte() method
        Compte compte = new Compte();
        compte.blockerCompte();

        assertEquals(EtatCompte.BLOCKE, compte.getEtatCompte());
    }

    @Test
    void testSuspenduCompte() {
        // Test the suspenduCompte() method
        Compte compte = new Compte();
        compte.suspenduCompte();

        assertEquals(EtatCompte.SUSPENDU, compte.getEtatCompte());
    }
}
