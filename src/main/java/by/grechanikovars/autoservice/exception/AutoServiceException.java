package by.grechanikovars.autoservice.exception;

public class AutoServiceException extends Exception {

    public AutoServiceException(String message) {
        super(message);
    }

    public AutoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
