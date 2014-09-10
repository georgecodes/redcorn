package com.elevenware.redcorn.config;

public class SimpleServer {

    private int port;

    public SimpleServer(String port) {
        this.port = Integer.parseInt(port);
    }

    public int getPort() {
        return port;
    }
}
