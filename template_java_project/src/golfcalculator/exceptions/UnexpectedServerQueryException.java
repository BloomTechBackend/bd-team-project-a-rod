package golfcalculator.exceptions;

public class UnexpectedServerQueryException extends RuntimeException {

    private static final long serialVersionUID = 8316028698631927603L;

    public UnexpectedServerQueryException() {
        super();
    }

    public UnexpectedServerQueryException(String message) {
        super(message);
    }

    public UnexpectedServerQueryException(Throwable cause) {
        super(cause);
    }

    public UnexpectedServerQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
