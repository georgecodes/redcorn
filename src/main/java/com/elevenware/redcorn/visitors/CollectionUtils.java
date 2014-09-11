package com.elevenware.redcorn.visitors;

import java.util.*;

public class CollectionUtils {

    static WrappedCollection collectFrom(Collection<?> collection) {
        return new WrappedCollection(collection);
    }

    public static <T> Collection<T> newInstance(Collection<T> delegate) {
        if(Set.class.isAssignableFrom(delegate.getClass())) {
            return new HashSet<T>();
        }
//        if(List.class.isAssignableFrom(delegate.getClass())) {
            return new ArrayList<>();
//        }
//        throw new RuntimeException("What sort of collection is this??");
    }

    public static <T> List<T> asList(T object) {
        List<T> list = new ArrayList<>();
        list.add(object);
        return list;
    }
}
