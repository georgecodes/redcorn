package com.elevenware.ioc.container;

import com.elevenware.ioc.DependentBean;
import com.elevenware.ioc.SimpleBean;
import com.elevenware.ioc.visitors.MultipleOfSame;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestValueTypeConstructor {

    @Test
    public void addProperties() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(SimpleBean.class);
        container.register(DependentBean.class)
                .addContructorArg("hello");

        container.start();

        DependentBean bean = container.get(DependentBean.class);
        assertNotNull(bean);

        assertEquals("hello", bean.getMessage());
        assertNotNull(bean.getSimpleBean());

    }

    @Test
    public void addMultiProperties() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(MultipleOfSame.class)
                .addContructorArg("hello")
                .addContructorArg("world");

        container.start();

        MultipleOfSame bean = container.get(MultipleOfSame.class);
        assertNotNull(bean);

        assertEquals("hello", bean.getFirst());
        assertEquals("world", bean.getSecond());

    }

}
