package com.elevenware.redcorn.factories;

public class FooService {
    private final String fooName;

    public FooService() {
        this("Default name");
    }

    public FooService(String fooName) {
        this.fooName = fooName;
    }

    public String getText() {
        return fooName;
    }

}
