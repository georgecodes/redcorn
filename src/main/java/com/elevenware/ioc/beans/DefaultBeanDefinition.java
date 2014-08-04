package com.elevenware.ioc.beans;

import com.elevenware.ioc.visitors.BeanDefinitionVisitor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBeanDefinition implements BeanDefinition {

    private final Class type;
    private final List<Object> constructorArgs;
    private final List<String> namedConstructorRefs;
    private Object payload;
    private ConstructorModel constructorModel;
    private boolean resolved;
    private String name;
    private Map<String, Object> properties;

    public DefaultBeanDefinition(Class concreteClass) {
       this(concreteClass, concreteClass.getCanonicalName());
    }

    public DefaultBeanDefinition(Class clazz, String name) {
        this.type = clazz;
        this.name = name;
        this.constructorModel = new ConstructorModel(clazz);
        this.constructorArgs = new ArrayList<>();
        this.namedConstructorRefs = new ArrayList<>();
        this.properties = new HashMap<>();
    }

    @Override
    public void instantiate() {
        constructorModel.assertHasConstructorFor(constructorArgs);
        Constructor constructor = constructorModel.findConstructorFor(constructorArgs);
        try {
            payload = constructor.newInstance(constructorArgs.toArray());
            hydratePayload();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private void hydratePayload() {
        try {
            BeanInfo info = Introspector.getBeanInfo(payload.getClass());
            for(Map.Entry<String, Object> entry: this.properties.entrySet()) {
                boolean found = false;
                for(PropertyDescriptor descriptor: info.getPropertyDescriptors()) {
                   if(descriptor.getName().equals(entry.getKey())) {
                       setProperty(descriptor, entry.getValue());
                       found = true;
                   }
               }
               if(!found) {
                   throw new RuntimeException("You tried to set a property called " + entry.getKey() + ", but it doesn't exist");
               }
            }
         } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    private void setProperty(PropertyDescriptor descriptor, Object value) {

        if(!descriptor.getPropertyType().isAssignableFrom(value.getClass())) {
            if(!descriptor.getPropertyType().isPrimitive()) {
                throw new RuntimeException("You tried to set a property called " + descriptor.getName() + ", but it was the wrong type");
            }
        }
        try {
            descriptor.getWriteMethod().invoke(this.payload, value);
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
    public BeanDefinition addConstructorRef(String reference) {
        this.namedConstructorRefs.add(reference);
        return this;
    }

    @Override
    public List<String> getConstructorRefs() {
        return namedConstructorRefs;
    }

    @Override
    public BeanDefinition addProperty(String name, Object value) {
        this.properties.put(name, value);
        return this;
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
