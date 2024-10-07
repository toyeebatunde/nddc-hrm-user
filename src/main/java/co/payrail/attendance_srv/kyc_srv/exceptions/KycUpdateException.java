package co.payrail.attendance_srv.kyc_srv.exceptions;

import co.payrail.attendance_srv.exception.BranchlessBankingException;

public class KycUpdateException extends BranchlessBankingException {

    public KycUpdateException(){super("Failed to process password");}

    public KycUpdateException(Throwable cause){super("Failed to process password",cause);}


    public KycUpdateException(String message){super(message);}

    public KycUpdateException(String message, Throwable cause){super(message,cause);}

}