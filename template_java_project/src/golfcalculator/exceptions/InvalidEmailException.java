package golfcalculator.exceptions;

public class InvalidEmailException extends RuntimeException {

    private static final long serialVersionUID = -6273754291009341157L;

    public InvalidEmailException() {
        super();
    }

    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException(Throwable cause) {
        super(cause);
    }

    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
