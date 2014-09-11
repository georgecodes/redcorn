package com.elevenware.redcorn.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class InjectableArgumentModel implements Iterable<InjectableArgument> {
    private ReferenceResolutionContext context;
    private List<InjectableArgument> constructorArgs;
    private Object[] inflatedConstructorArgs;

    public InjectableArgumentModel(ReferenceResolutionContext context) {
        this();
        this.context = context;
    }

    public InjectableArgumentModel() {
        this.constructorArgs = new ArrayList<>();
    }

    public void setContext(ReferenceResolutionContext ctx) {
        this.context = ctx;
        rewireResolutionContext(ctx);
    }

    public void addConstructorArg(InjectableArgument injectableArgument) {
        if(ReferenceInjectableArgument.class.isAssignableFrom(injectableArgument.getClass())) {
            ((ReferenceInjectableArgument) injectableArgument).setContext(context);
        }
        constructorArgs.add(injectableArgument);
    }

    public List<InjectableArgument> getConstructorArgs() {
        return constructorArgs;
    }

    public Object[] getInflatedConstructorArgs() {
        if(inflatedConstructorArgs == null) {
            inflatedConstructorArgs = inflateConstructorArgs();
        }
        return inflatedConstructorArgs;
    }

    public Object[] inflateConstructorArgs() {

        List<Object> inflatedArgs = new ArrayList<>();
        for(InjectableArgument arg: constructorArgs) {
            if(ReferenceInjectableArgument.class.isAssignableFrom(arg.getClass())) {
                ((ReferenceInjectableArgument) arg).setContext(context);
            }
            arg.inflate();
            inflatedArgs.add(arg.getPayload());
        }

        return inflatedArgs.toArray();
    }

    public boolean matchesConstructorArgumentsInOrder(Constructor constructor) {
        if(constructor.getParameterTypes().length != constructorArgs.size()) {
            return false;
        }
        Iterator<InjectableArgument> iter = constructorArgs.iterator();
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
        for(InjectableArgument argument: constructorArgs) {
            types.add(argument.getType());
        }
        return types;
    }

    public boolean isResolved() {
        for(InjectableArgument argument: constructorArgs) {
            if(argument.getPayload() == null) {
                return false;
            }
        }
        return true;
    }

    public List<Object> getConcreteConstructorArgs() {
        List<Object> concreteConstructorArgs = new ArrayList<>();
        for(InjectableArgument argument: constructorArgs) {
            if(ConcreteInjectableArgument.class.isAssignableFrom(argument.getClass())) {
                concreteConstructorArgs.add(argument.getPayload());
            }
         }
        return concreteConstructorArgs;
    }

    @Override
    public Iterator<InjectableArgument> iterator() {
        return constructorArgs.iterator();
    }

    public boolean canResolve() {
        for(InjectableArgument argument: constructorArgs) {
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
        for(InjectableArgument argument: constructorArgs) {
            if(ReferenceInjectableArgument.class.isAssignableFrom(argument.getClass())) {
                ((ReferenceInjectableArgument) argument).setContext(context);
            }
            types.add(argument.getType());
        }
        return types;
    }

    private void rewireResolutionContext(ReferenceResolutionContext context) {
        for(InjectableArgument argument: constructorArgs) {
            if(ReferenceInjectableArgument.class.isAssignableFrom(argument.getClass())) {
                ((ReferenceInjectableArgument) argument).setContext(context);
            }
        }
    }
}
