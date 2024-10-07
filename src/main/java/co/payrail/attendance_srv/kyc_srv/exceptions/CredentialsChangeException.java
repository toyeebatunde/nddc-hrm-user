package co.payrail.attendance_srv.kyc_srv.exceptions;

public class CredentialsChangeException extends RuntimeException {

    public CredentialsChangeException(){super("Failed to process password");}

    public CredentialsChangeException(Throwable cause){super("Failed to process password",cause);}


    public CredentialsChangeException(String message){super(message);}

    public CredentialsChangeException(String message, Throwable cause){super(message,cause);}
}
