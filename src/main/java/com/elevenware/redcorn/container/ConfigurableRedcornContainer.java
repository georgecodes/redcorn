package com.elevenware.redcorn.container;

import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.DefaultBeanDefinition;
import com.elevenware.redcorn.beans.ResolvableDependencyInstantiationOrdering;
import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConfigurableRedcornContainer extends AbstractRedcornContainer {


    private final Properties properties;

    public ConfigurableRedcornContainer(Properties properties) {
        this.properties = properties;
    }

    public ConfigurableRedcornContainer(String filename) {
        this(new Properties());
        try {
            Reader reader = new FileReader(filename);
            properties.load(reader);
        } catch (FileNotFoundException e) {
            throw new ContainerNotStartedException("Was unable to find properties file " + filename, e);
        } catch (IOException e) {
           throw new ContainerNotStartedException("Was unable to load properties file " + filename, e);
        }
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
