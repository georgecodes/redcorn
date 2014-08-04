package com.elevenware.ioc.beans;

import com.elevenware.ioc.visitors.BeanDefinitionVisitor;

import java.util.List;

/**
 * Represents as abstract definition of a managed object in the container.
 */
public interface BeanDefinition {

    /**
     * Denotes whether or not this bean definition is in a state that it can
     * be instantiated by the container. This will depend on whether there is
     * a constructor that is entirely satisfied by all the configured
     * constructor arguments in the definition.
     *
     * e.g If no constructor arguments are configured, and there is a default constructor
     * then this method returns true.
     *
     * If there is a constructor argument configured, then there must exist a constructor
     * that is satisified by that argument, even if a less specific constructor exists
     *
     * @return boolean
     */
    boolean canInstantiate();

    /**
     * Returns the specific concrete type represented by this bean definition
     */
    void instantiate();

    /**
     * Actually instantiates the bean. If there are configured constructor arguments
     * then the constructor must be satisfied.
     *
     * @return java.lang.Class
     */
    Class getType();

    /**
     * Returns an instance of the concrete type represented by the bean definition. Whether
     * this is the same instance, or a new instance, is dependent on specific subclass implementations
     * and the scope they provide.
     *
     * @return java.lang.Object
     */
    Object getPayload();

    /**
     * Adds a constructor argument to this bean definition. The container will attempt to get
     * a constructor for <bold>all</bold> of the arguments defined in a bean. The container will
     * look for constructors whose types are <i>assignable</i> from the types of arguments,
     * not their exact class.
     *
     * @param object - the instance for configuration
     */
    BeanDefinition addContructorArg(Object object);

    /**
     * Returns the list of constructor arguments configured.
     * @return java.util.List
     */
    List<Object> getConstructorArgs();

    /**
     * Allows for visitation by {@link com.elevenware.ioc.visitors.BeanDefinitionVisitor}
     *
     * @param visitor
     */
    void accept(BeanDefinitionVisitor visitor);

    /**
     * Returns the {@link com.elevenware.ioc.beans.ConstructorModel} for this
     * definition.
     *
     * @return {@link com.elevenware.ioc.beans.ConstructorModel}
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

    String getName();

    BeanDefinition addConstructorRef(String reference);
    List<String> getConstructorRefs();
}
