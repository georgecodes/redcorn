package com.elevenware.redcorn.config;

import com.elevenware.redcorn.AllPrimitives;
import com.elevenware.redcorn.container.ConfigurableRedcornContainer;
import com.elevenware.redcorn.container.RedcornContainer;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestPropertiesFileConfiguration {

    @Test
    public void simpleCheck() {

        Properties properties = new Properties();

        properties.setProperty("server.port", "8009");

        RedcornContainer container = new ConfigurableRedcornContainer(properties);
        container.register(SimpleServer.class)
                .addConstructorRef("server.port", int.class);

        container.start();

        SimpleServer server = container.get(SimpleServer.class);
        assertEquals(8009, server.getPort());

    }

    @Test
    public void loadsFromReader() {

        RedcornContainer container = new ConfigurableRedcornContainer("src/test/resources/simpleserver.properties");
        container.register(SimpleServer.class)
                .addConstructorRef("server.port", int.class);

        container.start();

        SimpleServer server = container.get(SimpleServer.class);
        assertEquals(9100, server.getPort());

    }

    @Test
    public void primitivesAreHandled() {

        Properties properties = new Properties();
        properties.setProperty("theBoolean", "true");
        properties.setProperty("theShort", "9");
        properties.setProperty("theInt", "39");
        properties.setProperty("theLong", "66");
        properties.setProperty("theDouble", "38.3873");
        properties.setProperty("theFloat", "273.39");
        properties.setProperty("theChar", "x");
        properties.setProperty("theString", "Hello");

        RedcornContainer container = new ConfigurableRedcornContainer(properties);
        container.register(AllPrimitives.class)
                .addConstructorRef("theBoolean", Boolean.TYPE)
                .addConstructorRef("theShort", Short.TYPE)
                .addConstructorRef("theInt", Integer.TYPE)
                .addConstructorRef("theLong", Long.TYPE)
                .addConstructorRef("theDouble", Double.TYPE)
                .addConstructorRef("theFloat", Float.TYPE)
                .addConstructorRef("theChar", Character.TYPE)
                .addConstructorRef("theString", String.class);

        container.start();

        AllPrimitives all = container.get(AllPrimitives.class);

        assertEquals(true, all.isTheBoolean());
        assertEquals((short) 9, all.getTheShort());
        assertEquals(39, all.getTheInt());
        assertEquals(66L, all.getTheLong());
        assertEquals(38.3873d, all.getTheDouble(), 4);
        assertEquals(273.39f, all.getTheFloat(), 2);
        assertEquals('x', all.getTheChar());
        assertEquals("Hello", all.getTheString());
    }

    @Test
    public void primitivesAreCast() {

        Properties properties = new Properties();
        properties.setProperty("theBoolean", "true");
        properties.setProperty("theShort", "9");
        properties.setProperty("theInt", "39");
        properties.setProperty("theLong", "66");
        properties.setProperty("theDouble", "38.3873");
        properties.setProperty("theFloat", "273.39");
        properties.setProperty("theChar", "x");
        properties.setProperty("theString", "Hello");

        RedcornContainer container = new ConfigurableRedcornContainer(properties);
        container.register(AllPrimitives.class)
                .addConstructorRef("theBoolean", Boolean.class)
                .addConstructorRef("theShort", Short.class)
                .addConstructorRef("theInt", Integer.class)
                .addConstructorRef("theLong", Long.class)
                .addConstructorRef("theDouble", Double.class)
                .addConstructorRef("theFloat", Float.class)
                .addConstructorRef("theChar", Character.class)
                .addConstructorRef("theString", String.class);

        container.start();

        AllPrimitives all = container.get(AllPrimitives.class);

        assertEquals(true, all.isTheBoolean());
        assertEquals((short) 9, all.getTheShort());
        assertEquals(39, all.getTheInt());
        assertEquals(66L, all.getTheLong());
        assertEquals(38.3873d, all.getTheDouble(), 4);
        assertEquals(273.39f, all.getTheFloat(), 2);
        assertEquals('x', all.getTheChar());
        assertEquals("Hello", all.getTheString());
    }

    @Test
    public void systemPropertiesOverrideProperties() {

        Properties properties = new Properties();
        properties.setProperty("server.port", "8009");

        System.setProperty("server.port", "8010");

        RedcornContainer container = new ConfigurableRedcornContainer(properties);
        container.register(SimpleServer.class)
                .addConstructorRef("server.port", int.class);

        container.start();

        SimpleServer server = container.get(SimpleServer.class);
        assertEquals(8010, server.getPort());

    }

}
