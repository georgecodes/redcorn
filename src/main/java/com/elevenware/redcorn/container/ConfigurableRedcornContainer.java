package com.elevenware.redcorn.container;

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

}
