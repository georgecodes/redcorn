package com.elevenware.redcorn.container;

import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.DefaultBeanDefinition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FactoryBeanDefinition extends DefaultBeanDefinition {
    private final String factoryMethod;
    private final Class factoryClass;

    public FactoryBeanDefinition(String name, String factoryMethod, Class factoryClass) {
        super(factoryClass, name);
        this.factoryMethod = factoryMethod;
        this.factoryClass = factoryClass;
    }

    @Override
    public Object getPayload() {
        Object factory = super.getPayload();
        try {
            Method method = factoryClass.getMethod(factoryMethod);
            Object payload = method.invoke(factory);
            return payload;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

}
