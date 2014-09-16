package com.elevenware.redcorn.config;

public class SimpleServer {

    private int port;

//    public SimpleServer(int port) {
//        this.port = port;
//    }

    public SimpleServer(String port) {
        this.port = Integer.parseInt(port);
    }
    public SimpleServer(Integer port) {
        this.port = port.intValue();
    }

    public int getPort() {
        return port;
    }
}
