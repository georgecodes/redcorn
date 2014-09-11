package com.elevenware.redcorn.container;

import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.ExtendedBeanDefinition;
import com.elevenware.redcorn.lifecycle.Lifecycle;

public interface RedcornContainer extends Lifecycle {
    BeanDefinition register(Class clazz);
    <T> T get(Class<T> clazz);
    <T> T get(String canonicalName);
    java.util.Collection<BeanDefinition> getBeanDefinitions();
    void addDefinition(BeanDefinition definition);
    BeanDefinition register(String name, Class clazz);
    <T> T find(String name);
    <T> T find(Class clazz);

    RedcornContainer createChild(String name);

    BeanDefinition registerFactory(String name, String factoryMethod, Class factoryClass);

}
