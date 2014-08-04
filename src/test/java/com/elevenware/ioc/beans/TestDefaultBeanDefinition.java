package com.elevenware.ioc.beans;

import com.elevenware.ioc.DependentBean;
import com.elevenware.ioc.SimpleBean;
import com.elevenware.ioc.hierarchy.FooHandler;
import com.elevenware.ioc.hierarchy.Helper;
import com.elevenware.ioc.hierarchy.Worker;
import org.junit.Test;

import static org.junit.Assert.*;

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

        BeanDefinition definition = new DefaultBeanDefinition(FooHandler.class)
                .addConstructorRef("worker")
                .addConstructorRef("helper");

        definition.addContructorArg(new Worker())
                .addContructorArg(new Helper());

        definition.instantiate();

        assertNotNull(definition.getPayload());


    }

}
