package co.payrail.attendance_srv.exception;


public class BranchlessInvalidRequestException extends RuntimeException {

    public BranchlessInvalidRequestException(String message) {
        super(message);
    }

}
