package dev.timjelenz.openlocationapi.exceptions.service.location;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException() { }

    public LocationNotFoundException(final String msg) {
        super(msg);
    }
}
