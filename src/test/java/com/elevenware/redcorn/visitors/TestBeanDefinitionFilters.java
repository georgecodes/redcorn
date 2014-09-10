package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.DefaultBeanDefinition;
import com.elevenware.redcorn.hierarchy.HelloWorldApplication;
import com.elevenware.redcorn.hierarchy.HelloWorldMessageProducerImpl;
import com.elevenware.redcorn.hierarchy.MessageFactoryImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TestBeanDefinitionFilters {

    @Test
    public void filterBeansWithConcreteConstructorArgs() {

        List<BeanDefinition> beans = new ArrayList<>();

        beans.add(new DefaultBeanDefinition(HelloWorldApplication.class).addContructorArg("foo"));
        beans.add(new DefaultBeanDefinition(MessageFactoryImpl.class).addConstructorRef("wibble"));
        beans.add(new DefaultBeanDefinition(HelloWorldMessageProducerImpl.class));

        BeanFilter filter = new BeanFilter();
        List<BeanDefinition> concreteConstructors = filter.filterByConcreteArgs(beans);

        Set<BeanDefinition> remainder = filter.getRemainder();

        assertEquals(1, concreteConstructors.size());
        assertEquals(HelloWorldApplication.class, concreteConstructors.get(0).getType());

        assertEquals(2, remainder.size());

    }

    @Test
    public void filterBeansWithReferenceConstructorArgs() {

        List<BeanDefinition> beans = new ArrayList<>();

        beans.add(new DefaultBeanDefinition(HelloWorldApplication.class).addContructorArg("foo"));
        beans.add(new DefaultBeanDefinition(MessageFactoryImpl.class).addConstructorRef("wibble"));
        beans.add(new DefaultBeanDefinition(HelloWorldMessageProducerImpl.class));

        BeanFilter filter = new BeanFilter();
        List<BeanDefinition> concreteConstructors = filter.filterByReferenceArgs(beans);

        Set<BeanDefinition> remainder = filter.getRemainder();

        assertEquals(1, concreteConstructors.size());
        assertEquals(MessageFactoryImpl.class, concreteConstructors.get(0).getType());

        assertEquals(2, remainder.size());

    }

    @Test
    public void filterBeansWithImplicitConstructorArgs() {

        List<BeanDefinition> beans = new ArrayList<>();

        beans.add(new DefaultBeanDefinition(HelloWorldApplication.class));
        beans.add(new DefaultBeanDefinition(MessageFactoryImpl.class).addConstructorRef("wibble"));
        beans.add(new DefaultBeanDefinition(HelloWorldMessageProducerImpl.class));

        BeanFilter filter = new BeanFilter();
        List<BeanDefinition> concreteConstructors = filter.filterByImplicitConstructorArgs(beans);

        Set<BeanDefinition> remainder = filter.getRemainder();

        assertEquals(2, concreteConstructors.size());

        assertEquals(1, remainder.size());
        assertEquals(MessageFactoryImpl.class, remainder.iterator().next().getType());

    }

}
