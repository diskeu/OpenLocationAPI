package dev.timjelenz.openlocationapi.exceptions.service.user;

public class InvalidCredentials extends RuntimeException {

    public InvalidCredentials() { }

    public InvalidCredentials(final String msg) {
        super(msg);
    }
}
