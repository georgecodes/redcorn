package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.beans.ExtendedBeanDefinition;

import java.util.Collection;

public class BeanDefinitionUtils {

    static Collection<Class<?>> typesFrom(Collection<ExtendedBeanDefinition> beans) {
       return CollectionUtils.collectFrom(beans).collect(new Collector<ExtendedBeanDefinition, Class<?>>() {

            @Override
            public Class<?> doCollect(ExtendedBeanDefinition definition) {
                return definition.getType();
            }

        });
    }

}
