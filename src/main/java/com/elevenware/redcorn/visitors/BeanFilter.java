package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.beans.ExtendedBeanDefinition;
import com.elevenware.redcorn.model.ConcreteInjectableArgument;
import com.elevenware.redcorn.model.InjectableArgument;
import com.elevenware.redcorn.model.ConstructorInjectionModel;
import com.elevenware.redcorn.model.ReferenceInjectableArgument;

import java.util.*;

public class BeanFilter {

    private Set<ExtendedBeanDefinition> remainder = new HashSet<>();

    public List<ExtendedBeanDefinition> filterByConcreteArgs(List<ExtendedBeanDefinition> beans) {
        List<ExtendedBeanDefinition> filtered = new ArrayList<>();
        for(ExtendedBeanDefinition definition: beans) {
            ConstructorInjectionModel model = definition.getInjectionModel();
            boolean toBeFiltered = model.getArguments().size() != 0;
            for(InjectableArgument arg: model.getArguments()) {
                if(!(ConcreteInjectableArgument.class.isAssignableFrom(arg.getClass()))) {
                    toBeFiltered = false;
                }
            }
            if(toBeFiltered) {
                filtered.add(definition);
            } else {
                remainder.add(definition);
            }
        }
        return filtered;
    }

    public List<ExtendedBeanDefinition> filterByReferenceArgs(List<ExtendedBeanDefinition> beans) {
        List<ExtendedBeanDefinition> filtered = new ArrayList<>();
        for(ExtendedBeanDefinition definition: beans) {
            ConstructorInjectionModel model = definition.getInjectionModel();
            boolean toBeFiltered = model.getArguments().size() != 0;
            for(InjectableArgument arg: model.getArguments()) {
                if(!(ReferenceInjectableArgument.class.isAssignableFrom(arg.getClass()))) {
                    toBeFiltered = false;
                }
            }
            if(toBeFiltered) {
                filtered.add(definition);
            } else {
                remainder.add(definition);
            }
        }
        return filtered;
    }

    public List<ExtendedBeanDefinition> filterByImplicitConstructorArgs(List<ExtendedBeanDefinition> beans) {
        List<ExtendedBeanDefinition> filtered = new ArrayList<>();
        Collection<Class<?>> typesAvailable = BeanDefinitionUtils.typesFrom(beans);
        for(final ExtendedBeanDefinition definition: beans) {
            ConstructorInjectionModel model = definition.getInjectionModel();
            if( model.getArguments().size() != 0) {
                remainder.add(definition);
                continue;
            }


            if(definition.getConstructorModel().findBestConstructorsForTypes(typesAvailable) != null) {
                filtered.add(definition);
            } else {
                remainder.add(definition);
            }

        }
        return filtered;
    }

    public Set<ExtendedBeanDefinition> getRemainder() {
        return remainder;
    }


}

