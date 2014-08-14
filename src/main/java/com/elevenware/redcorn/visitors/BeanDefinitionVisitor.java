package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.beans.BeanDefinition;

public interface BeanDefinitionVisitor {

    void visit(BeanDefinition definition);
    void visitAll(Iterable<BeanDefinition> definitions);

}
