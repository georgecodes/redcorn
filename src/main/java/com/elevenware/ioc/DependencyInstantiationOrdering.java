package com.elevenware.ioc;

import com.elevenware.ioc.beans.BeanDefinition;
import com.elevenware.ioc.beans.ConstructorModel;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class DependencyInstantiationOrdering {

    private final List<BeanDefinition> beans;

    public DependencyInstantiationOrdering(List<BeanDefinition> beans) {
        this.beans = beans;
    }

    public List<Holder> getGreedyConstructorsFrom(List<BeanDefinition> beans, List<Class<?>> allTypes) {
        List<Holder> constructors  = new ArrayList<>();
        for(BeanDefinition bean: beans) {
            ConstructorModel model = bean.getConstructorModel();
            constructors.add(new Holder(model.findBestConstructorsForTypes(allTypes), bean));
        }
        return constructors;
    }

    private List<Class<?>> getTypesFrom(List<BeanDefinition> beans) {
        List<Class<?>> types = new ArrayList<>();
        for(BeanDefinition bean: beans) {
            types.add(bean.getType());
        }
        return types;
    }

    public List<BeanDefinition> sort() {
        List<Class<?>> allTypes = getTypesFrom(beans);
        List<BeanDefinition> sortedBeans = new ArrayList<>();
        assertAllSatisfiable(beans, allTypes);
        List<Holder> holders = getGreedyConstructorsFrom(beans, allTypes);
        int i = 0;
        while(!holders.isEmpty()) {
            List<Class<?>> currentlyReady = getTypesFrom(sortedBeans);
            if( i >= holders.size() ) {
                i = 0;
            }
            Holder holder = holders.get(i);
            if(holder.bean.getConstructorModel().findBestConstructorsForTypes(currentlyReady) != null) {
                sortedBeans.add(holder.bean);
                holders.remove(i);
            }
            i++;
        }
        return sortedBeans;
    }

    private void assertAllSatisfiable(List<BeanDefinition> beans, List<Class<?>> allTypes) {
        for(BeanDefinition bean: beans) {
            Constructor best = bean.getConstructorModel().findBestConstructorsForTypes(allTypes);
            if(best == null) {
                throw new RuntimeException("Could never satisfy dependencies for " + bean);
            }
        }
    }

    public static class Holder {
        Constructor best;
        BeanDefinition bean;

        public Holder(Constructor bestConstructorsForTypes, BeanDefinition bean) {
            this.best = bestConstructorsForTypes;
            this.bean = bean;
        }
    }

}
