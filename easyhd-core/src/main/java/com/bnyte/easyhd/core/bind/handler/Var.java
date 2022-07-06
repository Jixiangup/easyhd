package com.bnyte.easyhd.core.bind.handler;

import java.lang.annotation.*;

/**
 * @author bnyte
 * @since 1.0.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Var {

    String value();

}
