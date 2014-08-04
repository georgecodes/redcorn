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

public class TestConstructorInjectionContainer {

    @Test( expected = ContainerNotStartedException.class )
    public void cannotGetBeanUntilContainerStarted() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(SimpleBean.class);
        container.get(SimpleBean.class);

    }

    @Test
    public void canGetBeanOnceContainerStarted() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(SimpleBean.class);
        container.start();
        SimpleBean bean = container.get(SimpleBean.class);

        assertNotNull(bean);

    }

    @Test
    public void autoConstructorInjection() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(SimpleBean.class);
        container.register(DependentBean.class);
        container.start();

        DependentBean bean = container.get(DependentBean.class);
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

        MessageFactory factory = container.get(MessageFactoryImpl.class);
        assertNotNull(factory);
    }

    @Test
    public void lifecycleMaintenance() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(StartableBean.class);
        container.start();

        StartableBean bean = container.get(StartableBean.class);
        assertTrue(bean.isStarted());

    }

    @Test
    public void canFindBySupertype() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(SimpleBean.class);

        container.start();

        Simple bean = container.get(Simple.class);
        assertNotNull(bean);

    }

    @Test
    public void canFindBeanByName() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register(SimpleBean.class);

        container.start();

        SimpleBean bean = container.get(SimpleBean.class.getCanonicalName());
        assertNotNull(bean);

    }

    @Test
    public void canRegisterByName() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register("simple", SimpleBean.class);
        container.start();

        SimpleBean bean = container.get("simple");
        assertNotNull(bean);

    }


    @Test
    public void returnsNullIfCannotGetBeanByName() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register("simple", SimpleBean.class);
        container.start();

        assertNull(container.get("non-existent"));

    }

    @Test( expected = BeanNotFoundException.class)
    public void throwsExceptionIfCannotFindBeanByName() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register("simple", SimpleBean.class);
        container.start();
        container.find("non-existent");

    }

    @Test
    public void returnsNullIfCannotGetBeanByClass() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register("simple", SimpleBean.class);
        container.start();

        assertNull(container.get(Runnable.class));

    }

    @Test( expected = BeanNotFoundException.class)
    public void throwsExceptionIfCannotFindBeanByClass() {

        IocContainer container = new ConstructorInjectionIocContainer();
        container.register("simple", SimpleBean.class);
        container.start();
        container.find(Iterable.class);

    }

}
