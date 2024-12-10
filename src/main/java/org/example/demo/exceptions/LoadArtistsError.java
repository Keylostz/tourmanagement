package org.example.demo.exceptions;

public class LoadArtistsError extends Exception {
    public LoadArtistsError(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadArtistsError(String message) {
        super(message);
    }

    public LoadArtistsError(Throwable cause) {
        super(cause);
    }
}
