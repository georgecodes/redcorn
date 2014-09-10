package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.*;

public class ResolvableDependencyInstantiationOrdering implements ReferenceResolutionContext {
    private final List<ResolvableBeanDefinition> beans;
    private final Map<String, ResolvableBeanDefinition> nameToBean;
    private final Map<Class, ResolvableBeanDefinition> classToBean;
    private final List<Class<?>> types;

    public ResolvableDependencyInstantiationOrdering(List<ResolvableBeanDefinition> beans) {
        this.beans = beans;
        this.types = new ArrayList<>();
        this.nameToBean = new HashMap<>();
        this.classToBean = new HashMap<>();
        for(ResolvableBeanDefinition bean: beans) {
            types.add(bean.getType());
            nameToBean.put(bean.getName(), bean);
            classToBean.put(bean.getType(), bean);
        }
    }

    public List<ResolvableBeanDefinition> sort() {
        List<Class<?>> allTypes = getTypesFrom(beans);
        List<ResolvableBeanDefinition> sortedBeans = new ArrayList<>();
        assertAllSatisfiable(beans, allTypes);
        int i = 0;

        while(!beans.isEmpty()) {
            List<Class<?>> currentlyReady = getTypesFrom(sortedBeans);
            if( i >= beans.size() ) {
                i = 0;
            }
            ResolvableBeanDefinition bean = beans.get(i);

            if(bean.canInstantiate() && bean.isSatisfiedBy(getTypesFrom(sortedBeans))) {
                sortedBeans.add(bean);
                beans.remove(i);
            } else if(bean.canInstantiate() && bean.) {

            }
            i++;
        }
        return sortedBeans;
    }

    private void assertAllSatisfiable(List<ResolvableBeanDefinition> beans, List<Class<?>> allTypes) {
        for(ResolvableBeanDefinition bean: beans) {
            bean.setResolutionContext(this);
            bean.prepare();
            if(!bean.isSatisfiedBy(allTypes)) {
                throw new RuntimeException("I have no way of satisfying all dependencies for " + bean.getType());
            }
        }
    }

    private List<Class<?>> getTypesFrom(List<ResolvableBeanDefinition> beans) {
        List<Class<?>> types = new ArrayList<>();
        for(ResolvableBeanDefinition bean: beans) {
            types.add(bean.getType());
        }
        return types;
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
        ResolvableBeanDefinition bean = nameToBean.get(ref);
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
        return classToBean.get(clazz).getPayload();
    }
}
