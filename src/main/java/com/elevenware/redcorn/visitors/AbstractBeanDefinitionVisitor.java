package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.EventListener;
import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.ConstructorModel;
import com.elevenware.redcorn.beans.DependencyResolutionException;
import com.elevenware.redcorn.container.RedcornContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractBeanDefinitionVisitor implements BeanDefinitionVisitor {

    private static final Logger log = LoggerFactory.getLogger(AbstractBeanDefinitionVisitor.class);

    public static BeanDefinitionVisitor simpleTypeInstantiator(final EventListener<BeanDefinition> listener) {
        return new AbstractBeanDefinitionVisitor() {
            @Override
            public void visit(BeanDefinition definition) {
                if(definition.getConstructorArgs().size() == 0 && definition.canInstantiate()) {
                    definition.instantiate();
                    listener.doNotify(definition);
                }
            }

        };
    }

    public static BeanDefinitionVisitor constructorArgsInstantiator(final RedcornContainer existing) {
        return new ConstructionArgumentsInstantiator(existing);
    }

    public void visitAll(Iterable<BeanDefinition> definitions) {
        for(BeanDefinition definition: definitions) {
            definition.accept(this);
        }
    }

    public static BeanDefinitionVisitor referenceConstructorArgs(final RedcornContainer iocContainer) {
        return new AbstractBeanDefinitionVisitor() {
            @Override
            public void visit(BeanDefinition definition) {
            }
        };
    }
}
