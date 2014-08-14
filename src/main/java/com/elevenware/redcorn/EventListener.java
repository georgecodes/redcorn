package com.elevenware.redcorn;

public interface EventListener<T> {

    void doNotify(T event);

}
