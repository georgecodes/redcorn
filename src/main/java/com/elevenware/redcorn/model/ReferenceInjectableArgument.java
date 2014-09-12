package com.elevenware.redcorn.model;

import com.elevenware.redcorn.constructors.UnresolvableReferenceException;

public class ReferenceInjectableArgument implements InjectableArgument, ReferenceResolver {
    private Class<?> type;
    private final String ref;
    private ReferenceResolutionContext context;
    private Object payload;

    public ReferenceInjectableArgument(String ref, Class<?> type) {
        this(ref);
        this.type = type;
    }

    public ReferenceInjectableArgument(String reference) {
        this.ref = reference;
    }

    @Override
    public void setResolutionContext(ReferenceResolutionContext context) {
        this.context = context;
    }

    @Override
    public boolean compatibleWith(Class clazz) {
        return this.type.isAssignableFrom(clazz);
    }

    @Override
    public void inflate() {
        this.payload = context.resolve(ref);
        if(payload == null) {
            throw new UnresolvableReferenceException(ref);
        }
        this.type = payload.getClass();
    }

    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public Class getType() {
        if(type == null) {
            type = context.lookupType(ref);
        }
        return type;
    }

    @Override
    public boolean canResolve() {
        if(context == null) {
            return false;
        }
        return context.canResolve(ref);
    }
}
