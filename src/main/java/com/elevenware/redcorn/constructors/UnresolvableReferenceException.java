package com.elevenware.redcorn.constructors;

public class UnresolvableReferenceException extends RuntimeException {
    public UnresolvableReferenceException(String ref) {
        super("Was unable to locate reference property '" + ref + "'");
    }
}
