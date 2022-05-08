package com.bnyte.easyhd.core.uitl;

import com.bnyte.easyhd.core.bind.handler.Var;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author bnyte
 * @since 2022/5/8 16:05
 */
public class PropertiesUtils {

    public static Properties render(List<Object> args, Method method) {
        Properties properties = new Properties();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Var var = parameters[i].getAnnotation(Var.class);
            if (Objects.nonNull(var)) {
                properties.put(var.value(), args.get(i));
            }
        }
        return properties;
    }

    public void test(@Var("1") String a, @Var("1") String b) {

    }

}
