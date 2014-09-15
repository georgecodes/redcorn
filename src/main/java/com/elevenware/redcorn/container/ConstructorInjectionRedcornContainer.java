package com.elevenware.redcorn.container;

import com.elevenware.redcorn.beans.DefaultBeanDefinition;
import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.util.Map;

public class ConstructorInjectionRedcornContainer extends AbstractRedcornContainer {


    @Override
    protected ReferenceResolutionContext createResolutionContext(Map<String, DefaultBeanDefinition> context,
                                                                 Map<Class<?>, DefaultBeanDefinition> classContext) {
        return new ContainerResolutionContext(context, classContext);
    }

}
