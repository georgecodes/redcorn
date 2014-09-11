package com.elevenware.redcorn.container;

import com.elevenware.redcorn.DependentBean;
import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.visitors.MultipleOfSame;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestValueTypeConstructor {

    @Test
    public void addMultiProperties() {

        RedcornContainer container = new ConstructorInjectionRedcornContainer();
        container.register(MultipleOfSame.class)
                .addConstructorArg("hello")
                .addConstructorArg("world");

        container.start();

        MultipleOfSame bean = container.get(MultipleOfSame.class);
        assertNotNull(bean);

        assertEquals("hello", bean.getFirst());
        assertEquals("world", bean.getSecond());

    }

}
