package com.elevenware.redcorn;

import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.DefaultBeanDefinition;
import com.elevenware.redcorn.container.ConstructorInjectionRedcornContainer;
import com.elevenware.redcorn.container.HasNamedBeanArg;
import com.elevenware.redcorn.hierarchy.HelloWorldApplication;
import com.elevenware.redcorn.hierarchy.HelloWorldMessageProducerImpl;
import com.elevenware.redcorn.hierarchy.MessageFactoryImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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

    @Test
    public void orderingWhenNamedConstructorArgs() {

        List<BeanDefinition> beans = new ArrayList<>();

        BeanDefinition base = new DefaultBeanDefinition(HasNamedBeanArg.class, "hasNamedBean").addConstructorRef("second");
        BeanDefinition first = new DefaultBeanDefinition(NamedBean.class, "first").addContructorArg("first bean");
        BeanDefinition second = new DefaultBeanDefinition(NamedBean.class, "second").addContructorArg("second bean");

        beans.add(first);
        beans.add(base);
        beans.add(second);

        DependencyInstantiationOrdering ordering = new DependencyInstantiationOrdering(beans);

        List<BeanDefinition> sortedBeans = ordering.sort();

        assertEquals(3, sortedBeans.size());
        assertEquals(sortedBeans.get(2), base);

    }

    @Test
    public void weirdBugInGribble() {

        List<BeanDefinition> beans = new ArrayList<>();

        BeanDefinition bazHandler = new DefaultBeanDefinition(BazHandler.class, "bazHandler")
                .addConstructorRef("config")
                .addConstructorRef("resolver")
                .addConstructorRef("appContext");

        BeanDefinition appContext = new DefaultBeanDefinition(ConstructorInjectionRedcornContainer.class, "appContext");

        BeanDefinition bazResolver = new DefaultBeanDefinition(BazResolver.class, "resolver");
        BeanDefinition parser = new DefaultBeanDefinition(ManagedBazParser.class, "config")
                .addContructorArg("baz");

        beans.add(bazHandler);
        beans.add(bazResolver);
        beans.add(parser);
        beans.add(appContext);

        DependencyInstantiationOrdering ordering = new DependencyInstantiationOrdering(beans);

        List<BeanDefinition> sortedBeans = ordering.sort();

        assertEquals(bazHandler, sortedBeans.get(3));

    }



}
