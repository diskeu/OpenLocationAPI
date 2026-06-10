package dev.timjelenz.openlocationapi.exceptions.service.user;

public class UserNotFound extends RuntimeException {

    public UserNotFound() { };

    public UserNotFound(final String msg) {
        super(msg);
    }
}
