package com.elevenware.redcorn.constructors;

import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.model.*;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestConstructorInjectionModel {

    @Test
    public void constructorArguments() throws IllegalAccessException, InvocationTargetException, InstantiationException {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);
        when(context.resolve("server.name")).thenReturn("The name");
        when(context.resolve("server")).thenReturn(new SimpleBean());

        ConstructorInjectionModel model = new ConstructorInjectionModel(context);

        model.addConstructorArg(new ConcreteInjectableArgument(203));
        model.addConstructorArg(new ReferenceInjectableArgument("server.name", String.class));
        model.addConstructorArg(new ReferenceInjectableArgument("server", SimpleBean.class));

        TheBean bean = null;

        for(Constructor constructor: TheBean.class.getConstructors()) {
            if(model.matchesConstructorArgumentsInOrder(constructor)) {
                bean = (TheBean) constructor.newInstance(model.getInflatedArguments());
            }
        }

        assertNotNull(bean);
        assertEquals(203, bean.getPort());
        assertEquals("The name", bean.getName());
        assertNotNull(bean.getServer());

    }



}

class TheBean {

    private int port;
    private String name;
    private SimpleBean server;

    public TheBean() {}

    public TheBean(String name) {}

    public TheBean(int port, String name, SimpleBean server) {
        this.port = port;
        this.name = name;
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public SimpleBean getServer() {
        return server;
    }
}