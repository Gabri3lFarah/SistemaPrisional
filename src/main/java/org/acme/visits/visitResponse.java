package org.acme.visits;

public class visitResponse {
    private String message;

    public visitResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
