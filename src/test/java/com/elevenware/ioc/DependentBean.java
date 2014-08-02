package com.elevenware.ioc;

public class DependentBean {

    private SimpleBean simpleBean;

    public DependentBean(SimpleBean simpleBean) {
        this.simpleBean = simpleBean;
    }

    public DependentBean(String name, SimpleBean simpleBean) {}
    public DependentBean(String name, SimpleBean simpleBean, Runnable runnable) {}

    public SimpleBean getSimpleBean(){
        return simpleBean;
    }

}
