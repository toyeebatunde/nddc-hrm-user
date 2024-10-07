package co.payrail.attendance_srv.exception;

public class WrongPinException extends PasswordException{

    public WrongPinException(){super("Incorrect old pin provided");}


    public WrongPinException(String message){super(message);}


}
