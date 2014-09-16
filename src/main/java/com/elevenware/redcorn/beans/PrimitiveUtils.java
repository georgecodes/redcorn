package com.elevenware.redcorn.beans;

public class PrimitiveUtils {

    private static final Class[] BOOLEAN_TYPES = { Boolean.class, Boolean.TYPE };
    private static final Class[] SHORT_TYPE = { Short.class, Short.TYPE };
    private static final Class[] INT_TYPES = { Integer.class, Integer.TYPE };
    private static final Class[] LONG_TYPES = { Long.class, Long.TYPE };
    private static final Class[] FLOAT_TYPES = { Float.class, Float.TYPE };
    private static final Class[] DOUBLE_TYPES = { Double.class, Double.TYPE };
    private static final Class[] CHARACTER_TYPES = { Character.class, Character.TYPE };

    public static boolean areBothBoolean(Class first, Class second) {
        return areMatched(first, second, BOOLEAN_TYPES);
    }

    public static boolean areBothShort(Class first, Class second) {
        return areMatched(first, second, SHORT_TYPE);
    }
    public static boolean areBothInteger(Class first, Class second) {
        return areMatched(first, second, INT_TYPES);
    }
    public static boolean areBothLong(Class first, Class second) {
        return areMatched(first, second, LONG_TYPES);
    }
    public static boolean areBothFloat(Class first, Class second) {
        return areMatched(first, second, FLOAT_TYPES);
    }
    public static boolean areBothDouble(Class first, Class second) {
        return areMatched(first, second, DOUBLE_TYPES);
    }
    public static boolean areBothCharacter(Class first, Class second) {
        return areMatched(first, second,CHARACTER_TYPES);
    }


    private static boolean areMatched(Class first, Class second, Class[] primitives) {
        if(!inThere(first, primitives)) {
            return false;
        }
        return inThere(second, primitives);
    }

    private static boolean inThere(Class candidate, Class[] primitives) {
        for(Class clazz: primitives) {
            if(candidate.equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    public static boolean areBothSamePrimitive(Class clazz, Class next) {
        if(areBothBoolean(clazz, next)) {
            return true;
        }
        if(areBothCharacter(clazz, next)) {
            return true;
        }
        if(areBothDouble(clazz, next)) {
            return true;
        }
        if(areBothFloat(clazz, next)) {
            return true;
        }
        if(areBothInteger(clazz, next)) {
            return true;
        }
        if(areBothShort(clazz, next)) {
            return true;
        }
        if(areBothLong(clazz, next)) {
            return true;
        }
        return false;
    }
}
