package com.elevenware.redcorn;

public class HasInjectableProperty {

    private SimpleBean simpleBean;

    public void setSimpleBean(SimpleBean bean) {
        this.simpleBean = bean;
    }

    public SimpleBean getSimpleBean() {
        return simpleBean;
    }
}
