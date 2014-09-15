package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.*;
import com.elevenware.redcorn.properties.PropertyInjectionModel;

import java.lang.reflect.Constructor;
import java.util.Map;

public class DefaultBeanDefinition implements BeanDefinition {


    private final Class<?> type;
    private final String name;
    private final ConstructorModel constructorModel;
    private PropertyModel propertyModel;
    private ConstructorInjectionModel constructorArguments;
    private PropertyInjectionModel propertyArguments;
    private Object payload;
    private SwitchableReferenceResolutionContext resolutionContext;
    private InstantiationStrategy instantiationStrategy;

    public DefaultBeanDefinition(Class<?> type) {
        this(type, type.getCanonicalName());
    }

    public DefaultBeanDefinition(Class<?> type, String name) {
        this.type = type;
        this.name = name;
        this.constructorModel = new ConstructorModel(type);
        this.propertyModel = new PropertyModel(type);
        this.constructorArguments = new ConstructorInjectionModel();
        this.propertyArguments = new PropertyInjectionModel();
        this.resolutionContext = new SwitchableReferenceResolutionContext();
    }

    @Override
    public boolean canInstantiate() {

       return instantiationStrategy != null;

    }

    @Override
    public void instantiate() {
        Object object =  instantiationStrategy.instantiate();
        setProperties(object);
        this.payload = object;
    }

    private void setProperties(Object object) {
        for(Map.Entry<String, InjectableArgument> configuredProperty: propertyArguments) {
            PropertyModel.PropertyDefinition property = propertyModel.getProperty(configuredProperty.getKey());
            if(property == null) {
                throw new RuntimeException(type.getCanonicalName() +
                        " doesn't have a writable property " + configuredProperty.getKey());
            }
            InjectableArgument argument = configuredProperty.getValue();
            wireInContext(argument);
            argument.inflate();
            property.setProperty(object, argument.getPayload());
        }
    }

    private void wireInContext(InjectableArgument argument) {
        if(ReferenceResolver.class.isAssignableFrom(argument.getClass())) {
            ((ReferenceResolver) argument).setResolutionContext(this.resolutionContext);
        }
    }

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public void setResolutionContext(ReferenceResolutionContext resolutionContext) {
        this.resolutionContext.setDelegate(resolutionContext);
        constructorArguments.setResolutionContext(this.resolutionContext);
    }

    @Override
    public DefaultBeanDefinition addConstructorArg(Object arg) {
        constructorArguments.addConstructorArg(new ConcreteInjectableArgument(arg));
        return this;
    }

    @Override
    public DefaultBeanDefinition addConstructorRef(String other) {
        constructorArguments.addConstructorArg(new ReferenceInjectableArgument(other));
        return this;
    }

    @Override
    public DefaultBeanDefinition addConstructorRef(Class<?> clazz) {
        constructorArguments.addConstructorArg(new ReferenceInjectableArgument(clazz.getCanonicalName(), clazz));
        return this;
    }

    @Override
    public void prepare() {

        constructorArguments.setResolutionContext(resolutionContext);

        if(explicitConstructorArgumentsConfigured()) {
            createInstantiationStrategyFrom(constructorArguments);
            return;
        }

        if(implicitConstructorArgumentsExist()) {
            Constructor constructor = constructorModel.findBestConstructorsForTypes(resolutionContext.getContainedTypes());
            this.instantiationStrategy = new ImplicitConstructorArgsInstantiationStrategy(constructor, resolutionContext);
            return;
        }

        if(emptyConstructorExists()) {
            this.instantiationStrategy = new EmptyConstructorInstantiationStrategy(this.type);
            return;
        }

    }

    private boolean emptyConstructorExists() {
        return constructorModel.hasEmptyConstructor();
    }

    private boolean implicitConstructorArgumentsExist() {
        return constructorModel.findBestConstructorsForTypes(resolutionContext.getContainedTypes()) != null;
    }

    private boolean explicitConstructorArgumentsConfigured() {
        return constructorArguments.getArguments().size() > 0;
    }

    private void createInstantiationStrategyFrom(ConstructorInjectionModel constructorArguments) {
        boolean allConcrete = true;
        boolean allReferenceCanResolve = true;
        for(InjectableArgument argument: constructorArguments) {
            if(ReferenceInjectableArgument.class.isAssignableFrom(argument.getClass())) {
                ReferenceInjectableArgument arg = (ReferenceInjectableArgument) argument;
                arg.setResolutionContext(resolutionContext);
                if(!arg.canResolve()) {
                    allReferenceCanResolve = false;
                }
            }
            if(!ConcreteInjectableArgument.class.isAssignableFrom(argument.getClass())) {
                allConcrete = false;
            }

        }
        if(allConcrete) {
            Constructor constructor = constructorModel.findConstructorFor(constructorArguments);
            instantiationStrategy = new ConcreteArgumentsInstantiationStrategy(constructor, constructorArguments);
            return;
        }
        if(allReferenceCanResolve) {
            Constructor constructor = constructorModel.findConstructorFor(constructorArguments);
            instantiationStrategy = new ReferenceArgsInstantiationStrategy(constructor, constructorArguments);
            return;
        }
        throw new RuntimeException("Cannot resolve all reference constructor arguments for " + type);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isSatisfied() {
        return instantiationStrategy.isSatisfied();
    }

    @Override
    public BeanDefinition addProperty(String name, Object property) {
        propertyArguments.addProperty(name, new ConcreteInjectableArgument(property));
        return this;
    }

    @Override
    public BeanDefinition referenceProperty(String propertyName, String reference) {
        propertyArguments.addProperty(propertyName, new ReferenceInjectableArgument(reference));
        return this;
    }

    public DefaultBeanDefinition addConstructorRef(String name, Class<?> type) {
        constructorArguments.addConstructorArg(new ReferenceInjectableArgument(name, type));
        return this;
    }

}
