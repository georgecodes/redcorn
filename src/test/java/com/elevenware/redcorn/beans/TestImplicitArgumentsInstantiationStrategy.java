package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.DependentBean;
import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.factories.FooService;
import com.elevenware.redcorn.model.ConstructorModel;
import com.elevenware.redcorn.model.InstantiationStrategy;
import com.elevenware.redcorn.model.ReferenceResolutionContext;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestImplicitArgumentsInstantiationStrategy {

    @Test
    public void canInstantiate() throws NoSuchMethodException {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);
        List<Class<?>> types = Arrays.asList(SimpleBean.class, FooService.class);
        when(context.getContainedTypes()).thenReturn(types);
        when(context.resolveType(SimpleBean.class)).thenReturn(new SimpleBean());

        ConstructorModel constructorModel = new ConstructorModel(DependentBean.class);
        Constructor constructor = constructorModel.findBestConstructorsForTypes(types);
        InstantiationStrategy strategy = new ImplicitConstructorArgsInstantiationStrategy(constructor, context);

        DependentBean bean = (DependentBean) strategy.instantiate();

        assertNotNull(bean);
        assertNotNull(bean.getSimpleBean());

    }

}
