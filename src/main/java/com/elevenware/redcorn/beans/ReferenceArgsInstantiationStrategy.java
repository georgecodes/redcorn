package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.InjectableArgument;
import com.elevenware.redcorn.model.ConstructorInjectionModel;
import com.elevenware.redcorn.model.InstantiationStrategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReferenceArgsInstantiationStrategy implements InstantiationStrategy {

    private final Constructor constructorToUse;
    private final ConstructorInjectionModel argumentModel;

    public ReferenceArgsInstantiationStrategy(Constructor constructor, ConstructorInjectionModel model) {
        this.constructorToUse = constructor;
        this.argumentModel = model;
    }

    @Override
    public Object instantiate() {
        Object[] inflatedConstructorArgs = argumentModel.getInflatedArguments();
        try {
            return constructorToUse.newInstance(inflatedConstructorArgs);
        } catch (InstantiationException e) {
           throw new BeanInstantiationException("Failed to instantiate " + constructorToUse.getDeclaringClass(), e);
        } catch (IllegalAccessException e) {
            throw new BeanInstantiationException("Failed to instantiate " + constructorToUse.getDeclaringClass(), e);
        } catch (InvocationTargetException e) {
            throw new BeanInstantiationException("Failed to instantiate " + constructorToUse.getDeclaringClass(), e);
        }
    }

    @Override
    public boolean isSatisfied() {
        for(InjectableArgument argument: argumentModel) {
            if(!argument.canResolve()) {
                return false;
            }
        }
        return true;
    }
}
