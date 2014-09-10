package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.EventListener;
import com.elevenware.redcorn.beans.BeanDefinition;
import com.elevenware.redcorn.beans.DefaultBeanDefinition;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class TestVisitors {

    @Test
    public void simpleBeanInstantiationVisitor() {

        EventListener<BeanDefinition> mockListener = mock(EventListener.class);
        BeanDefinitionVisitor visitor = AbstractBeanDefinitionVisitor.simpleTypeInstantiator(mockListener);
        BeanDefinition simpleBean = mock(DefaultBeanDefinition.class);
        List<BeanDefinition> beans = new ArrayList<>();
        beans.add(simpleBean);

        when(simpleBean.getConstructorArgs()).thenReturn(Collections.emptyList());
        when(simpleBean.canInstantiate()).thenReturn(true);

        doCallRealMethod().when(simpleBean).accept(visitor);

        visitor.visitAll(beans);

        verify(mockListener).doNotify(simpleBean);

    }

}
