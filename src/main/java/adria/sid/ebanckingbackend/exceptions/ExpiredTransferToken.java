package adria.sid.ebanckingbackend.exceptions;

public class ExpiredTransferToken extends Throwable {
    public ExpiredTransferToken(String message) {
        super(message);
    }
}
