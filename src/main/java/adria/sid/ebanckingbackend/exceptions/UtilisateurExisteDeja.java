package adria.sid.ebanckingbackend.exceptions;

public class UtilisateurExisteDeja extends RuntimeException {
    public UtilisateurExisteDeja(String message) {
        super(message);
    }
}
