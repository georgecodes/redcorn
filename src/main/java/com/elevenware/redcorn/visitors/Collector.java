package com.elevenware.redcorn.visitors;

public interface Collector<T, R> {

    R doCollect(T input);

}
