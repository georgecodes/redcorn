package com.elevenware.ioc.hierarchy;

public class HelloWorldMessageProducerImpl implements MessageProducer {

    private String message = "Hello, world!";


    @Override
    public String getMessage() {
        return message;
    }
}
