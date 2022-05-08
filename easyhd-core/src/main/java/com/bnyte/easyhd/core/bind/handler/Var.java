package com.bnyte.easyhd.core.bind.handler;

import java.lang.annotation.*;

/**
 * @author bnyte
 * @since 2022/5/8 15:47
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Var {

    String value();

}
