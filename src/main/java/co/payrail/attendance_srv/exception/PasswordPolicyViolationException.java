package co.payrail.attendance_srv.exception;

public class PasswordPolicyViolationException extends PasswordException {

    public PasswordPolicyViolationException(){
        super("Password does not meet the password ploicy");
    }

    public PasswordPolicyViolationException(String message){super(message);}



}
