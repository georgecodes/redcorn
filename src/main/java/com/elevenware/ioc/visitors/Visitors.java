package com.elevenware.ioc.visitors;

import com.elevenware.ioc.EventListener;
import com.elevenware.ioc.beans.BeanDefinition;
import com.elevenware.ioc.beans.ConstructorModel;
import com.elevenware.ioc.container.IocContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public abstract class Visitors implements BeanDefinitionVisitor {

    private static final Logger log = LoggerFactory.getLogger(Visitors.class);

    public static BeanDefinitionVisitor simpleTypeInstantiator(final EventListener<BeanDefinition> listener) {
        return new Visitors() {
            @Override
            public void visit(BeanDefinition definition) {
                if(definition.getConstructorArgs().size() == 0 && definition.canInstantiate()) {
                    definition.instantiate();
                    listener.doNotify(definition);
                }
            }

        };
    }

    public static BeanDefinitionVisitor constructorArgsInstantiator(final IocContainer existing) {
        return new Visitors() {
            @Override
            public void visit(BeanDefinition definition) {
                if(definition.isResolved()) {
                    return;
                }
               ConstructorModel model = definition.getConstructorModel();

                List<Class<?>> types = new ArrayList<>();
                for(BeanDefinition dependency: existing.getBeanDefinitions()) {
                    types.add(dependency.getType());
                }
                Constructor best = model.findBestConstructorsForTypes(types, definition.getConstructorArgs());
                if( best != null ) {
                    for(Class type: best.getParameterTypes()) {
                        for(BeanDefinition def: existing.getBeanDefinitions()) {
                            if(type.isAssignableFrom(def.getType())) {
                                definition.addContructorArg(def.getPayload());
                            }
                        }
                    }
                    definition.instantiate();
                    existing.addDefinition(definition);
                }
            }

        };
    }

    public void visitAll(Iterable<BeanDefinition> definitions) {
        for(BeanDefinition definition: definitions) {
            definition.accept(this);
        }
    }

}
