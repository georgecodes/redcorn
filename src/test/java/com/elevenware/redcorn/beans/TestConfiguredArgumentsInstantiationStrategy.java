package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.DependentBean;
import com.elevenware.redcorn.NamedBean;
import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.model.ConcreteInjectableArgument;
import com.elevenware.redcorn.model.ConstructorInjectionModel;
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
        ConstructorInjectionModel model = new ConstructorInjectionModel();
        model.addConstructorArg(new ConcreteInjectableArgument(name));
        Constructor<?> constructor = NamedBean.class.getDeclaredConstructor(new Class[] {String.class});
        InstantiationStrategy strategy = new ConcreteArgumentsInstantiationStrategy(constructor, model);

        NamedBean bean = (NamedBean) strategy.instantiate();

        assertNotNull(bean);
        assertEquals(name, bean.getName());

    }

    @Test
    public void canInstantiateWithReferenceArgs() throws NoSuchMethodException {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);
        when(context.resolve("the.bean")).thenReturn(new SimpleBean());

        ConstructorInjectionModel model = new ConstructorInjectionModel();
        model.setContext(context);
        model.addConstructorArg(new ReferenceInjectableArgument("the.bean"));
        Constructor constructor = DependentBean.class.getDeclaredConstructor(new Class[] {SimpleBean.class });
        InstantiationStrategy strategy = new ConcreteArgumentsInstantiationStrategy(constructor, model);

        DependentBean bean = (DependentBean) strategy.instantiate();

        assertNotNull(bean);
        assertNotNull(bean.getSimpleBean());


    }


    @Test (expected = IllegalArgumentException.class)
    public void failsLazily() throws NoSuchMethodException {

        Constructor<?> constructor = NamedBean.class.getDeclaredConstructor(new Class[] {String.class});

        InstantiationStrategy strategy = new ConcreteArgumentsInstantiationStrategy(constructor, new ConstructorInjectionModel());

        strategy.instantiate();

    }

}
