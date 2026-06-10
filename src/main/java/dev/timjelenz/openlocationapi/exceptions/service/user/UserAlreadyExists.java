package dev.timjelenz.openlocationapi.exceptions.service.user;

public class UserAlreadyExists extends RuntimeException {

    public UserAlreadyExists() { }

    public UserAlreadyExists(final String msg) {
        super(msg);
    }
}