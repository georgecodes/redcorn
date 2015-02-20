package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.TypesMatcher;

import java.lang.reflect.Constructor;
import java.util.*;

public class ConstructorModel {

    private static final Comparator<? super Constructor> SORT_BY_ARGS = new Comparator<Constructor>() {
        @Override
        public int compare(Constructor o1, Constructor o2) {
            Integer o1Length = Integer.valueOf(o1.getParameterTypes().length);
            Integer o2Length = Integer.valueOf(o2.getParameterTypes().length);
            return o2Length.compareTo(o1Length);
        }
    };
    private final Class type;
    private List<Constructor> constructors;

    public ConstructorModel(Class clazz) {
        this.type = clazz;
       constructors = new ArrayList<Constructor>();
       for(Constructor constructor: clazz.getDeclaredConstructors()) {
            constructors.add(constructor);
       }
    }

    public boolean hasConstructorFor(List<Object> constructorArgs) {
        return findConstructorFor(constructorArgs) != null;
    }

    private boolean isSuitable(Constructor constructor, List<Object> constructorArgs) {
        if(! (constructor.getParameterTypes().length == constructorArgs.size()) ) {
            return false;
        }
        Iterator<Object> iter = constructorArgs.iterator();
        for(Class clazz: constructor.getParameterTypes()) {
            if(!clazz.isAssignableFrom(iter.next().getClass())) {
               return false;
            }
        }
        return true;
    }

    public void assertHasConstructorFor(List<Object> constructorArgs) {
        if(!hasConstructorFor(constructorArgs)) {
            StringBuilder buf = new StringBuilder("Unable to resolve constructor for type ")
                    .append(this.type)
                    .append(" and arguments ")
                    .append(constructorArgs);
            throw new RuntimeException(buf.toString());
        }
    }

    public Constructor findConstructorFor(List<Object> constructorArgs) {
        for(Constructor constructor: constructors) {
            if (isSuitable(constructor, constructorArgs)) {
                return constructor;
            }
        }
        return null;
    }

    public List<Constructor> findConstructorsForTypes(List<Class<?>> classes) {
        List<Constructor> constructorList = new ArrayList<Constructor>();
        for(Constructor constructor: constructors) {
            if(hasAllTypes(constructor, classes)) {
                constructorList.add(constructor);
            }
        }
        return constructorList;
    }

    private boolean hasAllTypes(Constructor constructor, List<Class<?>> classes) {
        if(constructor.getParameterTypes().length == 0) {
            return true;
        }
        boolean allMatched = false;
        for(Class constructorArgType: constructor.getParameterTypes()) {
            boolean thisOneMatches = false;
            for(Class argType: classes) {
                if(constructorArgType.isAssignableFrom(argType)) {
                    thisOneMatches = true;
                }
            }
            allMatched = thisOneMatches;
        }
        return allMatched;
//        return new TypesMatcher(constructor.getParameterTypes()).matchesClassesInOrder(classes.toArray(new Class[0]));
    }

    public Constructor findBestConstructorsForTypes(List<Class<?>> classes) {
        return findBestConstructorsForTypes(classes, Collections.emptyList());
    }


        public Constructor findBestConstructorsForTypes(List<Class<?>> classes, List<Object> args) {
        List<Class<?>> actualClasses = new ArrayList<Class<?>>(classes);
        for(Object object: args) {
            actualClasses.add(object.getClass());
        }
        List<Constructor> candidateConstructors = findConstructorsForTypes(actualClasses);
        if(candidateConstructors.isEmpty()) {
            return null;
        }
        Collections.sort(candidateConstructors, SORT_BY_ARGS);
        return candidateConstructors.get(0);
    }

}
