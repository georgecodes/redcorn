package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.DependentBean;
import com.elevenware.redcorn.NamedBean;
import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.model.ReferenceResolutionContext;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestResolvableBeanDefinition {

    @Test
    public void simpleBeanNeedsNoResolving() {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);

        ResolvableBeanDefinition definition = new ResolvableBeanDefinition(SimpleBean.class);
        definition.setResolutionContext(context);

        definition.prepare();

        assertTrue(definition.canInstantiate());
        definition.instantiate();

        SimpleBean bean = (SimpleBean) definition.getPayload();

        assertNotNull(bean);

    }

    @Test
    public void beanWithNoEmptyConstructorNeedsExtraWork() {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);

        ResolvableBeanDefinition definition = new ResolvableBeanDefinition(NamedBean.class);
        definition.setResolutionContext(context);

        definition.prepare();

        assertFalse(definition.canInstantiate());

    }

    @Test
    public void beanWithConcreteConstructorArgsCanInstantiate() {

        ResolvableBeanDefinition definition = new ResolvableBeanDefinition(NamedBean.class);
        definition.addConstructorArg("Hello");

        definition.prepare();

        assertTrue(definition.canInstantiate());

        definition.instantiate();

        NamedBean bean = (NamedBean) definition.getPayload();

        assertNotNull(bean);

        assertEquals("Hello", bean.getName());

    }

    @Test
    public void beanWithNamedReferenceConstructorArgCannotInstantiate() {

        ResolvableBeanDefinition definition = new ResolvableBeanDefinition(DependentBean.class);
        definition.addConstructorRef("other");

        assertFalse(definition.canInstantiate());

    }

    @Test
    public void beanWithNamedReferenceConstructorArgCanInstantiateWhenResolved() {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);
        when(context.canResolve("other")).thenReturn(true);
        when(context.lookupType("other")).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return SimpleBean.class;
            }
        });
        when(context.resolve("other")).thenReturn(new SimpleBean());

        ResolvableBeanDefinition definition = new ResolvableBeanDefinition(DependentBean.class);
        definition.addConstructorRef("other");

        definition.setResolutionContext(context);
        definition.prepare();

        assertTrue(definition.canInstantiate());

        definition.instantiate();

        DependentBean bean = (DependentBean) definition.getPayload();

        assertNotNull(bean);
        assertNotNull(bean.getSimpleBean());

    }

    @Test
    public void beanWithReferenceConstructorArgCannotInstantiate() {

        ResolvableBeanDefinition definition = new ResolvableBeanDefinition(DependentBean.class);
        definition.addConstructorRef(SimpleBean.class);

        assertFalse(definition.canInstantiate());

    }

    @Test
    public void beanWithReferenceConstructorArgCanInstantiateWhenResolved() {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);
        when(context.canResolve(SimpleBean.class.getCanonicalName())).thenReturn(true);
        when(context.lookupType(SimpleBean.class.getCanonicalName())).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return SimpleBean.class;
            }
        });
        when(context.resolve(SimpleBean.class.getCanonicalName())).thenReturn(new SimpleBean());


        ResolvableBeanDefinition definition = new ResolvableBeanDefinition(DependentBean.class);
        definition.addConstructorRef(SimpleBean.class);

        definition.setResolutionContext(context);

        definition.prepare();

        assertTrue(definition.canInstantiate());

        definition.instantiate();

        DependentBean bean = (DependentBean) definition.getPayload();

        assertNotNull(bean);
        assertNotNull(bean.getSimpleBean());

    }

    @Test
    public void beanWithImplicitConstructorArgsInstantiate() {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);
        List<Class<?>> types = new ArrayList<>();
        types.add(SimpleBean.class);
        when(context.getContainedTypes()).thenReturn(types);
        when(context.resolve(SimpleBean.class.getCanonicalName())).thenReturn(new SimpleBean());

        ResolvableBeanDefinition definition = new ResolvableBeanDefinition(DependentBean.class);

        definition.setResolutionContext(context);

        definition.prepare();

        assertTrue(definition.canInstantiate());

    }

}
