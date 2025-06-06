package com.practiceproject.linkchat_back.model;

public class Message {
    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String setMessage(String message) {
        return this.message = message;
    }
}
