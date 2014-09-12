package com.elevenware.redcorn.model;

import java.beans.*;
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

    private class PropertyDefinition {
        public PropertyDefinition(String name, Method writeMethod) {

        }
    }
}
