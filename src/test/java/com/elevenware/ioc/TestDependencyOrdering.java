package com.elevenware.ioc;

import com.elevenware.ioc.beans.BeanDefinition;
import com.elevenware.ioc.beans.DefaultBeanDefinition;
import com.elevenware.ioc.hierarchy.HelloWorldApplication;
import com.elevenware.ioc.hierarchy.HelloWorldMessageProducerImpl;
import com.elevenware.ioc.hierarchy.MessageFactoryImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestDependencyOrdering {

    @Test
    public void ordering() {

        List<BeanDefinition> beans = new ArrayList<>();

        beans.add(new DefaultBeanDefinition(HelloWorldApplication.class));
        beans.add(new DefaultBeanDefinition(MessageFactoryImpl.class));
        beans.add(new DefaultBeanDefinition(HelloWorldMessageProducerImpl.class));

        DependencyInstantiationOrdering ordering = new DependencyInstantiationOrdering(beans);

        List<BeanDefinition> sortedBeans = ordering.sort();

        assertEquals(3, sortedBeans.size());
        assert(sortedBeans.get(0).getType().equals(HelloWorldMessageProducerImpl.class));
        assert(sortedBeans.get(1).getType().equals(MessageFactoryImpl.class));
        assert(sortedBeans.get(2).getType().equals(HelloWorldApplication.class));


    }




}
