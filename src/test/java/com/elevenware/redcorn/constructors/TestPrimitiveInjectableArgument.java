package com.elevenware.redcorn.constructors;

import com.elevenware.redcorn.model.ConcreteInjectableArgument;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPrimitiveInjectableArgument {

    @Test
    public void acceptsPrimitiveBoolean() {

        boolean val = false;
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(boolean.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsPrimitiveChar() {

        char val = 'x';
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(char.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsPrimitiveShort() {

        short val = (short) 39;
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(short.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsPrimitiveInt() {

        int val = 48;
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(int.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsPrimitiveFloat() {

        float val = 48.440f;
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(float.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsPrimitiveDouble() {

        double val = 48.3948d;
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(double.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsPrimitiveLong() {

        long val = 48L;
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(long.class));
        assertEquals(val, primitive.getPayload());

    }


    @Test
    public void acceptsBoolean() {

        Boolean val = Boolean.TRUE;
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(Boolean.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsCharacter() {

        Character val = Character.valueOf('i');
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(Character.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsShort() {

        Short val = Short.parseShort("34");
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(Short.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsInteger() {

        Integer val = Integer.valueOf(7);
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(Integer.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsFloat() {

        Float val = Float.valueOf(48.440f);
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(Float.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsDouble() {

        Double val = Double.valueOf(48.3948d);
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(Double.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsLong() {

        Long val = Long.valueOf(48L);
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(Long.class));
        assertEquals(val, primitive.getPayload());

    }

    @Test
    public void acceptsString() {

        String val = "The string";
        ConcreteInjectableArgument primitive = new ConcreteInjectableArgument(val);

        assertTrue(primitive.compatibleWith(String.class));
        assertEquals(val, primitive.getPayload());

    }

}
