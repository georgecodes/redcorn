package com.elevenware.ioc.container;

import com.elevenware.ioc.Lifecycle;

public interface IocContainer extends Lifecycle {
    void register(Class clazz);
    <T> T find(Class<T> clazz);
}
