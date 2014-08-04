package com.elevenware.ioc.visitors;

import com.elevenware.ioc.EventListener;
import com.elevenware.ioc.beans.BeanDefinition;
import com.elevenware.ioc.beans.ConstructorModel;
import com.elevenware.ioc.container.ConstructorInjectionIocContainer;
import com.elevenware.ioc.container.IocContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                Map<String, BeanDefinition> resolvedBeans = new HashMap<>();
                for(BeanDefinition dependency: existing.getBeanDefinitions()) {
                    types.add(dependency.getType());
                    resolvedBeans.put(dependency.getName(), dependency);
                }

                if(resolvedBeans.keySet().containsAll(definition.getConstructorRefs()) && !definition.getConstructorRefs().isEmpty() ) {
                    for(String ref: definition.getConstructorRefs()) {
                        definition.addContructorArg(resolvedBeans.get(ref).getPayload());
                    }
                    definition.instantiate();
                    existing.addDefinition(definition);
                    return;
                }

                Constructor best = model.findBestConstructorsForTypes(types, definition.getConstructorArgs());
                if( best != null ) {
                    List<BeanDefinition> potential = new ArrayList<>();
                    for(Class type: best.getParameterTypes()) {
                        for(BeanDefinition def: existing.getBeanDefinitions()) {
                            if(type.isAssignableFrom(def.getType())) {
                                potential.add(def);
                            }
                        }
                        switch(potential.size()) {
                            case 0:
                                break;
                            case 1:
                                definition.addContructorArg(potential.get(0).getPayload());
                                break;
                            default:
                                throw new RuntimeException("Attempted to infer correct dependency for " + definition.getType() + " but found more than one potential dependency. Consider using named constructor references");
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

    public static BeanDefinitionVisitor referenceConstructorArgs(final IocContainer iocContainer) {
        return new Visitors() {
            @Override
            public void visit(BeanDefinition definition) {
            }
        };
    }
}
