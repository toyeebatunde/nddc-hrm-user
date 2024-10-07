package co.payrail.attendance_srv.kyc_srv.exceptions;

public class PinExistException  extends RuntimeException {
    public PinExistException(){super("Failed to process password");}

    public PinExistException(Throwable cause){super("Failed to process password",cause);}


    public PinExistException(String message){super(message);}

    public PinExistException(String message, Throwable cause){super(message,cause);}
}
