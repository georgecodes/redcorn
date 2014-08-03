package com.elevenware.ioc.beans;

import com.elevenware.ioc.visitors.BeanDefinitionVisitor;

import java.util.List;

public interface BeanDefinition {
    void instantiate();
    Object getPayload();
    Class getType();
    boolean canInstantiate();

    void addContructorArg(Object object);

    List<Object> getConstructorArgs();

    void accept(BeanDefinitionVisitor visitor);

    ConstructorModel getConstructorModel();

    void markResolved();
    boolean isResolved();
}
