package com.elevenware.ioc.hierarchy;

/**
 * Created by pairing on 04/08/2014.
 */
public class FooHandler {

    private Worker worker;
    private Helper helper;

    public FooHandler(Worker worker, Helper helper) {
        this.worker = worker;
        this.helper = helper;
    }

    public Worker getWorker() {
        return worker;
    }

    public Helper getHelper() {
        return helper;
    }
}
