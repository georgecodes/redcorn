package com.elevenware.ioc.container;

import com.elevenware.ioc.Lifecycle;
import com.elevenware.ioc.beans.BeanDefinition;

import java.util.List;

public interface IocContainer extends Lifecycle {
    BeanDefinition register(Class clazz);
    <T> T get(Class<T> clazz);
    <T> T get(String canonicalName);
    java.util.Collection<BeanDefinition> getBeanDefinitions();
    void addDefinition(BeanDefinition definition);
    BeanDefinition register(String name, Class clazz);
    <T> T find(String name);
    <T> T find(Class clazz);
}
