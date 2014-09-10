package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.ConcreteInjectableArgument;
import com.elevenware.redcorn.model.InjectableArgumentModel;
import com.elevenware.redcorn.model.ReferenceInjectableArgument;
import com.elevenware.redcorn.model.ReferenceResolutionContext;
import com.elevenware.redcorn.visitors.BeanDefinitionVisitor;

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
    private final InjectableArgumentModel injectableArgumentModel;
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
        this.injectableArgumentModel = new InjectableArgumentModel();
    }

    @Override
    public void instantiate() {
        injectableArgumentModel.inflateConstructorArgs();
        constructorModel.assertHasConstructorFor(injectableArgumentModel);
        Constructor constructor = constructorModel.findConstructorFor(injectableArgumentModel);
        try {
            payload = constructor.newInstance(injectableArgumentModel.getInflatedConstructorArgs());
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
        return constructorModel.hasConstructorFor(injectableArgumentModel);
    }

    @Override
    public BeanDefinition addContructorArg(Object object) {
//        this.constructorArgs.add(object);
        injectableArgumentModel.addConstructorArg(new ConcreteInjectableArgument(object));
        return this;
    }

    @Override
    public List<Object> getConstructorArgs() {
        return injectableArgumentModel.getConcreteConstructorArgs();
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
//        this.namedConstructorRefs.add(reference);
        injectableArgumentModel.addConstructorArg(new ReferenceInjectableArgument(reference));
        return this;
    }

    @Override
    public DefaultBeanDefinition addConstructorRef(String reference, Class<?> type) {
        injectableArgumentModel.addConstructorArg(new ReferenceInjectableArgument(reference, type));
        return this;
    }

    @Override
    public void inflateConstructorArgs() {
        for(Object object: injectableArgumentModel.getInflatedConstructorArgs()) {
            constructorArgs.add(object);
        }
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
    public BeanDefinition reference(String other) {
        this.referenceProperties.add(other);
        return this;
    }

    @Override
    public boolean canHydrate() {
        return this.referenceProperties.isEmpty();
    }

    @Override
    public BeanDefinition resolve(String name, Object dependency) {
        addProperty(name, dependency);
        if(!this.referenceProperties.remove(name)) {
            throw new RuntimeException("Tried to resolve unknown property " + name);
        }
        return this;
    }

    @Override
    public void setResolutionContext(ReferenceResolutionContext context) {
        this.resolutionContext = context;
        this.injectableArgumentModel.setContext(context);
    }

    @Override
    public InjectableArgumentModel getInjectionModel() {
        return injectableArgumentModel;
    }



    @Override
    public boolean isResolved() {
        return resolved;
    }

    public String toString() {
        return new StringBuilder("definition for ")
                .append(this.getType()).toString();
    }
}
