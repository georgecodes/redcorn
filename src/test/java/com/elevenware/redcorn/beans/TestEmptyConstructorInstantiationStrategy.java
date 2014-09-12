package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.DependentBean;
import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.model.InstantiationStrategy;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestEmptyConstructorInstantiationStrategy {

    @Test
    public void canInstantiate() {

        InstantiationStrategy strategy = new EmptyConstructorInstantiationStrategy(SimpleBean.class);

        SimpleBean bean = (SimpleBean) strategy.instantiate();

        assertNotNull(bean);

    }

    @Test (expected = BeanInstantiationException.class)
    public void failsLazily() {

        InstantiationStrategy strategy = new EmptyConstructorInstantiationStrategy(DependentBean.class);

        strategy.instantiate();

    }

}
