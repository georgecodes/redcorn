package com.elevenware.ioc.beans;

import com.elevenware.ioc.DependentBean;
import com.elevenware.ioc.SimpleBean;
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

}
