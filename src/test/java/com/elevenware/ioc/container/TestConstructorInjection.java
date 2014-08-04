package com.elevenware.ioc.container;

import com.elevenware.ioc.DependentBean;
import com.elevenware.ioc.Simple;
import com.elevenware.ioc.SimpleBean;
import com.elevenware.ioc.StartableBean;
import com.elevenware.ioc.hierarchy.MessageFactory;
import com.elevenware.ioc.hierarchy.MessageFactoryImpl;
import com.elevenware.ioc.hierarchy.MessageProducerImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestConstructorInjection {

    @Test( expected = ContainerNotStartedException.class )
    public void cannotGetBeanUntilContainerStarted() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(SimpleBean.class);
        container.find(SimpleBean.class);

    }

    @Test
    public void canGetBeanOnceContainerStarted() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(SimpleBean.class);
        container.start();
        SimpleBean bean = container.find(SimpleBean.class);

        assertNotNull(bean);

    }

    @Test
    public void autoConstructorInjection() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(SimpleBean.class);
        container.register(DependentBean.class);
        container.start();

        DependentBean bean = container.find(DependentBean.class);
        assertNotNull(bean);
        assertNotNull(bean.getSimpleBean());

    }

    @Test (expected = RuntimeException.class)
    public void throwsExceptionIfNoBeansConfigured() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(DependentBean.class);
        container.start();

    }

    @Test
    public void polymorphism() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(MessageFactoryImpl.class);
        container.register(MessageProducerImpl.class);
        container.start();

        MessageFactory factory = container.find(MessageFactoryImpl.class);
        assertNotNull(factory);
    }

    @Test
    public void lifecycleMaintenance() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(StartableBean.class);
        container.start();

        StartableBean bean = container.find(StartableBean.class);
        assertTrue(bean.isStarted());

    }

    @Test
    public void canFindBySupertype() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(SimpleBean.class);

        container.start();

        Simple bean = container.find(Simple.class);
        assertNotNull(bean);

    }

    @Test
    public void canFindBeanByName() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(SimpleBean.class);

        container.start();

        SimpleBean bean = container.find(SimpleBean.class.getCanonicalName());
        assertNotNull(bean);

    }

    @Test
    public void canRegisterByName() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register("simple", SimpleBean.class);
        container.start();

        SimpleBean bean = container.find("simple");
        assertNotNull(bean);

    }




}
