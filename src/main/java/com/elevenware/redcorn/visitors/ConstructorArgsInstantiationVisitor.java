package com.elevenware.redcorn.visitors;

import com.elevenware.redcorn.beans.BeanDefinition;

public class ConstructorArgsInstantiationVisitor implements BeanDefinitionVisitor {
    @Override
    public void visit(BeanDefinition definition) {

    }

    @Override
    public void visitAll(Iterable<BeanDefinition> definitions) {

    }

    private void pointlessCodeChangeToTestCloudbeesIntegration() {
        this should now fail the build
    }
}
