package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.EGender;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeneficierTest {

    @Test
    void testGettersAndSetters() {
        // Test the getters and setters
        String beneficierID = UUID.randomUUID().toString();
        Beneficier beneficier = new Beneficier();
        beneficier.setId(beneficierID);
        beneficier.setNom("kaoutar");
        beneficier.setPrenom("sougrati");
        beneficier.setRaisonSociale("jcjkc");
        beneficier.setRegisterNumber("1234");
        beneficier.setGender(EGender.FEMALE);
        beneficier.setPersonneType(EPType.PHYSIQUE);

        assertEquals(beneficierID, beneficier.getId());
        assertEquals("kaoutar", beneficier.getNom());
        assertEquals("sougrati", beneficier.getPrenom());
        assertEquals("jcjkc", beneficier.getRaisonSociale());
        assertEquals("FEMALE", beneficier.getGender().toString());
        assertEquals("PHYSIQUE", beneficier.getPersonneType().toString());
    }

    @Test
    void testVirement() {
        Beneficier beneficier = new Beneficier();

        Virement virement = new Virement();
        String virementId = UUID.randomUUID().toString();

        virement.setId(virementId);
        virement.setMontant(2000.0);
        virement.setDateOperation(new Date());


        Virement virement2 = new Virement();
        String virementId2 = UUID.randomUUID().toString();

        virement2.setId(virementId2);
        virement2.setMontant(3000.0);
        virement2.setDateOperation(new Date());

        //beneficier.addVirement(virement);
        //beneficier.addVirement(virement2);

        assertEquals(2, beneficier.getVirements().size());
        assertTrue(beneficier.getVirements().contains(virement));
        assertTrue(beneficier.getVirements().contains(virement2));

    }

}
