package com.elevenware.redcorn.container;

import com.elevenware.redcorn.beans.DefaultBeanDefinition;
import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.Map;
import java.util.Properties;

public class ConfigurableRedcornContainer extends AbstractRedcornContainer {


    private final Properties properties;

    public ConfigurableRedcornContainer(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected ReferenceResolutionContext createResolutionContext(Map<String, DefaultBeanDefinition> context,
                                                                 Map<Class<?>, DefaultBeanDefinition> classContext) {
       return new OverridableReferenceResolutionContext(properties, new ContainerResolutionContext(context, classContext));

    }

}
