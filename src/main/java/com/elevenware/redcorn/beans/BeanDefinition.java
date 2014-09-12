package com.elevenware.redcorn.beans;

import com.elevenware.redcorn.model.ReferenceResolutionContext;
import com.elevenware.redcorn.model.ReferenceResolver;

public interface BeanDefinition extends ReferenceResolver {

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
     * Actually instantiates the bean. If there are configured constructor arguments
     * then the constructor must be satisfied.
     *
     * @return java.lang.Class
     */
    void instantiate();


    /**
     * Returns the specific concrete type represented by this bean definition
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

    void setResolutionContext(ReferenceResolutionContext resolutionContext);

    /**
     * Adds a constructor argument to this bean definition. The container will attempt to get
     * a constructor for <bold>all</bold> of the arguments defined in a bean. The container will
     * look for constructors whose types are <i>assignable</i> from the types of arguments,
     * not their exact class.
     *
     * @param object - the instance for configuration
     */
    BeanDefinition addConstructorArg(Object object);

    /**
     * Adds a constructor argument that explicitly references another definition, which will be
     * resolved later by containers. In the case of anonymous definitions, the fqn of the class
     * is the name.
     * @param other
     * @return
     */
    BeanDefinition addConstructorRef(String other);

    BeanDefinition referenceProperty(String propertyName, String reference);

    /**
     * Adds a constructor argument that explicitly references another definition, which will be
     * resolved later by containers. In the case of anonymous definitions, the fqn of the class
     * is the name. A type is also provided, to cover cases where the type cannot be inferred in advance
     * @param other
     * @return
     */
    BeanDefinition addConstructorRef(String other, Class<?> type);
    BeanDefinition addConstructorRef(Class<?> clazz);
    void prepare();
    String getName();
    boolean isSatisfied();

    BeanDefinition addProperty(String name, Object property);

}
