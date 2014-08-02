package com.elevenware.ioc.container;

import com.elevenware.ioc.SimpleBean;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSimpleContainer {

    @Test( expected = ContainerNotStartedException.class )
    public void cannotGetBeanUntilContainerStarted() {

        IocContainer container = new SimpleIocContainer();
        container.register(SimpleBean.class);
        container.find(SimpleBean.class);

    }

    @Test
    public void canGetBeanOnceContainerStarted() {

        IocContainer container = new SimpleIocContainer();
        container.register(SimpleBean.class);
        container.start();
        SimpleBean bean = container.find(SimpleBean.class);

        assertNotNull(bean);

    }



}
