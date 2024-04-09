package golfcalculator.exceptions;

import com.amazonaws.services.dynamodbv2.xspec.S;

public class EmailAlreadyExistsException extends RuntimeException {


    private static final long serialVersionUID = -2657297886563198359L;

    public EmailAlreadyExistsException() {
        super();
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
