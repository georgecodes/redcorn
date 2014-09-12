package com.elevenware.redcorn.model;

import com.elevenware.redcorn.beans.BeanInstantiationException;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PropertyModel {
    private final Class<?> type;
    private final Map<String, PropertyDefinition> properties;

    public PropertyModel(Class<?> type) {
        this.type = type;
        properties = new HashMap<>();
        initialiseFor(type);
    }

    private void initialiseFor(Class<?> type) {
        try {
            BeanInfo info = Introspector.getBeanInfo(type);
            for(PropertyDescriptor descriptor: info.getPropertyDescriptors()) {
                if(descriptor.getWriteMethod() != null) {
                    addProperty(descriptor);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    private void addProperty(PropertyDescriptor descriptor) {
        PropertyDefinition definition = new PropertyDefinition(
                descriptor.getName(),
                descriptor.getWriteMethod()
        );
        properties.put(descriptor.getName(), definition);
    }

    public boolean hasProperty(String name) {
        return properties.containsKey(name);
    }

    public PropertyDefinition getProperty(String name) {
        return properties.get(name);
    }

    public class PropertyDefinition {
        private final String name;
        private final Method writeMethod;


        public PropertyDefinition(String name, Method writeMethod) {
            this.name = name;
            this.writeMethod = writeMethod;
        }

        public void setProperty(Object payload, Object value) {
            try {
                writeMethod.invoke(payload, value);
            } catch (IllegalAccessException e) {
                throw new BeanInstantiationException("Cannot set property " + name + " on " + payload, e);
            } catch (InvocationTargetException e) {
                throw new BeanInstantiationException("Cannot set property " + name + " on " + payload, e);
            }

        }
    }
}
