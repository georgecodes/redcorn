package com.elevenware.redcorn.hierarchy;

public class HelloWorldMessageProducerImpl implements MessageProducer {

    private String message = "Hello, world!";


    @Override
    public String getMessage() {
        return message;
    }
}
