package com.elevenware.redcorn.model;

import java.util.List;

public interface ReferenceResolutionContext {
    Object resolve(String ref);
    boolean canResolve(String ref);
    Class<?> lookupType(String ref);
    List<Class<?>> getContainedTypes();
    Object resolveType(Class<?> clazz);
}
