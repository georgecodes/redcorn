package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.List;

public interface InstantiationStrategy {

    Object instantiate();
    boolean isSatisfied();

}
