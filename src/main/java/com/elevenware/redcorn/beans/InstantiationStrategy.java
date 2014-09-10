package com.elevenware.redcorn.beans;

import java.util.List;

public interface InstantiationStrategy {
    Object instantiate();

    boolean isSatisfiedBy(List<Class<?>> availableTypes);
}
