package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.model.ConcreteInjectableArgument;
import com.elevenware.redcorn.model.InjectableArgument;
import com.elevenware.redcorn.model.InjectableArgumentModel;
import com.elevenware.redcorn.model.ReferenceInjectableArgument;

import java.util.*;

public class BeanFilter {

    private Set<BeanDefinition> remainder = new HashSet<>();

    public List<BeanDefinition> filterByConcreteArgs(List<BeanDefinition> beans) {
        List<BeanDefinition> filtered = new ArrayList<>();
        for(BeanDefinition definition: beans) {
            InjectableArgumentModel model = definition.getInjectionModel();
            boolean toBeFiltered = model.getConstructorArgs().size() != 0;
            for(InjectableArgument arg: model.getConstructorArgs()) {
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

    public List<BeanDefinition> filterByReferenceArgs(List<BeanDefinition> beans) {
        List<BeanDefinition> filtered = new ArrayList<>();
        for(BeanDefinition definition: beans) {
            InjectableArgumentModel model = definition.getInjectionModel();
            boolean toBeFiltered = model.getConstructorArgs().size() != 0;
            for(InjectableArgument arg: model.getConstructorArgs()) {
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

    public List<BeanDefinition> filterByImplicitConstructorArgs(List<BeanDefinition> beans) {
        List<BeanDefinition> filtered = new ArrayList<>();
        Collection<Class<?>> typesAvailable = BeanDefinitionUtils.typesFrom(beans);
        for(final BeanDefinition definition: beans) {
            InjectableArgumentModel model = definition.getInjectionModel();
            if( model.getConstructorArgs().size() != 0) {
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

    public Set<BeanDefinition> getRemainder() {
        return remainder;
    }


}

