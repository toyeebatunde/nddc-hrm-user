package co.payrail.attendance_srv.exception;


public class InvalidTranRequestException extends RuntimeException {

    public InvalidTranRequestException() {
        super("Failed to perform the requested action");
    }

    public InvalidTranRequestException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public InvalidTranRequestException(String message) {
        super(message);
    }

    public InvalidTranRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
