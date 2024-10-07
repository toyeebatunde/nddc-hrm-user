package co.payrail.attendance_srv.exception;

public class DuplicateObjectException extends BranchlessBankingException {

    public DuplicateObjectException(){super("The target object already exists");}

    public DuplicateObjectException(String message){super(message);}
}
