package com.elevenware.redcorn.container;

import com.elevenware.redcorn.AllPrimitives;
import com.elevenware.redcorn.DependentBean;
import com.elevenware.redcorn.HasInjectableProperty;
import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.DefaultBeanDefinition;
import com.elevenware.redcorn.model.ReferenceResolutionContext;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestSetterInjection {

    @Test
    public void canAddValuePropertiesToDefinitions() {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);

        String message = "This was injected";
        BeanDefinition definition = new DefaultBeanDefinition(SimpleBean.class);
        definition.setResolutionContext(context);
        definition.addProperty("name", message);

        definition.prepare();
        definition.instantiate();

        SimpleBean bean = (SimpleBean) definition.getPayload();

        assertEquals(message, bean.getName());

    }

    @Test
    public void allPrimitivesWork() {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);

        BeanDefinition definition = new DefaultBeanDefinition(AllPrimitives.class)
                .addProperty("theBoolean", true)
                .addProperty("theShort", (short) 9)
                .addProperty("theInt", 39)
                .addProperty("theLong", 66L)
                .addProperty("theDouble", 38.3873d)
                .addProperty("theFloat", 273.39f)
                .addProperty("theChar", 'x')
                .addProperty("theString", "Hello");
        definition.setResolutionContext(context);

        definition.prepare();

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

    @Test
    public void resolvesReferenceProperties() {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);
        when(context.resolve("other.bean")).thenReturn(new SimpleBean());

        BeanDefinition definition = new DefaultBeanDefinition(HasInjectableProperty.class);
        definition.referenceProperty("simpleBean", "other.bean");

        definition.setResolutionContext(context);
        definition.prepare();

        definition.instantiate();

        HasInjectableProperty bean = (HasInjectableProperty) definition.getPayload();

        assertNotNull(bean);
        assertNotNull(bean.getSimpleBean());


    }

}
