package dev.marcodenisi.resilience.model;

import java.io.Serializable;

public class RemoteCallResult implements Serializable {

    private final String id;
    private final String message;

    public RemoteCallResult(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
