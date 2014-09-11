package com.elevenware.redcorn.container;

import com.elevenware.redcorn.beans.ResolvableBeanDefinition;
import com.elevenware.redcorn.beans.ResolvableDependencyInstantiationOrdering;
import com.elevenware.redcorn.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResolvableContainer {

    private Map<String, ResolvableBeanDefinition> definitions;
    private Map<String, ResolvableBeanDefinition> context;
    private Map<Class, ResolvableBeanDefinition> classContext;

    public ResolvableContainer() {
        definitions = new HashMap<>();
        context = new HashMap<>();
        classContext = new HashMap<>();
    }

    private boolean started = false;

    public ResolvableBeanDefinition register(Class<?> clazz) {
        ResolvableBeanDefinition definition = new ResolvableBeanDefinition(clazz);
        definitions.put(definition.getName(), definition);
        return definition;
    }

    public <T> T get(Class<?> clazz) {
        assertStarted();
        for(Map.Entry<Class, ResolvableBeanDefinition> entry: classContext.entrySet()) {
            if(clazz.isAssignableFrom(entry.getKey())) {
                return (T) entry.getValue().getPayload();
            }
        }
        return null;
    }

    public void start() {
        List<ResolvableBeanDefinition> beans = new ArrayList<>(definitions.values());

        ResolvableDependencyInstantiationOrdering ordering = new ResolvableDependencyInstantiationOrdering(beans);
        List<ResolvableBeanDefinition> sorted = ordering.sort();
        for(ResolvableBeanDefinition bean: sorted) {
            bean.prepare();
            bean.instantiate();
            if(Lifecycle.class.isAssignableFrom(bean.getType())) {
                Lifecycle lifecycle = (Lifecycle) bean.getPayload();
                lifecycle.start();
            }
            context.put(bean.getName(), bean);
            classContext.put(bean.getType(), bean);
        }
        this.started = true;
    }

    public <T> T get(String name) {
        assertStarted();
        ResolvableBeanDefinition definition = context.get(name);
        if(definition != null) {
            return (T) definition.getPayload();
        }
        return null;
    }

    public ResolvableBeanDefinition register(String name, Class<?> type) {
        ResolvableBeanDefinition definition = new ResolvableBeanDefinition(type, name);
        definitions.put(definition.getName(), definition);
        return definition;
    }

    public <T> T find(String name) {
        T found = get(name);
        if(found == null) {
            throw new BeanNotFoundException(name);
        }
        return found;
    }

    public <T> T find(Class<?> clazz) {
        T object = get(clazz);
        if(object == null) {
            throw new BeanNotFoundException(clazz.getCanonicalName());
        }
        return object;
    }

    private void assertStarted() {
        if(!started) {
            throw new ContainerNotStartedException();
        }
    }

    public void refresh() {
        start();
    }
}
