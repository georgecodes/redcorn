package com.elevenware.ioc.hierarchy;

public class CustomMessageProducer implements MessageProducer {
    private String message;

    public CustomMessageProducer(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
