package com.elevenware.redcorn.container;

import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.DefaultBeanDefinition;
import com.elevenware.redcorn.beans.ResolvableDependencyInstantiationOrdering;
import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConfigurableRedcornContainer extends AbstractRedcornContainer {


    private final Properties properties;

    public ConfigurableRedcornContainer(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected List<BeanDefinition> sort(List<BeanDefinition> beans) {
        ResolvableDependencyInstantiationOrdering ordering = new ResolvableDependencyInstantiationOrdering(beans, properties);
        return ordering.sort();
    }

    @Override
    protected ReferenceResolutionContext createResolutionContext(Map<String, BeanDefinition> context,
                                                                 Map<Class<?>, BeanDefinition> classContext) {
       return new OverridableReferenceResolutionContext(properties, new ContainerResolutionContext(context, classContext));

    }

}
