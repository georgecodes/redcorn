package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.ConcreteInjectableArgument;
import com.elevenware.redcorn.model.ConstructorInjectionModel;
import com.elevenware.redcorn.model.ReferenceInjectableArgument;
import com.elevenware.redcorn.model.ReferenceResolutionContext;

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
    private final ConstructorInjectionModel constructorInjectionModel;
    private Object payload;
    private ConstructorModel constructorModel;
    private boolean resolved;
    private String name;
    private Map<String, Object> properties;
    private List<String> referenceProperties;
    private ReferenceResolutionContext resolutionContext;

    public DefaultBeanDefinition(Class concreteClass) {
       this(concreteClass, concreteClass.getCanonicalName());
    }

    public DefaultBeanDefinition(Class clazz, String name) {
        this.type = clazz;
        this.name = name;
        this.constructorModel = new ConstructorModel(clazz);
        this.constructorArgs = new ArrayList<>();
        this.namedConstructorRefs = new ArrayList<>();
        this.referenceProperties = new ArrayList<>();
        this.properties = new HashMap<>();
        this.constructorInjectionModel = new ConstructorInjectionModel();
    }

    @Override
    public void instantiate() {
        constructorInjectionModel.inflateConstructorArgs();
        constructorModel.assertHasConstructorFor(constructorInjectionModel);
        Constructor constructor = constructorModel.findConstructorFor(constructorInjectionModel);
        try {
            payload = constructor.newInstance(constructorInjectionModel.getInflatedArguments());
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
        return constructorModel.hasConstructorFor(constructorInjectionModel);
    }

    @Override
    public BeanDefinition addConstructorArg(Object object) {
//        this.constructorArgs.add(object);
        constructorInjectionModel.addConstructorArg(new ConcreteInjectableArgument(object));
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isSatisfied() {
        return false;
    }

    @Override
    public BeanDefinition addConstructorRef(String reference) {
//        this.namedConstructorRefs.add(reference);
        constructorInjectionModel.addConstructorArg(new ReferenceInjectableArgument(reference));
        return this;
    }

    @Override
    public BeanDefinition addConstructorRef(String reference, Class<?> type) {
        constructorInjectionModel.addConstructorArg(new ReferenceInjectableArgument(reference, type));
        return this;
    }

    @Override
    public BeanDefinition addConstructorRef(Class<?> clazz) {
        return null;
    }

    @Override
    public void prepare() {

    }

    @Override
    public void setResolutionContext(ReferenceResolutionContext context) {
        this.resolutionContext = context;
        this.constructorInjectionModel.setContext(context);
    }

    public String toString() {
        return new StringBuilder("definition for ")
                .append(this.getType()).toString();
    }
}
