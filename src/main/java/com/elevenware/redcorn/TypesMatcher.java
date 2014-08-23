package com.elevenware.redcorn;

/**
 * @author George McIntosh <george@elevenware.com>
 * @version 1.0
 * @since 20th August 2014
 *
 * Utility class for seeing if sets of classes or objects match. Useful if seeing if
 * a candidate for reflective invocation is suitable.
 */
public class TypesMatcher {

    private final Class[] myTypes;

    public TypesMatcher(Class... myTypes) {
        this.myTypes = myTypes;
    }

    public boolean matchesClasses(Class... classes) {
        if(classes == null) {
            return false;
        }
        if(classes.length != myTypes.length) {
            return false;
        }

        for(Class a: classes) {
            boolean thisMatches = false;
            for(Class b: myTypes) {
                if(a == null) {
                    thisMatches = true;;
                } else
                if(b.isAssignableFrom(a)) {
                    thisMatches = true;
                }
            }
            if(!thisMatches) {
                return false;
            }
        }
        return true;
    }

    public boolean matchesClassesInOrder(Class...classes) {
        if(classes == null) {
            return false;
        }
        if(classes.length != myTypes.length) {
            return false;
        }
        int len = myTypes.length;
        for(int i = 0; i < len; i++) {
            Class a = classes[i];
            Class b = myTypes[i];
            if(a == null) {
                continue;
            }
            if(!b.isAssignableFrom(a)) {
                return false;
            }
        }
        return true;
    }

    public boolean matchesObjects(Object... objects) {

        return matchesClasses(getClassesFromObjects(objects));
    }

    public boolean matchesObjectsInOrder(Object... objects) {
        if(objects == null) {
            return false;
        }

        return matchesClassesInOrder(getClassesFromObjects(objects));
    }

    private Class[] getClassesFromObjects(Object...objects) {

        Class[] classes = new Class[objects.length];
        for(int i = 0; i < classes.length; i++) {
            Object object = objects[i];
            if(object != null) {
                classes[i] = object.getClass();
            }
        }
        return classes;
    }
}
