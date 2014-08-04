package com.elevenware.ioc.hierarchy;

import com.elevenware.ioc.container.ConstructorInjectionIocContainer;
import com.elevenware.ioc.container.IocContainer;

import static  org.junit.Assert.*;
import org.junit.Test;

public class TestHelloWorldApplication {

    @Test
    public void canSayHelloByBeingConfiguredUsingIoCContainer() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(HelloWorldApplication.class);
        container.register(HelloWorldMessageProducerImpl.class);
        container.register(MessageFactoryImpl.class);

        container.start();

        HelloWorldApplication app = container.get(HelloWorldApplication.class);
        assertNotNull(app);
        assertEquals("Hello, world!", app.getMessage());

    }

    @Test
    public void messageAsConstructorArg() {

        String message = "This is some custom message";
        IocContainer container = new ConstructorInjectionIocContainer();
        container.register("app", HelloWorldApplication.class);
        container.register(MessageFactoryImpl.class);
        container.register(CustomMessageProducer.class).addContructorArg(message);

        container.start();

        HelloWorldApplication app = container.find("app");

        assertEquals(message, app.getMessage());

    }

}
