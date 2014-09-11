package com.elevenware.redcorn.container;

import com.elevenware.redcorn.NamedBean;
import com.elevenware.redcorn.SimpleBean;
import org.junit.Test;

import static org.junit.Assert.*;
public class TestContainerHierarchies {

    @Test
    public void childContainersInheritFromParentButDoNotPassBeansBack() {

        RedcornContainer parentContainer = new ConstructorInjectionRedcornContainer();
        parentContainer.register("parent", NamedBean.class).addConstructorArg("I am in the parent container");
        parentContainer.start();
        RedcornContainer childContainer = parentContainer.createChild("sub");
        assertNotNull(childContainer);
        childContainer.register("child", NamedBean.class).addConstructorArg("I am in the child container");

        childContainer.start();

        SimpleBean parent = childContainer.get("parent");
        SimpleBean child = childContainer.get("child");


        assertEquals("I am in the parent container", parent.getName());
        assertEquals("I am in the child container", child.getName());

        assertNull(parentContainer.get("child"));

    }

}
