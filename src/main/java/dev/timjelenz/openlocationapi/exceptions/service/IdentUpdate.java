package dev.timjelenz.openlocationapi.exceptions.service;

public class IdentUpdate extends RuntimeException {

    public IdentUpdate() { }

    public IdentUpdate(final String msg) {
        super(msg);
    }
}
