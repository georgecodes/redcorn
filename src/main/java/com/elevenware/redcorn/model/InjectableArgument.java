package com.elevenware.redcorn.model;

public interface InjectableArgument {
    boolean compatibleWith(Class clazz);

    void inflate();

    Object getPayload();

    Class getType();

    boolean canResolve();
}
