package co.payrail.attendance_srv.exception;


public class LoanApplicationException extends RuntimeException {

    public LoanApplicationException() {
        super("Failed to perform the requested action");
    }

    public LoanApplicationException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public LoanApplicationException(String message) {
        super(message);
    }

    public LoanApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
