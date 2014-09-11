package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.*;

import java.lang.reflect.Constructor;

public class ResolvableBeanDefinition implements BeanDefinition {


    private final Class<?> type;
    private final String name;
    private final ConstructorModel constructorModel;
    private ConstructorInjectionModel constructorArguments;
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
        this.constructorArguments = new ConstructorInjectionModel();
    }

    @Override
    public boolean canInstantiate() {

       return instantiationStrategy != null;

    }

    @Override
    public void instantiate() {
        payload =  instantiationStrategy.instantiate();
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
        this.resolutionContext = resolutionContext;
        constructorArguments.setContext(resolutionContext);
    }

    @Override
    public ResolvableBeanDefinition addConstructorArg(Object arg) {
        constructorArguments.addConstructorArg(new ConcreteInjectableArgument(arg));
        return this;
    }

    @Override
    public ResolvableBeanDefinition addConstructorRef(String other) {
        constructorArguments.addConstructorArg(new ReferenceInjectableArgument(other));
        return this;
    }

    @Override
    public ResolvableBeanDefinition addConstructorRef(Class<?> clazz) {
        constructorArguments.addConstructorArg(new ReferenceInjectableArgument(clazz.getCanonicalName(), clazz));
        return this;
    }

    @Override
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
        return constructorArguments.getArguments().size() > 0;
    }

    private void createInstantiationStrategyFrom(ConstructorInjectionModel constructorArguments) {
        boolean allConcrete = true;
        boolean allReferenceCanResolve = true;
        for(InjectableArgument argument: constructorArguments) {
            if(ReferenceInjectableArgument.class.isAssignableFrom(argument.getClass())) {
                ReferenceInjectableArgument arg = (ReferenceInjectableArgument) argument;
                arg.setContext(resolutionContext);
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

    public ResolvableBeanDefinition addConstructorRef(String name, Class<?> type) {
        return null;
    }

}
