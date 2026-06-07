package dev.timjelenz.openlocationapi.exceptions.service.user;

public class UserAlreadyExists extends RuntimeException {
    UserAlreadyExists() { }

    public UserAlreadyExists(String msg) {
        super(msg);
    }
}