package com.elevenware.ioc.container;

import com.elevenware.ioc.AllPrimitives;
import com.elevenware.ioc.SimpleBean;
import com.elevenware.ioc.beans.BeanDefinition;
import com.elevenware.ioc.beans.DefaultBeanDefinition;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestSetterInjection {

    @Test
    public void canAddValuePropertiesToDefinitions() {

        String message = "This was injected";
        BeanDefinition definition = new DefaultBeanDefinition(SimpleBean.class);
        definition.addProperty("name", message);

        definition.instantiate();

        SimpleBean bean = (SimpleBean) definition.getPayload();

        assertEquals(message, bean.getName());

    }

    @Test
    public void allPrimitivesWork() {

        BeanDefinition definition = new DefaultBeanDefinition(AllPrimitives.class)
                .addProperty("theBoolean", true)
                .addProperty("theShort", (short) 9)
                .addProperty("theInt", 39)
                .addProperty("theLong", 66L)
                .addProperty("theDouble", 38.3873d)
                .addProperty("theFloat", 273.39f)
                .addProperty("theChar", 'x')
                .addProperty("theString", "Hello");

        definition.instantiate();

        AllPrimitives all = (AllPrimitives) definition.getPayload();
        assertEquals(true, all.isTheBoolean());
        assertEquals((short) 9, all.getTheShort());
        assertEquals(39, all.getTheInt());
        assertEquals(66L, all.getTheLong());
        assertEquals(38.3873d, all.getTheDouble(), 4);
        assertEquals(273.39f, all.getTheFloat(), 2);
        assertEquals('x', all.getTheChar());
        assertEquals("Hello", all.getTheString());
        
    }


}
