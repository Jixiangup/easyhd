package com.bnyte.easyhd.autoconfigure.annotation;

import com.bnyte.easyhd.autoconfigure.scanner.EasyHdScanRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(EasyHdScanRegister.class)
public @interface EasyHdScan {

    String[] basePackages() default {};

    String[] value() default {};

    Class<?>[] basePackageClasses() default {};
}
