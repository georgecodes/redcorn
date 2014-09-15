package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.container.ContainerResolutionContext;
import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.*;

public class ResolvableDependencyInstantiationOrdering {
    private final List<BeanDefinition> beans;
    private final Properties properties;

    public ResolvableDependencyInstantiationOrdering(List<BeanDefinition> beans) {
        this(beans, new Properties());
    }

    public ResolvableDependencyInstantiationOrdering(List<BeanDefinition> beans, Properties properties) {
        this.beans = beans;
        this.properties = properties;
    }

    public List<BeanDefinition> sort() {
        List<Class<?>> allTypes = getTypesFrom(beans);
        List<BeanDefinition> sortedBeans = new ArrayList<>();
        assertAllSatisfiable(beans, allTypes);
        int i = 0;
        SatisfactionChecker resolutionContext = new SatisfactionChecker(sortedBeans, properties);

        while(!beans.isEmpty()) {
            resolutionContext.initialise(sortedBeans);

            List<Class<?>> currentlyReady = getTypesFrom(sortedBeans);
            if( i >= beans.size() ) {
                i = 0;
            }
            BeanDefinition bean = beans.get(i);
            bean.setResolutionContext(resolutionContext);

            if(bean.canInstantiate() && bean.isSatisfied()) {
                sortedBeans.add(bean);
                beans.remove(i);
            }
            i++;
        }
        return sortedBeans;
    }

    private void assertAllSatisfiable(List<BeanDefinition> beans, List<Class<?>> allTypes) {
        ReferenceResolutionContext resolutionContext = new SatisfactionChecker(beans, properties);
        for(BeanDefinition bean: beans) {
            bean.setResolutionContext(resolutionContext);
            bean.prepare();
            if(!bean.isSatisfied()) {
                throw new RuntimeException("I have no way of satisfying all dependencies for " + bean.getType());
            }
        }
    }

    private List<Class<?>> getTypesFrom(List<BeanDefinition> beans) {
        List<Class<?>> types = new ArrayList<>();
        for(BeanDefinition bean: beans) {
            types.add(bean.getType());
        }
        return types;
    }

}
