package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.beans.ExtendedBeanDefinition;

public interface BeanDefinitionVisitor {

    void visit(ExtendedBeanDefinition definition);
    void visitAll(Iterable<ExtendedBeanDefinition> definitions);

}
