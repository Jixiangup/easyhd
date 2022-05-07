package com.bnyte.easyhd.core.bind;

import java.lang.annotation.*;

/**
 * @author bnyte
 * @since 2022/5/7 23:03
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FsPut {

    String address() default "localhost:8020";

    String local();

    String remote() default "";

    boolean overwrite() default true;

    boolean remove() default true;

}
