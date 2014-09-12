package com.elevenware.redcorn.model;

import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.List;

public interface InstantiationStrategy {

    Object instantiate();
    boolean isSatisfied();

}
