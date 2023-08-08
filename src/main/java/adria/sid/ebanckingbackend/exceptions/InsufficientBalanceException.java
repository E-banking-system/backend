package adria.sid.ebanckingbackend.exceptions;

public class InsufficientBalanceException extends Throwable {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
