package com.elevenware.redcorn.container;

import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.List;
import java.util.Properties;

public class OverridableReferenceResolutionContext implements ReferenceResolutionContext {

    private final ReferenceResolutionContext delegate;
    private final Properties properties;

    public OverridableReferenceResolutionContext(Properties properties, ReferenceResolutionContext delegate) {
        this.delegate = delegate;
        this.properties = properties;
    }

    @Override
    public Object resolve(String ref) {
        Object object = System.getProperties().get(ref);
        if(object == null) {
            object = properties.get(ref);
        }
        if(object == null) {
            object = delegate.resolve(ref);
        }
        return object;
    }

    @Override
    public boolean canResolve(String ref) {
        if(System.getProperties().containsKey(ref)) {
            return true;
        }
        if(properties.containsKey(ref)) {
            return true;
        }
        return delegate.canResolve(ref);
    }

    @Override
    public Class<?> lookupType(String ref) {
        return delegate.lookupType(ref);
    }

    @Override
    public List<Class<?>> getContainedTypes() {
        return delegate.getContainedTypes();
    }

    @Override
    public Object resolveType(Class<?> clazz) {
        return delegate.resolveType(clazz);
    }
}
