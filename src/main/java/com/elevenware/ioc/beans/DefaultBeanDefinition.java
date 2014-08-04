package com.elevenware.ioc.beans;

import com.elevenware.ioc.visitors.BeanDefinitionVisitor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DefaultBeanDefinition implements BeanDefinition {

    private final Class type;
    private final List<Object> constructorArgs;
    private Object payload;
    private ConstructorModel constructorModel;
    private boolean resolved;
    private String name;

    public DefaultBeanDefinition(Class concreteClass) {
       this(concreteClass, concreteClass.getCanonicalName());
    }

    public DefaultBeanDefinition(Class clazz, String name) {
        this.type = clazz;
        this.name = name;
        this.constructorModel = new ConstructorModel(clazz);
        this.constructorArgs = new ArrayList<>();
    }

    @Override
    public void instantiate() {
        constructorModel.assertHasConstructorFor(constructorArgs);
        Constructor constructor = constructorModel.findConstructorFor(constructorArgs);
        try {
            payload = constructor.newInstance(constructorArgs.toArray());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public boolean canInstantiate() {
        return constructorModel.hasConstructorFor(constructorArgs);
    }

    @Override
    public BeanDefinition addContructorArg(Object object) {
        this.constructorArgs.add(object);
        return this;
    }

    @Override
    public List<Object> getConstructorArgs() {
        return constructorArgs;
    }

    @Override
    public void accept(BeanDefinitionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ConstructorModel getConstructorModel() {
        return constructorModel;
    }

    @Override
    public void markResolved() {
        this.resolved = true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isResolved() {
        return resolved;
    }

    public String toString() {
        return new StringBuilder(super.toString()).append(" for ")
                .append(this.getType()).toString();
    }
}
