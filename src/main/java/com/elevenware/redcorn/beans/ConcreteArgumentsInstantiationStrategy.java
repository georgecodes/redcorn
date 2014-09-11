package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.ConstructorInjectionModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConcreteArgumentsInstantiationStrategy implements InstantiationStrategy {

    private final Constructor<?> constructorToUse;
//    private final Object[] args;
    private final ConstructorInjectionModel argumentModel;

    public ConcreteArgumentsInstantiationStrategy(Constructor<?> constructor, ConstructorInjectionModel args) {
        this.constructorToUse = constructor;
//        this.args = args.inflateConstructorArgs();
        this.argumentModel = args;
    }

    @Override
    public Object instantiate() {
        Object[] inflatedConstructorArgs = argumentModel.getInflatedArguments();
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
        return true;
    }
}
