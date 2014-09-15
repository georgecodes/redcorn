package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.*;

public class SatisfactionChecker implements ReferenceResolutionContext {

    private final Properties properties;
    private List<Class<?>> types;
    private Map<String, BeanDefinition> nameToBean;
    private Map<Class, BeanDefinition> classToBean;

    SatisfactionChecker(List<BeanDefinition> beans) {
       this(beans, new Properties());
    }

    public SatisfactionChecker(List<BeanDefinition> beans, Properties properties) {
        initialise(beans);
        this.properties = properties;
    }

    void initialise(List<BeanDefinition> beans) {
        this.types = new ArrayList<>();
        this.nameToBean = new HashMap<>();
        this.classToBean = new HashMap<>();
        for(BeanDefinition bean: beans) {
            types.add(bean.getType());
            nameToBean.put(bean.getName(), bean);
            classToBean.put(bean.getType(), bean);
        }
    }

    @Override
    public Object resolve(String ref) {
        return properties.containsKey(ref) ? properties.get(ref) : nameToBean.get(ref);
    }

    @Override
    public boolean canResolve(String ref) {
        boolean canResolve = properties.containsKey(ref);
        if(!canResolve) {
            return nameToBean.containsKey(ref);
        }
        return true;
    }

    @Override
    public Class<?> lookupType(String ref) {
        BeanDefinition bean = nameToBean.get(ref);
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
        List<BeanDefinition> candidates = new ArrayList<>();
        for(Map.Entry<Class, BeanDefinition> entry: classToBean.entrySet()) {
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
