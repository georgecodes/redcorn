package com.elevenware.ioc.beans;

import com.elevenware.ioc.DependentBean;
import com.elevenware.ioc.SimpleBean;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public class TestConstructorModel {

    @Test
    public void findsDefaultConstructor() {

        ConstructorModel model = new ConstructorModel(SimpleBean.class);
        assertTrue(model.hasConstructorFor(emptyList()));
        model.assertHasConstructorFor(emptyList());

    }

    @Test(expected = RuntimeException.class)
    public void doesntFindDefaultConstructor() {

        ConstructorModel model = new ConstructorModel(DependentBean.class);
        assertFalse(model.hasConstructorFor(emptyList()));
        model.assertHasConstructorFor(emptyList());

    }

    @Test
    public void findsConstructorForArgs() {
        ConstructorModel model = new ConstructorModel(DependentBean.class);
        List<Object> args = Arrays.asList((Object) new SimpleBean());
        assertTrue(model.hasConstructorFor(args));
        model.assertHasConstructorFor(args);
    }

    @Test
    public void findAllMatchingConstructors() {

        List<Class<?>> classes = Arrays.asList(SimpleBean.class, Runnable.class, String.class, List.class);
        ConstructorModel model = new ConstructorModel(DependentBean.class);
        List<Constructor> constructors = model.findConstructorsForTypes(classes);
        assertEquals(3, constructors.size());

    }

    @Test
    public void findBestMatchingConstructor() {
        List<Class<?>> classes = Arrays.asList(SimpleBean.class, Runnable.class, String.class, List.class);
        ConstructorModel model = new ConstructorModel(DependentBean.class);
        Constructor constructors = model.findBestConstructorsForTypes(classes);
        assertEquals(3, constructors.getParameterTypes().length);
    }


}
