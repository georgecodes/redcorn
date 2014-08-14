package com.elevenware.redcorn;

public class DependentBean {

    private SimpleBean simpleBean;
    private String message;

    public DependentBean(SimpleBean simpleBean) {
        this.simpleBean = simpleBean;
    }

    public DependentBean(String name, SimpleBean simpleBean) {

        this.message = name;
        this.simpleBean = simpleBean;

    }
    public DependentBean(String name, SimpleBean simpleBean, Runnable runnable) {}

    public SimpleBean getSimpleBean(){
        return simpleBean;
    }

    public String getMessage() {
        return message;
    }
}
