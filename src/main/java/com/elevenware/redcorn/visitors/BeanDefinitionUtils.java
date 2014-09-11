package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.beans.BeanDefinition;

import java.util.Collection;

public class BeanDefinitionUtils {

    static Collection<Class<?>> typesFrom(Collection<BeanDefinition> beans) {
       return CollectionUtils.collectFrom(beans).collect(new Collector<BeanDefinition, Class<?>>() {

            @Override
            public Class<?> doCollect(BeanDefinition definition) {
                return definition.getType();
            }

        });
    }

}
