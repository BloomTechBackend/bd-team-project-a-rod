package golfcalculator.exceptions;

public class UserIdAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = -9019916778796440883L;

    public UserIdAlreadyExistsException() {
        super();
    }

    public UserIdAlreadyExistsException(String message) {
        super(message);
    }

    public UserIdAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public UserIdAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
