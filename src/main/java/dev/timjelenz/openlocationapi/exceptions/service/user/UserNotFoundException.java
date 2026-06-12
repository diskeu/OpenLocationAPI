package dev.timjelenz.openlocationapi.exceptions.service.user;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() { };

    public UserNotFoundException(final String msg) {
        super(msg);
    }
}
