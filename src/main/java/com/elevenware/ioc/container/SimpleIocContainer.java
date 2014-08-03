package com.elevenware.ioc.container;

import com.elevenware.ioc.EventListener;
import com.elevenware.ioc.beans.BeanDefinition;
import com.elevenware.ioc.beans.DefaultBeanDefinition;
import com.elevenware.ioc.visitors.BeanDefinitionVisitor;
import com.elevenware.ioc.visitors.Visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleIocContainer implements IocContainer {

    private boolean started = false;
    private List<BeanDefinition> registeredTypes;
    private Map<Class, BeanDefinition> context;

    public SimpleIocContainer() {
        registeredTypes = new ArrayList<>();
        context = new HashMap<>();
    }

    @Override
    public void register(Class clazz) {
        BeanDefinition definition = new DefaultBeanDefinition(clazz);
        registeredTypes.add(definition);
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
        instantiateEasyBeans();
        instantiateBeansWithDependencies();
        started = true;
    }

    private void instantiateBeansWithDependencies() {
        Visitors.constructorArgsInstantiator(this.context).visitAll(this.registeredTypes);
    }

    private void instantiateEasyBeans() {
        Visitors.simpleTypeInstantiator(new EventListener<BeanDefinition>() {
            @Override
            public void doNotify(BeanDefinition event) {
                SimpleIocContainer.this.context.put(event.getType(), event);
                event.markResolved();
            }
        }).visitAll(registeredTypes);
    }
}
