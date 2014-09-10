package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.DependentBean;
import com.elevenware.redcorn.NamedBean;
import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.model.ConcreteInjectableArgument;
import com.elevenware.redcorn.model.InjectableArgumentModel;
import com.elevenware.redcorn.model.ReferenceInjectableArgument;
import com.elevenware.redcorn.model.ReferenceResolutionContext;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestConfiguredArgumentsInstantiationStrategy {

    @Test
    public void canInstantiateWithConcreteArgs() throws NoSuchMethodException {

        String name = "the name";
        InjectableArgumentModel model = new InjectableArgumentModel();
        model.addConstructorArg(new ConcreteInjectableArgument(name));
        Constructor<?> constructor = NamedBean.class.getDeclaredConstructor(new Class[] {String.class});
        InstantiationStrategy strategy = new ConfiguredArgumentsInstantiationStrategy(constructor, model);

        NamedBean bean = (NamedBean) strategy.instantiate();

        assertNotNull(bean);
        assertEquals(name, bean.getName());

    }

    @Test
    public void canInstantiateWithReferenceArgs() throws NoSuchMethodException {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);
        when(context.resolve("the.bean")).thenReturn(new SimpleBean());

        InjectableArgumentModel model = new InjectableArgumentModel();
        model.setContext(context);
        model.addConstructorArg(new ReferenceInjectableArgument("the.bean"));
        Constructor constructor = DependentBean.class.getDeclaredConstructor(new Class[] {SimpleBean.class });
        InstantiationStrategy strategy = new ConfiguredArgumentsInstantiationStrategy(constructor, model);

        DependentBean bean = (DependentBean) strategy.instantiate();

        assertNotNull(bean);
        assertNotNull(bean.getSimpleBean());


    }


    @Test (expected = IllegalArgumentException.class)
    public void failsLazily() throws NoSuchMethodException {

        Constructor<?> constructor = NamedBean.class.getDeclaredConstructor(new Class[] {String.class});

        InstantiationStrategy strategy = new ConfiguredArgumentsInstantiationStrategy(constructor, new InjectableArgumentModel());

        strategy.instantiate();

    }

}
