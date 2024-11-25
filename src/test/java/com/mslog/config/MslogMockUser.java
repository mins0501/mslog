package com.mslog.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MslogMockSecurityContext.class)
public @interface MslogMockUser {

    String name() default "mskym";
    String email() default "mins0501@gmail.com";
    String password() default "1234";
    String role() default "ROLE_ADMIN";

}
