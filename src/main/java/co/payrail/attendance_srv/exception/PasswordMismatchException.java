package co.payrail.attendance_srv.exception;

public class PasswordMismatchException extends PasswordException{

    public PasswordMismatchException(){super("Passwords do not match");}


    public PasswordMismatchException(String message){super(message);}


}
