package com.elevenware.redcorn.container;

import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.lifecycle.Lifecycle;

public interface RedcornContainer extends Lifecycle {

    BeanDefinition register(Class<?> clazz);
    BeanDefinition register(String name, Class<?> type);
    <T> T get(Class<T> clazz);
    <T> T get(String name);
    <T> T find(String name);
    <T> T find(Class<T> clazz);
    void refresh();

}
