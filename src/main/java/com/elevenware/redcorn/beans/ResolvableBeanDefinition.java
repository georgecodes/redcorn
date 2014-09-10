package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ResolvableBeanDefinition {

    private final Class<?> type;
    private final String name;
    private final ConstructorModel constructorModel;
    private InjectableArgumentModel constructorArguments;
    private Object payload;
    private ReferenceResolutionContext resolutionContext;
    private InstantiationStrategy instantiationStrategy;

    public ResolvableBeanDefinition(Class<?> type) {
        this(type, type.getCanonicalName());
    }

    public ResolvableBeanDefinition(Class<?> type, String name) {
        this.type = type;
        this.name = name;
        this.constructorModel = new ConstructorModel(type);
        this.constructorArguments = new InjectableArgumentModel();
    }

    public boolean canInstantiate() {

       return instantiationStrategy != null;

    }

    public boolean isSatisfiedBy(List<Class<?>> availableTypes) {
        return instantiationStrategy.isSatisfiedBy(availableTypes);
    }

    public void instantiate() {
        payload =  instantiationStrategy.instantiate();
    }

    public Class getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setResolutionContext(ReferenceResolutionContext resolutionContext) {
        this.resolutionContext = resolutionContext;
    }

    public ResolvableBeanDefinition addConstructorArg(Object arg) {
        constructorArguments.addConstructorArg(new ConcreteInjectableArgument(arg));
        return this;
    }

    public ResolvableBeanDefinition addConstructorRef(String other) {
        constructorArguments.addConstructorArg(new ReferenceInjectableArgument(other));
        return this;
    }

    public ResolvableBeanDefinition addConstructorRef(Class<?> clazz) {
        constructorArguments.addConstructorArg(new ReferenceInjectableArgument(clazz.getCanonicalName(), clazz));
        return this;
    }

    public void prepare() {

        constructorArguments.setContext(resolutionContext);

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
        return constructorArguments.getConstructorArgs().size() > 0;
    }

    private void createInstantiationStrategyFrom(InjectableArgumentModel constructorArguments) {
        for(InjectableArgument argument: constructorArguments) {
            if(ReferenceInjectableArgument.class.isAssignableFrom(argument.getClass())) {
                ReferenceInjectableArgument arg = (ReferenceInjectableArgument) argument;
                arg.setContext(resolutionContext);
            }

        }
        Constructor constructor = constructorModel.findConstructorFor(constructorArguments);
        instantiationStrategy = new ConfiguredArgumentsInstantiationStrategy(constructor, constructorArguments);
        return;
    }

    public String getName() {
        return name;
    }
}
