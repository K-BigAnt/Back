package com.bigant.gaeme.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Authorized {

    AuthRole authRole() default AuthRole.MEMBER;

    enum AuthRole {
        MEMBER,
        ADMIN
    }

}
