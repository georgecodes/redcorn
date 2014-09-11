package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.ReferenceResolutionContext;

public interface IResolvableBeanDefinition {
    boolean canInstantiate();

    void instantiate();

    Class getType();

    Object getPayload();

    void setResolutionContext(ReferenceResolutionContext resolutionContext);

    ResolvableBeanDefinition addConstructorArg(Object arg);

    ResolvableBeanDefinition addConstructorRef(String other);

    ResolvableBeanDefinition addConstructorRef(Class<?> clazz);

    void prepare();

    String getName();

    boolean isSatisfied();
}
