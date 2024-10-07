package co.payrail.attendance_srv.exception;

public class PasswordException extends BranchlessBankingException {

    public PasswordException(){super("Failed to process password");}

    public PasswordException(Throwable cause){super("Failed to process password",cause);}


    public PasswordException(String message){super(message);}

    public PasswordException(String message, Throwable cause){super(message,cause);}

}
