package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.*;

public class ResolvableDependencyInstantiationOrdering {
    private final List<ResolvableBeanDefinition> beans;
    private final SatisfactionChecker eventuallySatisfiable;

    public ResolvableDependencyInstantiationOrdering(List<ResolvableBeanDefinition> beans) {
        this.beans = beans;
        this.eventuallySatisfiable = new SatisfactionChecker(beans);
    }

    public List<ResolvableBeanDefinition> sort() {
        List<Class<?>> allTypes = getTypesFrom(beans);
        List<ResolvableBeanDefinition> sortedBeans = new ArrayList<>();
        assertAllSatisfiable(beans, allTypes);
        int i = 0;


        while(!beans.isEmpty()) {
            eventuallySatisfiable.initialise(sortedBeans);
            List<Class<?>> currentlyReady = getTypesFrom(sortedBeans);
            if( i >= beans.size() ) {
                i = 0;
            }
            ResolvableBeanDefinition bean = beans.get(i);

            if(bean.canInstantiate() && bean.isSatisfied()) {
//            if(bean.canInstantiate() && bean.isSatisfiedBy(getTypesFrom(sortedBeans))) {
                sortedBeans.add(bean);
                beans.remove(i);
            }
            i++;
        }
        return sortedBeans;
    }

    private void assertAllSatisfiable(List<ResolvableBeanDefinition> beans, List<Class<?>> allTypes) {
        for(ResolvableBeanDefinition bean: beans) {
            bean.setResolutionContext(eventuallySatisfiable);
            bean.prepare();
            if(!bean.isSatisfied()) {
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

    private static class SatisfactionChecker implements ReferenceResolutionContext {

        private List<Class<?>> types;
        private Map<String, ResolvableBeanDefinition> nameToBean;
        private Map<Class, ResolvableBeanDefinition> classToBean;

        private SatisfactionChecker(List<ResolvableBeanDefinition> beans) {
           initialise(beans);
        }

        void initialise(List<ResolvableBeanDefinition> beans) {
            this.types = new ArrayList<>();
            this.nameToBean = new HashMap<>();
            this.classToBean = new HashMap<>();
            for(ResolvableBeanDefinition bean: beans) {
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
            List<ResolvableBeanDefinition> candidates = new ArrayList<>();
            for(Map.Entry<Class, ResolvableBeanDefinition> entry: classToBean.entrySet()) {
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
}
