package com.elevenware.ioc.container;

import com.elevenware.ioc.SimpleBean;
import org.junit.Test;

import static org.junit.Assert.*;
public class TestContainerHierarchies {

    @Test
    public void childContainersInheritFromParentButDoNotPassBeansBack() {

        IocContainer parentContainer = new ConstructorInjectionIocContainer();
        parentContainer.register("parent", SimpleBean.class).addProperty("name", "I am in the parent container");
        parentContainer.start();
        IocContainer childContainer = parentContainer.createChild();
        assertNotNull(childContainer);
        childContainer.register("child", SimpleBean.class).addProperty("name", "I am in the child container");

        childContainer.start();

        SimpleBean parent = childContainer.get("parent");
        SimpleBean child = childContainer.get("child");


        assertEquals("I am in the parent container", parent.getName());
        assertEquals("I am in the child container", child.getName());

        assertNull(parentContainer.get("child"));

    }

}
