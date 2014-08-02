package com.elevenware.ioc.beans;

import java.lang.reflect.Constructor;
import java.util.*;

public class ConstructorModel {

    private static final Comparator<? super Constructor> SORT_BY_ARGS = new Comparator<Constructor>() {
        @Override
        public int compare(Constructor o1, Constructor o2) {
            return Integer.compare(o2.getParameterTypes().length, o1.getParameterTypes().length);
        }
    };
    private List<Constructor> constructors;

    public ConstructorModel(Class clazz) {
       constructors = new ArrayList<>();
       for(Constructor constructor: clazz.getDeclaredConstructors()) {
            constructors.add(constructor);
       }
    }

    public boolean hasConstructorFor(List<Object> constructorArgs) {
        for(Constructor constructor: constructors) {
            if (isSuitable(constructor, constructorArgs)) {
                return true;
            }
        }
        return false;
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
            throw new RuntimeException();
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
        List<Constructor> constructorList = new ArrayList<>();
        for(Constructor constructor: constructors) {
            if(hasAllTypes(constructor, classes)) {
                constructorList.add(constructor);
            }
        }
        return constructorList;
    }

    private boolean hasAllTypes(Constructor constructor, List<Class<?>> classes) {
        for(Class constructorArgType: constructor.getParameterTypes()) {
            if(!classes.contains(constructorArgType)) {
                return false;
            }
        }
        return true;
    }

    public Constructor findBestConstructorsForTypes(List<Class<?>> classes) {
        List<Constructor> candidateConstructors = findConstructorsForTypes(classes);
        Collections.sort(candidateConstructors, SORT_BY_ARGS);
        return candidateConstructors.get(0);
    }
}
