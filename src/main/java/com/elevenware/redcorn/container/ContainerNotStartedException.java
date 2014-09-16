package com.elevenware.redcorn.container;

public class ContainerNotStartedException extends RuntimeException {
    public ContainerNotStartedException(String s, Throwable cause) {
        super(s, cause);
    }
}
