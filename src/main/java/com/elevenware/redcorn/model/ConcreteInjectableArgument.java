package com.elevenware.redcorn.model;

public class ConcreteInjectableArgument implements InjectableArgument {

    private Object payload;
    private Class type;

    public ConcreteInjectableArgument(Object i) {
        this.payload = i;
        this.type = i.getClass();
    }

    @Override
    public boolean compatibleWith(Class clazz) {
       if( type.isAssignableFrom(clazz) ) {
           return true;
       }
       return compatibleByUnboxing(clazz);
    }

    private boolean compatibleByUnboxing(Class clazz) {
        if (clazz == boolean.class) clazz = Boolean.class;
        if (clazz == char.class) clazz = Character.class;
        if (clazz == short.class) clazz = Short.class;
        if (clazz == int.class) clazz = Integer.class;
        if (clazz == double.class) clazz = Double.class;
        if (clazz == float.class) clazz = Float.class;
        if (clazz == long.class) clazz = Long.class;
        return type.isAssignableFrom(clazz);
    }

    @Override
    public void inflate() {

    }

    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public boolean canResolve() {
        return true;
    }
}
