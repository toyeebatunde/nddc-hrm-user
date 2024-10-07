package co.payrail.attendance_srv.kyc_srv.exceptions;

import co.payrail.attendance_srv.exception.BranchlessBankingException;

public class UserCreationException extends BranchlessBankingException {

    public UserCreationException(){super("Failed to process password");}

    public UserCreationException(Throwable cause){super("Failed to process password",cause);}


    public UserCreationException(String message){super(message);}

    public UserCreationException(String message, Throwable cause){super(message,cause);}

}
