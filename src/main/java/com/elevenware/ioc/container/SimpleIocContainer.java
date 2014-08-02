package com.elevenware.ioc.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleIocContainer implements IocContainer {

    private boolean started = false;
    private List<Class> registeredTypes;
    private Map<Class, Object> context;

    public SimpleIocContainer() {
        registeredTypes = new ArrayList<>();
        context = new HashMap<>();
    }

    @Override
    public void register(Class clazz) {
        registeredTypes.add(clazz);
    }

    @Override
    public <T> T find(Class<T> clazz) {
        checkStarted();
        return (T) context.get(clazz);
    }

    private void checkStarted() {
        if(!started) {
            throw new ContainerNotStartedException();
        }
    }

    @Override
    public void start() {
        for(Class clazz: registeredTypes) {
            try {
                Object object = clazz.newInstance();
                context.put(clazz, object);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        started = true;
    }
}
