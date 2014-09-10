package com.elevenware.redcorn.constructors;

import com.elevenware.redcorn.SimpleBean;
import com.elevenware.redcorn.model.ReferenceInjectableArgument;
import com.elevenware.redcorn.model.ReferenceResolutionContext;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestReferenceInjectableArgument {

    @Test
    public void findsReferenceOk() {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);
        when(context.resolve("findableref")).thenReturn(new SimpleBean());

        ReferenceInjectableArgument argument = new ReferenceInjectableArgument("findableref", SimpleBean.class);
        argument.setContext(context);

        argument.inflate();

        assertNotNull(argument.getPayload());

    }

    @Test( expected = UnresolvableReferenceException.class)
    public void throwsExceptionIfReferenceNotResolvable() {

        ReferenceResolutionContext context = mock(ReferenceResolutionContext.class);

        ReferenceInjectableArgument argument = new ReferenceInjectableArgument("unfindableref", SimpleBean.class);
        argument.setContext(context);

        argument.inflate();

    }

}
