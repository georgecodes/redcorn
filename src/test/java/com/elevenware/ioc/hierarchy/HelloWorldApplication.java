package com.elevenware.ioc.hierarchy;

import com.elevenware.ioc.Lifecycle;

public class HelloWorldApplication implements Lifecycle {

    private final MessageFactory messageFactory;

    public HelloWorldApplication(MessageFactory factory) {
        this.messageFactory = factory;
    }

    public String getMessage() {
        return messageFactory.getProducer().getMessage();
    }

    @Override
    public void start() {

    }
}
