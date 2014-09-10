package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.ConstructorModel;
import com.elevenware.redcorn.beans.DependencyResolutionException;
import com.elevenware.redcorn.container.RedcornContainer;
import com.elevenware.redcorn.model.InjectableArgument;
import com.elevenware.redcorn.model.ReferenceResolutionContext;

import java.lang.reflect.Constructor;
import java.util.*;

public class ConstructionArgumentsInstantiator extends AbstractBeanDefinitionVisitor {

    private final RedcornContainer existing;

    public ConstructionArgumentsInstantiator(RedcornContainer existing) {
        this.existing = existing;
    }

    @Override
    public void visit(BeanDefinition definition) {
        if(definition.isResolved()) {
            return;
        }
        ConstructorModel model = definition.getConstructorModel();
        final Map<String, BeanDefinition> resolvedBeans = new HashMap<>();

        List<Class<?>> availableTypes = extractTypesFrom(existing.getBeanDefinitions());

        gatherResolved(existing.getBeanDefinitions(), resolvedBeans);

        // resolve all references
        if(resolvedBeans.keySet().containsAll(definition.getConstructorRefs()) && !definition.getConstructorRefs().isEmpty() ) {
            for(String ref: definition.getConstructorRefs()) {
                definition.addContructorArg(resolvedBeans.get(ref).getPayload());
            }
            definition.instantiate();
            existing.addDefinition(definition);
            definition.markResolved();
            return;
        }

        // resolve all references by calling Resolution context
        definition.setResolutionContext(new ReferenceResolutionContext() {
            @Override
            public Object resolve(String ref) {
                return resolvedBeans.get(ref).getPayload();
            }

            @Override
            public boolean canResolve(String ref) {
                return resolvedBeans.containsKey(ref);
            }

            @Override
            public Class<?> lookupType(String ref) {
                BeanDefinition bean = resolvedBeans.get(ref);
                if(bean != null) {
                    return bean.getType();
                }
                return null;
            }

            @Override
            public List<Class<?>> getContainedTypes() {
                List<Class<?>> types = new ArrayList<Class<?>>();
                for(BeanDefinition beanDefinition: resolvedBeans.values()) {
                    types.add(beanDefinition.getType());
                }
                return types;
            }

            @Override
            public Object resolveType(Class<?> clazz) {
                return null;
            }
        });

        definition.inflateConstructorArgs();

        Constructor best = model.findBestConstructorsForTypes(availableTypes, definition.getConstructorArgs());
        if( best != null ) {


            for(Class type: best.getParameterTypes()) {
                List<BeanDefinition> potential = new ArrayList<>();
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

    private List<Class<?>> extractTypesFrom(Collection<BeanDefinition> beanDefinitions) {
        List<Class<?>> types = new ArrayList<>();
        for(BeanDefinition definition:beanDefinitions) {
            types.add(definition.getType());
        }
        return types;
    }

    private void gatherResolved(Collection<BeanDefinition> beanDefinitions, Map<String, BeanDefinition> resolvedBeans) {
        for(BeanDefinition dependency: existing.getBeanDefinitions()) {
            resolvedBeans.put(dependency.getName(), dependency);
        }
    }

}

