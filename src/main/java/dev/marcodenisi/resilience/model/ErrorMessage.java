package dev.marcodenisi.resilience.model;

import java.io.Serializable;

public class ErrorMessage implements Serializable {
    private final String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
