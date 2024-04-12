package golfcalculator.exceptions;

public class InvalidUserNameException extends RuntimeException {

    private static final long serialVersionUID = -8428038244163344447L;

    public InvalidUserNameException() {
        super();
    }

    public InvalidUserNameException(String message) {
        super(message);
    }

    public InvalidUserNameException(Throwable cause) {
        super(cause);
    }

    public InvalidUserNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
