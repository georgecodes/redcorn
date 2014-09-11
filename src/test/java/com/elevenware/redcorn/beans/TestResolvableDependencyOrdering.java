package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.*;
import com.elevenware.redcorn.container.ConstructorInjectionRedcornContainer;
import com.elevenware.redcorn.container.HasNamedBeanArg;
import com.elevenware.redcorn.hierarchy.HelloWorldApplication;
import com.elevenware.redcorn.hierarchy.HelloWorldMessageProducerImpl;
import com.elevenware.redcorn.hierarchy.MessageFactoryImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestResolvableDependencyOrdering {

    @Test
    public void ordering() {

        List<ResolvableBeanDefinition> beans = new ArrayList<>();

        beans.add(new ResolvableBeanDefinition(HelloWorldApplication.class));
        beans.add(new ResolvableBeanDefinition(MessageFactoryImpl.class));
        beans.add(new ResolvableBeanDefinition(HelloWorldMessageProducerImpl.class));

        ResolvableDependencyInstantiationOrdering ordering = new ResolvableDependencyInstantiationOrdering(beans);

        List<ResolvableBeanDefinition> sortedBeans = ordering.sort();

        assertEquals(3, sortedBeans.size());
        assert(sortedBeans.get(0).getType().equals(HelloWorldMessageProducerImpl.class));
        assert(sortedBeans.get(1).getType().equals(MessageFactoryImpl.class));
        assert(sortedBeans.get(2).getType().equals(HelloWorldApplication.class));

    }

    @Test
    public void orderingWhenNamedConstructorArgs() {

        List<ResolvableBeanDefinition> beans = new ArrayList<>();

        ResolvableBeanDefinition base = new ResolvableBeanDefinition(HasNamedBeanArg.class, "hasNamedBean")
                .addConstructorRef("second");
        ResolvableBeanDefinition first = new ResolvableBeanDefinition(NamedBean.class, "first")
                .addConstructorArg("first bean");
        ResolvableBeanDefinition second = new ResolvableBeanDefinition(NamedBean.class, "second")
                .addConstructorArg("second bean");

        beans.add(first);
        beans.add(base);
        beans.add(second);

        ResolvableDependencyInstantiationOrdering ordering = new ResolvableDependencyInstantiationOrdering(beans);

        List<ResolvableBeanDefinition> sortedBeans = ordering.sort();

        assertEquals(3, sortedBeans.size());
        assertEquals(sortedBeans.get(2), base);

    }

    @Test
    public void weirdBugInGribble() {

        List<ResolvableBeanDefinition> beans = new ArrayList<>();

        ResolvableBeanDefinition bazHandler = new ResolvableBeanDefinition(BazHandler.class, "bazHandler")
                .addConstructorRef("config")
                .addConstructorRef("resolver")
                .addConstructorRef("appContext");

        ResolvableBeanDefinition appContext = new ResolvableBeanDefinition(ConstructorInjectionRedcornContainer.class, "appContext");

        ResolvableBeanDefinition bazResolver = new ResolvableBeanDefinition(BazResolver.class, "resolver");
        ResolvableBeanDefinition parser = new ResolvableBeanDefinition(ManagedBazParser.class, "config")
                .addConstructorArg("baz");

        beans.add(bazHandler);
        beans.add(bazResolver);
        beans.add(parser);
        beans.add(appContext);

        ResolvableDependencyInstantiationOrdering ordering = new ResolvableDependencyInstantiationOrdering(beans);

        List<ResolvableBeanDefinition> sortedBeans = ordering.sort();

        assertEquals(bazHandler, sortedBeans.get(3));

    }

}