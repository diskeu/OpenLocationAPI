package dev.timjelenz.openlocationapi.exceptions.service.user;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() { }

    public InvalidCredentialsException(final String msg) {
        super(msg);
    }
}