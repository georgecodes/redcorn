package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.DependentBean;
import com.elevenware.redcorn.SimpleBean;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
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

    @Test
    public void includeProvidedConstructorArgs() {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(SimpleBean.class);
        classes.add(List.class);
        ConstructorModel model = new ConstructorModel(DependentBean.class);
        List<Object> args = new ArrayList<Object>();
        args.add("hello");
        Constructor constructors = model.findBestConstructorsForTypes(classes, args);
        assertEquals(2, constructors.getParameterTypes().length);
    }


}
