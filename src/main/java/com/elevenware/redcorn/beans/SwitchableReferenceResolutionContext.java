package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.List;

public class SwitchableReferenceResolutionContext implements ReferenceResolutionContext {

    private ReferenceResolutionContext delegate;

    public void setDelegate(ReferenceResolutionContext delegate) {
        this.delegate = delegate;
    }
    @Override
    public Object resolve(String ref) {
        return delegate.resolve(ref);
    }

    @Override
    public boolean canResolve(String ref) {
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
