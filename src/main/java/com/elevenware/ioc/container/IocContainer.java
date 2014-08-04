package com.elevenware.ioc.container;

import com.elevenware.ioc.Lifecycle;
import com.elevenware.ioc.beans.BeanDefinition;

import java.util.List;

public interface IocContainer extends Lifecycle {
    BeanDefinition register(Class clazz);
    <T> T find(Class<T> clazz);

    <T> T find(String canonicalName);

    java.util.Collection<BeanDefinition> getBeanDefinitions();

    void addDefinition(BeanDefinition definition);

    BeanDefinition register(String name, Class clazz);
}
