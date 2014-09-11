package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.TypesMatcher;
import com.elevenware.redcorn.model.InjectableArgument;
import com.elevenware.redcorn.model.InjectableArgumentModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ReferenceArgsInstantiationStrategy implements InstantiationStrategy {

    private final Constructor constructorToUse;
    private final InjectableArgumentModel argumentModel;

    public ReferenceArgsInstantiationStrategy(Constructor constructor, InjectableArgumentModel model) {
        this.constructorToUse = constructor;
        this.argumentModel = model;
    }

    @Override
    public Object instantiate() {
        Object[] inflatedConstructorArgs = argumentModel.getInflatedConstructorArgs();
        try {
            return constructorToUse.newInstance(inflatedConstructorArgs);
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
    public boolean isSatisfied() {
        for(InjectableArgument argument: argumentModel) {
            if(!argument.canResolve()) {
                return false;
            }
        }
        return true;
    }
}
