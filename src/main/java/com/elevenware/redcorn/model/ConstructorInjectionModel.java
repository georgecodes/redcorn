package com.elevenware.redcorn.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConstructorInjectionModel implements ReferenceResolver, Iterable<InjectableArgument> {
    private ReferenceResolutionContext context;
    private List<InjectableArgument> arguments;
    private Object[] inflatedArguments;

    public ConstructorInjectionModel(ReferenceResolutionContext context) {
        this();
        this.context = context;
    }

    public ConstructorInjectionModel() {
        this.arguments = new ArrayList<>();
    }

    public void setResolutionContext(ReferenceResolutionContext ctx) {
        this.context = ctx;
        rewireResolutionContext(ctx);
    }

    public void addConstructorArg(InjectableArgument injectableArgument) {
        if(ReferenceInjectableArgument.class.isAssignableFrom(injectableArgument.getClass())) {
            ((ReferenceInjectableArgument) injectableArgument).setContext(context);
        }
        arguments.add(injectableArgument);
    }

    public List<InjectableArgument> getArguments() {
        return arguments;
    }

    public Object[] getInflatedArguments() {
        if(inflatedArguments == null) {
            inflatedArguments = inflateConstructorArgs();
        }
        return inflatedArguments;
    }

    public Object[] inflateConstructorArgs() {

        List<Object> inflatedArgs = new ArrayList<>();
        for(InjectableArgument arg: arguments) {
            if(ReferenceInjectableArgument.class.isAssignableFrom(arg.getClass())) {
                ((ReferenceInjectableArgument) arg).setContext(context);
            }
            arg.inflate();
            inflatedArgs.add(arg.getPayload());
        }

        return inflatedArgs.toArray();
    }

    public boolean matchesConstructorArgumentsInOrder(Constructor constructor) {
        if(constructor.getParameterTypes().length != arguments.size()) {
            return false;
        }
        Iterator<InjectableArgument> iter = arguments.iterator();
        for(Class clazz: constructor.getParameterTypes()) {
            InjectableArgument arg = iter.next();
            if(!arg.compatibleWith(clazz)) {
                return false;
            }
        }
        return true;
    }

    public List<Class<?>> getTypes() {
        List<Class<?>> types = new ArrayList<>();
        for(InjectableArgument argument: arguments) {
            types.add(argument.getType());
        }
        return types;
    }

    public boolean isResolved() {
        for(InjectableArgument argument: arguments) {
            if(argument.getPayload() == null) {
                return false;
            }
        }
        return true;
    }

    public List<Object> getConcreteConstructorArgs() {
        List<Object> concreteConstructorArgs = new ArrayList<>();
        for(InjectableArgument argument: arguments) {
            if(ConcreteInjectableArgument.class.isAssignableFrom(argument.getClass())) {
                concreteConstructorArgs.add(argument.getPayload());
            }
         }
        return concreteConstructorArgs;
    }

    @Override
    public Iterator<InjectableArgument> iterator() {
        return arguments.iterator();
    }

    public boolean canResolve() {
        for(InjectableArgument argument: arguments) {
            if(ReferenceInjectableArgument.class.isAssignableFrom(argument.getClass())) {
                ((ReferenceInjectableArgument) argument).setContext(context);
                if(!argument.canResolve()) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Class<?>> getConfiguredTypes() {
        List<Class<?>> types = new ArrayList<>();
        for(InjectableArgument argument: arguments) {
            if(ReferenceInjectableArgument.class.isAssignableFrom(argument.getClass())) {
                ((ReferenceInjectableArgument) argument).setContext(context);
            }
            types.add(argument.getType());
        }
        return types;
    }

    private void rewireResolutionContext(ReferenceResolutionContext context) {
        for(InjectableArgument argument: arguments) {
            if(ReferenceInjectableArgument.class.isAssignableFrom(argument.getClass())) {
                ((ReferenceInjectableArgument) argument).setContext(context);
            }
        }
    }
}
