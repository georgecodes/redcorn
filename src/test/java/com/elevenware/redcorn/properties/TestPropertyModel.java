package com.elevenware.redcorn.properties;

import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.model.PropertyModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPropertyModel {

    @Test
    public void modelsProperties() {

        PropertyModel model = new PropertyModel(SimpleBean.class);
        assertTrue(model.hasProperty("name"));

    }

    @Test
    public void providesProperty() {

        String name = "some name";
        PropertyModel model = new PropertyModel(SimpleBean.class);
        PropertyModel.PropertyDefinition property = model.getProperty("name");
        SimpleBean bean = new SimpleBean();
        property.setProperty(bean, name);

        assertEquals(name, bean.getName());

    }

}
