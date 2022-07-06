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
 * @since 1.0.0
 */
public class PropertiesUtils {

    /**
     * 需要解析的字符模板串参数渲染准备
     * @param args 参数列表
     * @param method 被执行的方法
     * @return 返回准备好的属性对象
     */
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

}
