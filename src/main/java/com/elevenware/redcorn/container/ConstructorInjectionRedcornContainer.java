package com.elevenware.redcorn.container;

import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.ExtendedBeanDefinition;
import com.elevenware.redcorn.lifecycle.Lifecycle;
import com.elevenware.redcorn.beans.DefaultBeanDefinition;
import com.elevenware.redcorn.lifecycle.ContainerAware;
import com.elevenware.redcorn.visitors.AbstractBeanDefinitionVisitor;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ConstructorInjectionRedcornContainer implements RedcornContainer {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ConstructorInjectionRedcornContainer.class);

    private boolean started = false;
    protected List<BeanDefinition> registeredTypes;
    private Map<String, BeanDefinition> context;

    public ConstructorInjectionRedcornContainer() {
        registeredTypes = new ArrayList<>();
        context = new HashMap<>();
        log.trace("Container created - " + this);
    }

    @Override
    public <T> T get(Class<T> clazz) {
        checkStarted();
        hydrateNewBeans();
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
    public <T> T get(String id) {
        checkStarted();
        hydrateNewBeans();
        BeanDefinition defintion = context.get(id);
        if(defintion == null) {
            return null;
        }
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

    private void hydrateNewBeans() {

//        DependencyInstantiationOrdering ordering = new DependencyInstantiationOrdering(this.registeredTypes);
//        List<ExtendedBeanDefinition> dependencyChain = ordering.sort();
//        AbstractBeanDefinitionVisitor.constructorArgsInstantiator(this).visitAll(dependencyChain);

        for(BeanDefinition definition: context.values()) {
            if(Lifecycle.class.isAssignableFrom(definition.getType())) {
                Lifecycle lifecycle = (Lifecycle) definition.getPayload();
                lifecycle.start();
            }
            if(ContainerAware.class.isAssignableFrom(definition.getType())) {
                ((ContainerAware) definition.getPayload()).setContainer(this);
            }
        }

    }

    @Override
    public <T> T find(String name) {
        Object object = get(name);
        if(object == null) {
            throw new BeanNotFoundException(name);
        }
        return (T) object;
    }

    @Override
    public <T> T find(Class clazz) {
        Object object = get(clazz);
        if(object == null) {
            throw new BeanNotFoundException(clazz.getCanonicalName());
        }
        return (T) object;
    }

    @Override
    public RedcornContainer createChild(String name) {
//        BeanDefinition containerDef = new DefaultBeanDefinition(ConstructorInjectionIocContainer.class, name);
        BeanDefinition containerDef = this.register(name, ConstructorInjectionRedcornContainer.class);
        containerDef.instantiate();
        RedcornContainer child = (RedcornContainer) containerDef.getPayload();
        for(BeanDefinition bean: this.context.values()) {
            child.addDefinition(null);
        }
//        containerDef.markResolved();
        this.context.put(name, containerDef);
        child.start();
        return child;
    }

    @Override
    public BeanDefinition registerFactory(String name, String factoryMethod, Class factoryClass) {
        BeanDefinition definition = new FactoryBeanDefinition(name, factoryMethod, factoryClass);
        registeredTypes.add(definition);
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
//        DependencyInstantiationOrdering ordering = new DependencyInstantiationOrdering(this.registeredTypes);
//        List<ExtendedBeanDefinition> dependencyChain = ordering.sort();
//        AbstractBeanDefinitionVisitor.constructorArgsInstantiator(this).visitAll(dependencyChain);
        for(BeanDefinition definition: context.values()) {
            if(Lifecycle.class.isAssignableFrom(definition.getType())) {
                Lifecycle lifecycle = (Lifecycle) definition.getPayload();
                lifecycle.start();
            }
            if(ContainerAware.class.isAssignableFrom(definition.getType())) {
                ((ContainerAware) definition.getPayload()).setContainer(this);
            }
        }
        started = true;
    }

    @Override
    public void stop() {
        for(BeanDefinition definition: context.values()) {
            if (Lifecycle.class.isAssignableFrom(definition.getType())) {
                Lifecycle lifecycle = (Lifecycle) definition.getPayload();
                lifecycle.stop();
            }
        }
    }

}
