package com.elevenware.redcorn.hierarchy;

import com.elevenware.redcorn.lifecycle.Lifecycle;

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

    @Override
    public void stop() {

    }
}
