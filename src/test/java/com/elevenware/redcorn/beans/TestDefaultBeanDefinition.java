package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.DependentBean;
import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.hierarchy.FooHandler;
import com.elevenware.redcorn.hierarchy.Helper;
import com.elevenware.redcorn.hierarchy.Worker;
import com.elevenware.redcorn.model.ReferenceResolutionContext;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestDefaultBeanDefinition {

    @Test
    public void simpleInstantiation() {

        BeanDefinition definition = new DefaultBeanDefinition(SimpleBean.class);
        assertTrue(definition.canInstantiate());
        definition.instantiate();
        SimpleBean bean = (SimpleBean) definition.getPayload();
        assertNotNull(bean);

    }

    @Test
    public void constructorArgsInstantiation() {

        BeanDefinition definition = new DefaultBeanDefinition(DependentBean.class);
        assertFalse(definition.canInstantiate());
        SimpleBean simpleBean = new SimpleBean();
        definition.addContructorArg(simpleBean);

        assertTrue(definition.canInstantiate());
        definition.instantiate();
        DependentBean bean = (DependentBean) definition.getPayload();
        assertNotNull(bean);
        assertEquals(simpleBean, bean.getSimpleBean());

    }

    @Test
    public void definitionsAssumeClassNameAsNameByDefault() {

        BeanDefinition definition = new DefaultBeanDefinition(DependentBean.class);
        assertEquals(DependentBean.class.getCanonicalName(), definition.getName());

    }

    @Test
    public void multipleConstructorRefsAreResolved() {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);
        when(context.resolve("worker")).thenReturn(new Worker());
        when(context.resolve("helper")).thenReturn(new Helper());

        BeanDefinition definition = new DefaultBeanDefinition(FooHandler.class)
                .addConstructorRef("worker")
                .addConstructorRef("helper");

        definition.setResolutionContext(context);

        definition.instantiate();

        assertNotNull(definition.getPayload());

    }

}
