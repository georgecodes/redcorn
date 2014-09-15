package com.elevenware.redcorn.container;

import com.elevenware.redcorn.beans.DefaultBeanDefinition;
import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContainerResolutionContext implements ReferenceResolutionContext {
    private final Map<String, DefaultBeanDefinition> context;
    private final Map<Class<?>, DefaultBeanDefinition> classContext;

    public ContainerResolutionContext(Map<String, DefaultBeanDefinition> context,
                                      Map<Class<?>, DefaultBeanDefinition> classContext) {
        this.context = context;
        this.classContext = classContext;
    }

    @Override
    public Object resolve(String ref) {
        return context.get(ref).getPayload();
    }

    @Override
    public boolean canResolve(String ref) {
        return context.containsKey(ref);
    }

    @Override
    public Class<?> lookupType(String ref) {
        return context.get(ref).getType();
    }

    @Override
    public List<Class<?>> getContainedTypes() {
        return new ArrayList<Class<?>>(classContext.keySet());
    }

    @Override
    public Object resolveType(Class<?> clazz) {
        List<DefaultBeanDefinition> candidates = new ArrayList<>();
        for(Map.Entry<Class<?>, DefaultBeanDefinition> entry: classContext.entrySet()) {
            if(clazz.isAssignableFrom(entry.getKey())) {
                candidates.add(entry.getValue());
            }
        }
        if(candidates.size()==0) {
            throw new RuntimeException("Unresolvable");
        }
        if(candidates.size() > 1) {
            throw new RuntimeException("Too many candidates");
        }
        return candidates.get(0).getPayload();
    }
}
