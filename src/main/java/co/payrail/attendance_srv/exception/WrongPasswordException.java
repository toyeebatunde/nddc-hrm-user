package co.payrail.attendance_srv.exception;

public class WrongPasswordException extends PasswordException{

    public WrongPasswordException(){super("Incorrect old password provided");}


    public WrongPasswordException(String message){super(message);}


}
