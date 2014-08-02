package com.elevenware.ioc;

public interface EventListener<T> {

    void doNotify(T event);

}
