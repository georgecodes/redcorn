package com.elevenware.ioc.container;

import com.elevenware.ioc.DependencyInstantiationOrdering;
import com.elevenware.ioc.Lifecycle;
import com.elevenware.ioc.beans.BeanDefinition;
import com.elevenware.ioc.beans.DefaultBeanDefinition;
import com.elevenware.ioc.visitors.Visitors;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstructorInjectionIocContainer implements IocContainer {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ConstructorInjectionIocContainer.class);

    private boolean started = false;
    private List<BeanDefinition> registeredTypes;
    private Map<Class, BeanDefinition> context;

    public ConstructorInjectionIocContainer() {
        registeredTypes = new ArrayList<>();
        context = new HashMap<>();
        log.trace("Container created - " + this);
    }

    @Override
    public <T> T find(Class<T> clazz) {
        checkStarted();
        for(Class superType: context.keySet()) {
            if(clazz.isAssignableFrom(superType)) {
                clazz = superType;
            }
        }
         BeanDefinition definition = context.get(clazz);
        return (T) definition.getPayload();
    }

    @Override
    public BeanDefinition register(Class concreteType) {

        BeanDefinition definition = new DefaultBeanDefinition(concreteType);
        registeredTypes.add(definition);
        log.trace("Registered bean defintion for " + concreteType);
        return definition;
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
        Visitors.constructorArgsInstantiator(context).visitAll(dependencyChain);
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
