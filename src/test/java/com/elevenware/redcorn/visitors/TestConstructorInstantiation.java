package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.DependentBean;
import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.DefaultBeanDefinition;
import com.elevenware.redcorn.beans.DependencyResolutionException;
import com.elevenware.redcorn.container.RedcornContainer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestConstructorInstantiation {

    @Test
    public void instantiateSimpleClass() {

        RedcornContainer container = mock(RedcornContainer.class);
        BeanDefinition bean = new DefaultBeanDefinition(SimpleBean.class);

        Visitors.constructorArgsInstantiator(container).visit(bean);

        assertNotNull(bean.getPayload());

    }

    @Test(expected = DependencyResolutionException.class)
    public void wontInstantiateIfConstructorArgNotPresent() {

        RedcornContainer container = mock(RedcornContainer.class);
        BeanDefinition bean = new DefaultBeanDefinition(DependentBean.class);

        Visitors.constructorArgsInstantiator(container).visit(bean);

    }

    @Test
    public void willInstantiateIfDependencyPresent() {

        RedcornContainer container = mock(RedcornContainer.class);
        BeanDefinition dependency = new DefaultBeanDefinition(SimpleBean.class);
        dependency.instantiate();

        when(container.getBeanDefinitions()).thenReturn(toList(dependency));
        BeanDefinition bean = new DefaultBeanDefinition(DependentBean.class);

        Visitors.constructorArgsInstantiator(container).visit(bean);

        assertNotNull(bean.getPayload());

    }

    @Test
    public void infer() {

    }

    private Collection<BeanDefinition> toList(BeanDefinition dependency) {
        Collection<BeanDefinition> list = new ArrayList<BeanDefinition>();
        list.add(dependency);
        return list;
    }

}
