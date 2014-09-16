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
        if(type != null) {
            payload = coerceToType(payload);
        } else {
            this.type = payload.getClass();
        }
    }

    private Object coerceToType(Object payload) {
        if(!CharSequence.class.isAssignableFrom(payload.getClass())) {
            return payload;
        }
        String value = String.valueOf(payload);
        if(type.equals(int.class)) {
            return Integer.parseInt(value);
        }
        if(type.equals(boolean.class)) {
           return Boolean.parseBoolean(value);
        }
        if(type.equals(short.class)) {
            return Short.parseShort(value);
        }
        if(type.equals(long.class)) {
            return Long.parseLong(value);
        }
        if(type.equals(float.class)) {
            return Float.parseFloat(value);
        }
        if(type.equals(double.class)) {
            return Double.parseDouble(value);
        }
        if(type.equals(char.class)) {
            return value.charAt(0);
        }
        if(type.equals(Integer.class)) {
            return Integer.valueOf(value);
        }
        if(type.equals(Boolean.class)) {
            return Boolean.valueOf(value);
        }
        if(type.equals(Short.class)) {
            return Short.valueOf(value);
        }
        if(type.equals(Long.class)) {
            return Long.valueOf(value);
        }
        if(type.equals(Float.class)) {
            return Float.valueOf(value);
        }
        if(type.equals(Double.class)) {
            return Double.valueOf(value);
        }
        if(type.equals(Character.class)) {
            return Character.valueOf(value.charAt(0));
        }
        return payload;

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
