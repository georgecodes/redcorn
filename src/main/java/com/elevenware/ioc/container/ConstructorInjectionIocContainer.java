package com.elevenware.ioc.container;

import com.elevenware.ioc.DependencyInstantiationOrdering;
import com.elevenware.ioc.Lifecycle;
import com.elevenware.ioc.beans.BeanDefinition;
import com.elevenware.ioc.beans.DefaultBeanDefinition;
import com.elevenware.ioc.visitors.Visitors;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ConstructorInjectionIocContainer implements IocContainer {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ConstructorInjectionIocContainer.class);

    private boolean started = false;
    private List<BeanDefinition> registeredTypes;
    private Map<String, BeanDefinition> context;

    public ConstructorInjectionIocContainer() {
        registeredTypes = new ArrayList<>();
        context = new HashMap<>();
        log.trace("Container created - " + this);
    }

    @Override
    public <T> T find(Class<T> clazz) {
        checkStarted();
        BeanDefinition definition = context.get(clazz.getCanonicalName());
        if(definition != null) {
            return (T) definition.getPayload();
        }
        for(BeanDefinition bean: context.values()) {
            Class superType = bean.getType();
            if(clazz.isAssignableFrom(superType)) {
                return (T) bean.getPayload();
            }
        }
       return null;
    }

    @Override
    public <T> T find(String id) {
        checkStarted();
        return (T) context.get(id).getPayload();
    }

    @Override
    public Collection<BeanDefinition> getBeanDefinitions() {
        return Collections.unmodifiableCollection(this.context.values());
    }

    @Override
    public void addDefinition(BeanDefinition definition) {
        this.context.put(definition.getName(), definition);
    }

    @Override
    public BeanDefinition register(String name, Class clazz) {
        BeanDefinition definition = new DefaultBeanDefinition(clazz, name);
        registeredTypes.add(definition);
        log.trace("Registered bean defintion for " + clazz);
        return definition;
    }

    @Override
    public BeanDefinition register(Class concreteType) {
        return register(concreteType.getCanonicalName(), concreteType);
    }

    private void checkStarted() {
        if(!started) {
            throw new ContainerNotStartedException();
        }
    }

    @Override
    public void start() {
        log.trace("Starting container " + this);
        DependencyInstantiationOrdering ordering = new DependencyInstantiationOrdering(this.registeredTypes);
        List<BeanDefinition> dependencyChain = ordering.sort();
        Visitors.constructorArgsInstantiator(this).visitAll(dependencyChain);
        if(this.context.isEmpty()) {
            throw new RuntimeException("No beans configured");
        }
        for(BeanDefinition definition: context.values()) {
            if(Lifecycle.class.isAssignableFrom(definition.getType())) {
                Lifecycle lifecycle = (Lifecycle) definition.getPayload();
                lifecycle.start();
            }
        }
        started = true;
    }

}
