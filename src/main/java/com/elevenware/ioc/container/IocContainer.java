package com.elevenware.ioc.container;

import com.elevenware.ioc.Lifecycle;
import com.elevenware.ioc.beans.BeanDefinition;

public interface IocContainer extends Lifecycle {
    BeanDefinition register(Class clazz);
    <T> T find(Class<T> clazz);
}
