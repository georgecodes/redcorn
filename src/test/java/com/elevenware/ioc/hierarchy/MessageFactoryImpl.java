package com.elevenware.ioc.hierarchy;

public class MessageFactoryImpl implements MessageFactory {

    private MessageProducer producer;

    public MessageFactoryImpl(MessageProducer producer) {
        this.producer = producer;
    }

    public MessageProducer getProducer() {
        return this.producer;
    }

}
