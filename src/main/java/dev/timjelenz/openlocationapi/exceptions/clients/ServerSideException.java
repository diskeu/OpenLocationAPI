package dev.timjelenz.openlocationapi.exceptions.clients;

public class ServerSideException extends RuntimeException {

    public ServerSideException() { }

    public ServerSideException(String msg) {
        super(msg);
    }
}
