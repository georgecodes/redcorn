package com.elevenware.redcorn.properties;

import com.elevenware.redcorn.model.InjectableArgument;
import com.elevenware.redcorn.model.ReferenceResolutionContext;
import com.elevenware.redcorn.model.ReferenceResolver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PropertyInjectionModel implements ReferenceResolver, Iterable<Map.Entry<String, InjectableArgument>> {

    private ReferenceResolutionContext context;
    private Map<String, InjectableArgument> arguments;

    public PropertyInjectionModel() {
        arguments = new HashMap<>();
    }

    @Override
    public void setResolutionContext(ReferenceResolutionContext context) {
        this.context = context;
    }

    public void addProperty(String name, InjectableArgument injectableArgument) {
        arguments.put(name, injectableArgument);
    }

    public InjectableArgument getProperty(String name) {
        return arguments.get(name);
    }

    @Override
    public Iterator<Map.Entry<String,InjectableArgument>> iterator() {
        return arguments.entrySet().iterator();
    }
}
