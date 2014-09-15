package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SatisfactionChecker implements ReferenceResolutionContext {

    private List<Class<?>> types;
    private Map<String, DefaultBeanDefinition> nameToBean;
    private Map<Class, DefaultBeanDefinition> classToBean;

    SatisfactionChecker(List<DefaultBeanDefinition> beans) {
       initialise(beans);
    }

    void initialise(List<DefaultBeanDefinition> beans) {
        this.types = new ArrayList<>();
        this.nameToBean = new HashMap<>();
        this.classToBean = new HashMap<>();
        for(DefaultBeanDefinition bean: beans) {
            types.add(bean.getType());
            nameToBean.put(bean.getName(), bean);
            classToBean.put(bean.getType(), bean);
        }
    }

    @Override
    public Object resolve(String ref) {
        return nameToBean.get(ref).getPayload();
    }

    @Override
    public boolean canResolve(String ref) {
        return nameToBean.containsKey(ref);
    }

    @Override
    public Class<?> lookupType(String ref) {
        DefaultBeanDefinition bean = nameToBean.get(ref);
        if(bean != null) {
            return bean.getType();
        }
        return null;
    }

    @Override
    public List<Class<?>> getContainedTypes() {
        return types;
    }

    @Override
    public Object resolveType(Class<?> clazz) {
        List<DefaultBeanDefinition> candidates = new ArrayList<>();
        for(Map.Entry<Class, DefaultBeanDefinition> entry: classToBean.entrySet()) {
            if(clazz.isAssignableFrom(entry.getKey())) {
                candidates.add(entry.getValue());
            }
        }
        if(candidates.size()==0) {
            throw new RuntimeException("Unresolvab;e");
        }
        if(candidates.size() > 1) {
            throw new RuntimeException("Too many candidates");
        }
        return candidates.get(0).getPayload();
    }
}
