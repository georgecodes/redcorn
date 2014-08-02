package com.elevenware.ioc.visitors;

import com.elevenware.ioc.beans.BeanDefinition;

public interface BeanDefinitionVisitor {

    void visit(BeanDefinition definition);
    void visitAll(Iterable<BeanDefinition> definitions);

}
