package com.elevenware.ioc.container;

import ch.qos.logback.classic.Logger;
import com.elevenware.ioc.DependencyInstantiationOrdering;
import com.elevenware.ioc.EventListener;
import com.elevenware.ioc.beans.BeanDefinition;
import com.elevenware.ioc.beans.DefaultBeanDefinition;
import com.elevenware.ioc.visitors.BeanDefinitionVisitor;
import com.elevenware.ioc.visitors.Visitors;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleIocContainer implements IocContainer {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SimpleIocContainer.class);

    private boolean started = false;
    private List<BeanDefinition> registeredTypes;
    private Map<Class, BeanDefinition> context;

    public SimpleIocContainer() {
        registeredTypes = new ArrayList<>();
        context = new HashMap<>();
        log.trace("Container created - " + this);
    }

    @Override
    public void register(Class clazz) {
        BeanDefinition definition = new DefaultBeanDefinition(clazz);
        registeredTypes.add(definition);
        log.trace("Registered bean defintion for " + clazz);
    }

    @Override
    public <T> T find(Class<T> clazz) {
        checkStarted();
        BeanDefinition definition = context.get(clazz);
        return (T) definition.getPayload();
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
//        instantiateEasyBeans();
//        instantiateBeansWithDependencies();
        if(this.context.isEmpty()) {
            throw new RuntimeException("No beans configured");
        }
        started = true;
    }

    private void instantiateBeansWithDependencies() {
        Visitors.constructorArgsInstantiator(this.context).visitAll(this.registeredTypes);
    }

    private void instantiateEasyBeans() {
        Visitors.simpleTypeInstantiator(new EventListener<BeanDefinition>() {
            @Override
            public void doNotify(BeanDefinition event) {
                log.trace("Bean Definition " + event + " instantiated and registered in context");
                SimpleIocContainer.this.context.put(event.getType(), event);
                event.markResolved();
            }
        }).visitAll(registeredTypes);
    }
}
