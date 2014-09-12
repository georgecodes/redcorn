package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.utils.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;

public class CollectingIterable<T, R> implements Collection<R> {
    private final Collection<T> delegate;
    private Collection<R> collected;
    private final Collector<T, R> collector;

    public CollectingIterable(Collection<T> delegate, Collector<T, R> collector) {
        this.delegate  = delegate;
        this.collector = collector;
        collected = (Collection<R>) CollectionUtils.newInstance(delegate);
        for(T a: delegate) {
            collected.add(collector.doCollect(a));
        }
    }

    @Override
    public int size() {
        return collected.size();
    }

    @Override
    public boolean isEmpty() {
        return collected.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return collected.contains(o);
    }

    @Override
    public Iterator<R> iterator() {
        return collected.iterator();
    }

    @Override
    public Object[] toArray() {
        return collected.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return collected.toArray(a);
    }

    @Override
    public boolean add(R r) {
        return collected.add(r);
    }

    @Override
    public boolean remove(Object o) {
        return collected.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return collected.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends R> c) {
        return collected.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return collected.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return collected.retainAll(c);
    }

    @Override
    public void clear() {
        collected.clear();
    }
}
