package com.elevenware.redcorn.container;

import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.ConstructorModel;
import com.elevenware.redcorn.container.ConstructorInjectionRedcornContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigurableRedcornContainer extends ConstructorInjectionRedcornContainer {


    private final Properties properties;

    public ConfigurableRedcornContainer(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void start() {
//        for(BeanDefinition definition: registeredTypes) {
//           int position = 0;
//           List<ConstructorArgument> args = new ArrayList<>();
//           for(String ref: definition.getConstructorRefs()) {
//                if(properties.containsKey(ref)) {
//                    replaceConstructorRefWithValue(definition, ref, properties.get(ref), position++, refs.size());
//                }
//            }
//        }
        super.start();
    }

    private void replaceConstructorRefWithValue(BeanDefinition definition, String ref, Object value, int position, int size) {
        ConstructorModel constructorModel = definition.getConstructorModel();
//        constructorModel.?

        definition.addContructorArg(value);

    }
}
