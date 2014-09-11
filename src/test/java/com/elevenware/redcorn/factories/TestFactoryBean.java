package com.elevenware.redcorn.factories;

import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.container.ConstructorInjectionRedcornContainer;
import com.elevenware.redcorn.container.RedcornContainer;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestFactoryBean {

    @Test
    public void canAddFactory() {

        RedcornContainer container = new ConstructorInjectionRedcornContainer();

        container.registerFactory("fooService", "createFooService", FooServiceFactory.class);

        container.start();

        FooService fooService = container.get("fooService");

        assertNotNull(fooService);

    }

    @Test
    public void canConfigureFactory() {

        RedcornContainer container = new ConstructorInjectionRedcornContainer();

        String message = "this is a configured message";

        container.register(SimpleBean.class);
        container.registerFactory("fooService", "createFooService", FooServiceFactory.class)
                .addConstructorArg(message);

        container.start();

        FooService service = container.get("fooService");

        assertNotNull(service);

        assertEquals(message, service.getText());

    }

}
