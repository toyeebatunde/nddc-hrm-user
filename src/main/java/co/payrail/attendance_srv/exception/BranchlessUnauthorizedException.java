package co.payrail.attendance_srv.exception;


public class BranchlessUnauthorizedException extends RuntimeException {

    public BranchlessUnauthorizedException(String message) {
        super(message);
    }

}
