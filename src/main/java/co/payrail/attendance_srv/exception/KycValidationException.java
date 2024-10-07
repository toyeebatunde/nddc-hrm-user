package co.payrail.attendance_srv.exception;


public class KycValidationException extends RuntimeException {

    public KycValidationException() {
        super("Kyc Process not complete");
    }

    public KycValidationException(Throwable cause) {
        super("Kyc Process not complete", cause);
    }

    public KycValidationException(String message) {
        super(message);
    }

    public KycValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
