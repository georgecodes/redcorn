package com.elevenware.ioc.container;

import com.elevenware.ioc.*;
import com.elevenware.ioc.hierarchy.MessageFactory;
import com.elevenware.ioc.hierarchy.MessageFactoryImpl;
import com.elevenware.ioc.hierarchy.MessageProducerImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestConstructorInjectionContainer {

    private IocContainer container;

    @Before
    public void setup() {
        this.container = new ConstructorInjectionIocContainer();
    }

    @Test( expected = ContainerNotStartedException.class )
    public void cannotGetBeanUntilContainerStarted() {

        container.register(SimpleBean.class);
        container.get(SimpleBean.class);

    }

    @Test
    public void canGetBeanOnceContainerStarted() {

        container.register(SimpleBean.class);
        container.start();
        SimpleBean bean = container.get(SimpleBean.class);

        assertNotNull(bean);

    }

    @Test
    public void autoConstructorInjection() {

        container.register(SimpleBean.class);
        container.register(DependentBean.class);
        container.start();

        DependentBean bean = container.get(DependentBean.class);
        assertNotNull(bean);
        assertNotNull(bean.getSimpleBean());

    }

    @Test (expected = RuntimeException.class)
    public void throwsExceptionIfNoBeansConfigured() {

        container.register(DependentBean.class);
        container.start();

    }

    @Test
    public void polymorphism() {

        container.register(MessageFactoryImpl.class);
        container.register(MessageProducerImpl.class);
        container.start();

        MessageFactory factory = container.get(MessageFactoryImpl.class);
        assertNotNull(factory);
    }

    @Test
    public void lifecycleMaintenance() {

        container.register(StartableBean.class);
        container.start();

        StartableBean bean = container.get(StartableBean.class);
        assertTrue(bean.isStarted());

    }

    @Test
    public void canFindBySupertype() {

        container.register(SimpleBean.class);

        container.start();

        Simple bean = container.get(Simple.class);
        assertNotNull(bean);

    }

    @Test
    public void canFindBeanByName() {

        container.register(SimpleBean.class);

        container.start();

        SimpleBean bean = container.get(SimpleBean.class.getCanonicalName());
        assertNotNull(bean);

    }

    @Test
    public void canRegisterByName() {

        container.register("simple", SimpleBean.class);
        container.start();

        SimpleBean bean = container.get("simple");
        assertNotNull(bean);

    }


    @Test
    public void returnsNullIfCannotGetBeanByName() {

        container.register("simple", SimpleBean.class);
        container.start();

        assertNull(container.get("non-existent"));

    }

    @Test( expected = BeanNotFoundException.class)
    public void throwsExceptionIfCannotFindBeanByName() {

        container.register("simple", SimpleBean.class);
        container.start();
        container.find("non-existent");

    }

    @Test
    public void returnsNullIfCannotGetBeanByClass() {

        container.register("simple", SimpleBean.class);
        container.start();

        assertNull(container.get(Runnable.class));

    }

    @Test( expected = BeanNotFoundException.class)
    public void throwsExceptionIfCannotFindBeanByClass() {

        container.register("simple", SimpleBean.class);
        container.start();
        container.find(Iterable.class);

    }

    @Test
    public void canRegisterMoreThanOneBeanOfType() {

        container.register("first", NamedBean.class).addContructorArg("first bean");
        container.register("second", NamedBean.class).addContructorArg("second bean");

        container.start();

        NamedBean first = container.find("first");
        NamedBean second = container.find("second");

        assertEquals("first bean", first.getName());
        assertEquals("second bean", second.getName());

    }

    @Test
    public void registeringByClassAgainOverwritesFirstBean() {

        container.register(NamedBean.class).addContructorArg("first bean");
        container.register(NamedBean.class).addContructorArg("second bean");

        container.start();

        NamedBean bean = container.find(NamedBean.class);

        assertEquals("second bean", bean.getName());

    }

    @Test( expected = RuntimeException.class)
    public void throwsExceptionIfAmbiguousConstructorArgs() {

        container.register("first", NamedBean.class).addContructorArg("first bean");
        container.register("second", NamedBean.class).addContructorArg("second bean");
        container.register(HasNamedBeanArg.class);

        container.start();

    }

    @Test
    public void canGiveNamedConstructorArgs() {

        container.register("first", NamedBean.class).addContructorArg("first bean");
        container.register("second", NamedBean.class).addContructorArg("second bean");
        container.register(HasNamedBeanArg.class).addConstructorRef("second");

        container.start();

        HasNamedBeanArg bean = container.get(HasNamedBeanArg.class);
        assertNotNull(bean);

        assertEquals("second bean", bean.getBean().getName());

    }


}