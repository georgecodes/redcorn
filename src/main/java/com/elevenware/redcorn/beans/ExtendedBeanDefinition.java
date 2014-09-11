package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.ConstructorInjectionModel;
import com.elevenware.redcorn.model.ReferenceResolutionContext;
import com.elevenware.redcorn.visitors.BeanDefinitionVisitor;

import java.util.List;

/**
 * Represents as abstract definition of a managed object in the container.
 */
public interface ExtendedBeanDefinition extends BeanDefinition {

    /**
     * Returns the list of constructor arguments configured.
     * @return java.util.List
     */
    List<Object> getConstructorArgs();

    /**
     * Allows for visitation by {@link com.elevenware.redcorn.visitors.BeanDefinitionVisitor}
     *
     * @param visitor
     */
    void accept(BeanDefinitionVisitor visitor);

    /**
     * Returns the {@link com.elevenware.redcorn.beans.ConstructorModel} for this
     * definition.
     *
     * @return {@link com.elevenware.redcorn.beans.ConstructorModel}
     */
    ConstructorModel getConstructorModel();

    /**
     * Returns whether or not all the dependencies of this definition
     * have been resolved.
     */
     boolean isResolved();

    /**
     * Marks this bean as having all dependencies resolved.
     */
     void markResolved();

    List<String> getConstructorRefs();

    ExtendedBeanDefinition addProperty(String name, Object value);

    ExtendedBeanDefinition reference(String other);

    boolean canHydrate();

    ExtendedBeanDefinition resolve(String name, Object dependency);

    ConstructorInjectionModel getInjectionModel();

    void inflateConstructorArgs();
}
