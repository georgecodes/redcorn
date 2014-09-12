package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.DependentBean;
import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.model.ConstructorInjectionModel;
import com.elevenware.redcorn.model.InstantiationStrategy;
import com.elevenware.redcorn.model.ReferenceInjectableArgument;
import com.elevenware.redcorn.model.ReferenceResolutionContext;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestReferenceArgsInstantiationStrategy {

    @Test
    public void canInstantiate() throws NoSuchMethodException {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);
        when(context.resolve("the.bean")).thenReturn(new SimpleBean());

        ConstructorInjectionModel model = new ConstructorInjectionModel();
        model.setContext(context);
        model.addConstructorArg(new ReferenceInjectableArgument("the.bean"));
        Constructor constructor = DependentBean.class.getDeclaredConstructor(new Class[] {SimpleBean.class });
        InstantiationStrategy strategy = new ReferenceArgsInstantiationStrategy(constructor, model);

        DependentBean bean = (DependentBean) strategy.instantiate();

        assertNotNull(bean);
        assertNotNull(bean.getSimpleBean());


    }


}
