package com.elevenware.redcorn.beans;

import java.util.List;

public class EmptyConstructorInstantiationStrategy implements InstantiationStrategy {
    private final Class<?> typeToInstantiate;

    public EmptyConstructorInstantiationStrategy(Class<?> typeToInstantiate) {
        this.typeToInstantiate = typeToInstantiate;
    }

    @Override
    public Object instantiate() {
        try {
            return typeToInstantiate.newInstance();
        } catch (InstantiationException e) {
           throw  new BeanInstantiationException("Was unable to find an empty constructor for " + typeToInstantiate.getCanonicalName(),e);
        } catch (IllegalAccessException e) {
            throw new BeanInstantiationException("Was unable to find a public empty constructor for " + typeToInstantiate.getCanonicalName(), e);
        }
    }

    @Override
    public boolean isSatisfied() {
        return false;
    }
}
