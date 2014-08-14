package com.elevenware.redcorn.container;

import com.elevenware.redcorn.NamedBean;

/**
 * Created by pairing on 04/08/2014.
 */
public class HasNamedBeanArg {

    private final NamedBean bean;

    public HasNamedBeanArg(NamedBean named) {
        this.bean = named;
    }

    public NamedBean getBean() {
        return bean;
    }

}
