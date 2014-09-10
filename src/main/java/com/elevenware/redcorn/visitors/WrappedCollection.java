package com.elevenware.redcorn.visitors;

import java.util.Collection;

public class WrappedCollection<T, R> {
    private final Collection<T> delegate;

    public WrappedCollection(Collection<T> collection) {
        this.delegate = collection;
    }

    public Collection<R> collect(Collector<T, R> collector) {
        return new CollectingIterable(delegate, collector);
    }

}
