package com.elevenware.redcorn.properties;

import com.elevenware.redcorn.model.ConcreteInjectableArgument;
import com.elevenware.redcorn.model.InjectableArgument;
import com.elevenware.redcorn.model.ReferenceInjectableArgument;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPropertyInjectionModel  {

    @Test
    public void canAddAllPrimitives() {

        PropertyInjectionModel model = new PropertyInjectionModel();
        model.addProperty("theBoolean", new ConcreteInjectableArgument(true));
        model.addProperty("theChar", new ConcreteInjectableArgument('x'));
        model.addProperty("theShort", new ConcreteInjectableArgument((short) 3));
        model.addProperty("theInt", new ConcreteInjectableArgument(17));
        model.addProperty("theLong", new ConcreteInjectableArgument(947L));
        model.addProperty("theFloat", new ConcreteInjectableArgument(485.399f));
        model.addProperty("theDouble", new ConcreteInjectableArgument(3.1415927D));
        model.addProperty("theString", new ConcreteInjectableArgument("how you doin'?"));

        InjectableArgument booleanArgument = model.getProperty("theBoolean");
        InjectableArgument charArgument = model.getProperty("theChar");
        InjectableArgument shortArgument = model.getProperty("theShort");
        InjectableArgument intArgument = model.getProperty("theInt");
        InjectableArgument longArgument = model.getProperty("theLong");
        InjectableArgument floatArgument = model.getProperty("theFloat");
        InjectableArgument doubleArgument = model.getProperty("theDouble");
        InjectableArgument stringArgument = model.getProperty("theString");

        assertEquals(true, booleanArgument.getPayload());
        assertEquals('x', charArgument.getPayload());
        assertEquals((short) 3, shortArgument.getPayload());
        assertEquals(17, intArgument.getPayload());
        assertEquals(947L, longArgument.getPayload());
        assertEquals(485.399f, floatArgument.getPayload());
        assertEquals(3.1415927D, doubleArgument.getPayload());
        assertEquals("how you doin'?", stringArgument.getPayload());

    }

}
