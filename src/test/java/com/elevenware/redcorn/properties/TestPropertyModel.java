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

        PropertyModel model = new PropertyModel(SimpleBean.class);


    }

}
