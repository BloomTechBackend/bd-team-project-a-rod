package golfcalculator.exceptions;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6922176688543503951L;

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
