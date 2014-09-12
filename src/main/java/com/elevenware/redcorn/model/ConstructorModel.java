package com.elevenware.redcorn.model;

import com.elevenware.redcorn.model.ConstructorInjectionModel;

import java.lang.reflect.Constructor;
import java.util.*;

public class ConstructorModel {

    private static final Comparator<? super Constructor> SORT_BY_ARGS = new Comparator<Constructor>() {
        @Override
        public int compare(Constructor o1, Constructor o2) {
            return Integer.compare(o2.getParameterTypes().length, o1.getParameterTypes().length);
        }
    };
    private final Class type;
    private List<Constructor> constructors;

    public ConstructorModel(Class clazz) {
        this.type = clazz;
       constructors = new ArrayList<>();
       for(Constructor constructor: clazz.getDeclaredConstructors()) {
            constructors.add(constructor);
       }
    }

    @Deprecated
    public boolean hasConstructorFor(List<Object> constructorArgs) {
        return findConstructorFor(constructorArgs) != null;
    }

    @Deprecated
    public boolean hasConstructorForClasses(Collection<Class<?>> classes) {
        return findConstructorForClasses(classes) != null;
    }

    @Deprecated
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

    private boolean isSuitableForClasses(Constructor constructor, Collection<Class<?>> classes) {
        if(! (constructor.getParameterTypes().length == classes.size()) ) {
            return false;
        }
        Iterator<Class<?>> iter = classes.iterator();
        for(Class clazz: constructor.getParameterTypes()) {
            if(!clazz.isAssignableFrom(iter.next())) {
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

    public Constructor findConstructorForClasses(Collection<Class<?>> classes) {
        for(Constructor constructor: constructors) {
            if (isSuitableForClasses(constructor, classes)) {
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
        if(constructor.getParameterTypes().length == 0) {
            return true;
        }
        for(Class constructorArgType: constructor.getParameterTypes()) {
            boolean thisOneMatches = false;
            for(Class argType: classes) {
                if(constructorArgType.isAssignableFrom(argType)) {
                    thisOneMatches = true;
                }
            }
            if(!thisOneMatches) {
                return false;
            }
        }
        return true;
    }

    public Constructor findBestConstructorsForTypes(Collection<Class<?>> classes) {
        return findBestConstructorsForTypes(classes, Collections.emptyList());
    }


    public Constructor findBestConstructorsForTypes(Collection<Class<?>> classes, List<Object> args) {
        List<Class<?>> actualClasses = new ArrayList<>(classes);
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

    public Constructor findBestConstructorsForTypes(List<Class<?>> availableClasses, ConstructorInjectionModel argumentModel) {
        List<Class<?>> actualClasses = new ArrayList<>(availableClasses);
        actualClasses.addAll(argumentModel.getTypes());
        List<Constructor> candidateConstructors = findConstructorsForTypes(actualClasses);
        if(candidateConstructors.isEmpty()) {
            return null;
        }
        Collections.sort(candidateConstructors, SORT_BY_ARGS);
        return candidateConstructors.get(0);
    }

    public boolean hasConstructorFor(ConstructorInjectionModel injectionModel) {
//        if(!injectionModel.isResolved()) {
//            return false;
//        }
        List<Class<?>> classes = injectionModel.getConfiguredTypes();
        return hasConstructorForClasses(classes);
    }

    public void assertHasConstructorFor(ConstructorInjectionModel constructorInjectionModel) {
        if(!hasConstructorFor(constructorInjectionModel)) {
            StringBuilder buf = new StringBuilder("Unable to resolve constructor for type ")
                    .append(this.type)
                    .append(" and arguments ")
                    .append(constructorInjectionModel);
            throw new RuntimeException(buf.toString());
        }
    }

    public Constructor findConstructorFor(ConstructorInjectionModel constructorInjectionModel) {
        for(Constructor constructor: constructors) {
            if (isSuitableForClasses(constructor, constructorInjectionModel.getTypes())) {
                return constructor;
            }
        }
        return null;
    }

    public boolean hasEmptyConstructor() {
        return hasConstructorForClasses(new ArrayList<Class<?>>());
    }
}
