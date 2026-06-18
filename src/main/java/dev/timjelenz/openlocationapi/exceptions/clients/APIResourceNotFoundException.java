package dev.timjelenz.openlocationapi.exceptions.clients;

public class APIResourceNotFoundException extends RuntimeException {

    public APIResourceNotFoundException() { }

    public APIResourceNotFoundException(String msg) {
        super(msg);
    }
}
