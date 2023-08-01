package adria.sid.ebanckingbackend.exceptions;

public class CompteNotActiveException extends RuntimeException {
    public CompteNotActiveException(String message) {
        super(message);
    }
}
