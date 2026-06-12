package dev.timjelenz.openlocationapi.exceptions.service.user;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() { }

    public UserAlreadyExistsException(final String msg) {
        super(msg);
    }
}