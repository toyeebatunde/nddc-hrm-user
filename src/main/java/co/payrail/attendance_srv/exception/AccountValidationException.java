package co.payrail.attendance_srv.exception;


public class AccountValidationException extends RuntimeException {

    public AccountValidationException() {
        super("Failed to perform the requested action");
    }

    public AccountValidationException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public AccountValidationException(String message) {
        super(message);
    }

    public AccountValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
