package org.acme.visits;

public class visitResponse {
    private String message;

    public visitResponse() {}

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
// visitResponse moved/renamed to VisitResponse.java
// Kept as placeholder to avoid breaking IDE references while refactoring.
