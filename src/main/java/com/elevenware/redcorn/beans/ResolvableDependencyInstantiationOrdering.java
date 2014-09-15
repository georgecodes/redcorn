package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.container.ContainerResolutionContext;
import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.*;

public class ResolvableDependencyInstantiationOrdering {
    private final List<DefaultBeanDefinition> beans;
    private final ReferenceResolutionContext eventuallySatisfiable;

    public ResolvableDependencyInstantiationOrdering(List<DefaultBeanDefinition> beans, ReferenceResolutionContext resolutionContext) {
        this.beans = beans;
        this.eventuallySatisfiable = resolutionContext;
    }

    public List<DefaultBeanDefinition> sort() {
        List<Class<?>> allTypes = getTypesFrom(beans);
        List<DefaultBeanDefinition> sortedBeans = new ArrayList<>();
        assertAllSatisfiable(beans, allTypes);
        int i = 0;
        SatisfactionChecker resolutionContext = new SatisfactionChecker(sortedBeans);

        while(!beans.isEmpty()) {
            resolutionContext.initialise(sortedBeans);

            List<Class<?>> currentlyReady = getTypesFrom(sortedBeans);
            if( i >= beans.size() ) {
                i = 0;
            }
            DefaultBeanDefinition bean = beans.get(i);
            bean.setResolutionContext(resolutionContext);
//            bean.prepare();

            if(bean.canInstantiate() && bean.isSatisfied()) {
//            if(bean.canInstantiate() && bean.isSatisfiedBy(getTypesFrom(sortedBeans))) {
                sortedBeans.add(bean);
                beans.remove(i);
            }
            i++;
        }
        return sortedBeans;
    }

    private void assertAllSatisfiable(List<DefaultBeanDefinition> beans, List<Class<?>> allTypes) {
        ReferenceResolutionContext resolutionContext = new SatisfactionChecker(beans);
        for(DefaultBeanDefinition bean: beans) {
            bean.setResolutionContext(resolutionContext);
            bean.prepare();
            if(!bean.isSatisfied()) {
                throw new RuntimeException("I have no way of satisfying all dependencies for " + bean.getType());
            }
        }
    }

    private List<Class<?>> getTypesFrom(List<DefaultBeanDefinition> beans) {
        List<Class<?>> types = new ArrayList<>();
        for(DefaultBeanDefinition bean: beans) {
            types.add(bean.getType());
        }
        return types;
    }

}
