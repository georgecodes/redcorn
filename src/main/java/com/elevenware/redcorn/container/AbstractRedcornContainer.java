package com.elevenware.redcorn.container;

import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.DefaultBeanDefinition;
import com.elevenware.redcorn.beans.ResolvableDependencyInstantiationOrdering;
import com.elevenware.redcorn.lifecycle.Lifecycle;
import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRedcornContainer implements RedcornContainer {
    private Map<String, DefaultBeanDefinition> definitions;
    private Map<String, DefaultBeanDefinition> context;
    private Map<Class<?>, DefaultBeanDefinition> classContext;

    public AbstractRedcornContainer() {
        definitions = new HashMap<>();
        context = new HashMap<>();
        classContext = new HashMap<>();
    }

    private boolean started = false;

    @Override
    public BeanDefinition register(Class<?> clazz) {
        DefaultBeanDefinition definition = new DefaultBeanDefinition(clazz);
        definitions.put(definition.getName(), definition);
        return definition;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        assertStarted();
        for(Map.Entry<Class<?>, DefaultBeanDefinition> entry: classContext.entrySet()) {
            if(clazz.isAssignableFrom(entry.getKey())) {
                return (T) entry.getValue().getPayload();
            }
        }
        return null;
    }

    @Override
    public void start() {
        List<DefaultBeanDefinition> beans = new ArrayList<>(definitions.values());

        ReferenceResolutionContext resolutionContext = createResolutionContext(context, classContext);
        ResolvableDependencyInstantiationOrdering ordering = new ResolvableDependencyInstantiationOrdering(beans, resolutionContext);
        List<DefaultBeanDefinition> sorted = ordering.sort();
        for(DefaultBeanDefinition bean: sorted) {
            bean.setResolutionContext(resolutionContext);
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

    protected abstract ReferenceResolutionContext createResolutionContext(Map<String, DefaultBeanDefinition> context, Map<Class<?>, DefaultBeanDefinition> classContext);

    @Override
    public void stop() {

    }

    @Override
    public <T> T get(String name) {
        assertStarted();
        DefaultBeanDefinition definition = context.get(name);
        if(definition != null) {
            return (T) definition.getPayload();
        }
        return null;
    }

    @Override
    public DefaultBeanDefinition register(String name, Class<?> type) {
        DefaultBeanDefinition definition = new DefaultBeanDefinition(type, name);
        definitions.put(definition.getName(), definition);
        return definition;
    }

    @Override
    public <T> T find(String name) {
        T found = get(name);
        if(found == null) {
            throw new BeanNotFoundException(name);
        }
        return found;
    }

    @Override
    public <T> T find(Class<T> clazz) {
        T object = get(clazz);
        if(object == null) {
            throw new BeanNotFoundException(clazz.getCanonicalName());
        }
        return object;
    }


    @Override
    public void refresh() {
        start();
    }


    private void assertStarted() {
        if(!started) {
            throw new ContainerNotStartedException();
        }
    }
}
