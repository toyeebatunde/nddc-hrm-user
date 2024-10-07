package co.payrail.attendance_srv.integration.dojah.exceptions;

import co.payrail.attendance_srv.exception.BranchlessBankingException;

public class DojahExceptions extends BranchlessBankingException {
    public DojahExceptions() {
        super("Failed to perform the requested action");
    }

    public DojahExceptions(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public DojahExceptions(String message) {
        super(message);
    }

    public DojahExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
