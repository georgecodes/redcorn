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

    public static BeanDefinitionVisitor constructorArgsInstantiator(final RedcornContainer existing) {
        return new Visitors() {
            @Override
            public void visit(BeanDefinition definition) {
                if(definition.isResolved()) {
                    return;
                }
               ConstructorModel model = definition.getConstructorModel();

                List<Class<?>> types = new ArrayList<Class<?>>();
                Map<String, BeanDefinition> resolvedBeans = new HashMap<String, BeanDefinition>();
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
                    definition.markResolved();
                    return;
                }

                Constructor best = model.findBestConstructorsForTypes(types, definition.getConstructorArgs());
                if( best != null ) {

                    for(Class type: best.getParameterTypes()) {
                        List<BeanDefinition> potential = new ArrayList<BeanDefinition>();
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
                    definition.markResolved();
                } else {
                    throw new DependencyResolutionException("Have no way to instantiate " + definition.getName());
                }

            }

        };
    }

    public void visitAll(Iterable<BeanDefinition> definitions) {
        for(BeanDefinition definition: definitions) {
            definition.accept(this);
        }
    }

    public static BeanDefinitionVisitor referenceConstructorArgs(final RedcornContainer iocContainer) {
        return new Visitors() {
            @Override
            public void visit(BeanDefinition definition) {
            }
        };
    }
}
