package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.EGender;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonneTest {

    @Test
    void testGettersAndSetters() {
        // Test the getters and setters
        String personneId = UUID.randomUUID().toString();
        Personne personne = new Personne();
        personne.setId(personneId);
        personne.setNom("kaoutar");
        personne.setPrenom("sougrati");
        personne.setRaisonSociale("jcjkc");
        personne.setRegisterNumber("1234");
        personne.setGender(EGender.FEMALE);
        personne.setPersonneType(EPType.PHYSIQUE);

        assertEquals(personneId, personne.getId());
        assertEquals("kaoutar", personne.getNom());
        assertEquals("sougrati", personne.getPrenom());
        assertEquals("jcjkc", personne.getRaisonSociale());
        assertEquals("FEMALE", personne.getGender().toString());
        assertEquals("PHYSIQUE", personne.getPersonneType().toString());
    }


}
