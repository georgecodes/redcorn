package com.elevenware.ioc;

public class StartableBean implements Lifecycle {

    private boolean started = false;

    @Override
    public void start() {
       this.started = true;
    }

    public boolean isStarted() {
        return started;
    }
}
