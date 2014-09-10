package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.TypesMatcher;
import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ImplicitConstructorArgsInstantiationStrategy implements InstantiationStrategy {
    private final Constructor constructor;
    private final ReferenceResolutionContext resolutionContext;

    public ImplicitConstructorArgsInstantiationStrategy(Constructor constructor, ReferenceResolutionContext context) {
        this.constructor = constructor;
        this.resolutionContext = context;
    }

    @Override
    public Object instantiate() {
        List<Object> args = new ArrayList<>();
        for(Class clazz: constructor.getParameterTypes()) {
            args.add(resolutionContext.resolveType(clazz));
        }
        try {
            return constructor.newInstance(args.toArray());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSatisfiedBy(List<Class<?>> availableTypes) {
        for(Class constructorType: constructor.getParameterTypes()) {
            boolean satisfiable = false;
            for(Class availableType: availableTypes) {
                if(constructorType.isAssignableFrom(availableType)) {
                    satisfiable = true;
                }
            }
            if(!satisfiable) {
                return false;
            }
        }
        return true;
    }
}
