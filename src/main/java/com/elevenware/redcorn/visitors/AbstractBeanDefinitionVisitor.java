package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.EventListener;
import com.elevenware.redcorn.beans.ExtendedBeanDefinition;
import com.elevenware.redcorn.container.RedcornContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBeanDefinitionVisitor implements BeanDefinitionVisitor {

    private static final Logger log = LoggerFactory.getLogger(AbstractBeanDefinitionVisitor.class);

    public static BeanDefinitionVisitor simpleTypeInstantiator(final EventListener<ExtendedBeanDefinition> listener) {
        return new AbstractBeanDefinitionVisitor() {
            @Override
            public void visit(ExtendedBeanDefinition definition) {
                if(definition.getConstructorArgs().size() == 0 && definition.canInstantiate()) {
                    definition.instantiate();
                    listener.doNotify(definition);
                }
            }

        };
    }


    public void visitAll(Iterable<ExtendedBeanDefinition> definitions) {
        for(ExtendedBeanDefinition definition: definitions) {
            definition.accept(this);
        }
    }

    public static BeanDefinitionVisitor referenceConstructorArgs(final RedcornContainer iocContainer) {
        return new AbstractBeanDefinitionVisitor() {
            @Override
            public void visit(ExtendedBeanDefinition definition) {
            }
        };
    }
}
