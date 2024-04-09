package golfcalculator.exceptions;

public class MinimumGamesNotPlayedException extends RuntimeException {

    private static final long serialVersionUID = -6986199179034964849L;

    public MinimumGamesNotPlayedException() {
        super();
    }

    public MinimumGamesNotPlayedException(String message) {
        super(message);
    }

    public MinimumGamesNotPlayedException(Throwable cause) {
        super(cause);
    }

    public MinimumGamesNotPlayedException(String message, Throwable cause) {
        super(message, cause);
    }
}
