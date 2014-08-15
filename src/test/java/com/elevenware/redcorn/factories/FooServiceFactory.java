package com.elevenware.redcorn.factories;

import com.elevenware.redcorn.SimpleBean;

public class FooServiceFactory {

    private String fooName;

    public FooServiceFactory() {}

    public FooServiceFactory(String fooName, SimpleBean bean) {
        this.fooName = fooName;
    }

    public FooService createFooService() {
        if(fooName == null) {
            return new FooService();
        } else {
            return new FooService(fooName);
        }

    }

}
