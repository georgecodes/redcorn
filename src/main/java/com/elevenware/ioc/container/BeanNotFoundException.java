package com.elevenware.ioc.container;

public class BeanNotFoundException extends RuntimeException {
    public BeanNotFoundException(String name) {
        super("Could not find bean named '" + name + "'");
    }
}
