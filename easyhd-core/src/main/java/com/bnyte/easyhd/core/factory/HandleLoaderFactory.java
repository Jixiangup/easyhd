package com.bnyte.easyhd.core.factory;

import com.bnyte.easyhd.core.exception.ClientNotFoundException;
import com.bnyte.easyhd.core.handle.Handler;
import com.bnyte.easyhd.core.handle.impl.HdfsHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author bnyte
 * @since 1.0.0
 */
public abstract class HandleLoaderFactory {

    /**
     * 通过被执行的目标方法加载对应的处理器
     * @param method 被执行的目标方法
     * @param args 被执行的目标方法形参值列表
     * @return 返回具体的处理器
     */
    public static Handler loader(Method method, Object[] args) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (HdfsHandler.hdfs.contains(annotation.annotationType())) {
                return new HdfsHandler(method, args);
            }
        }
        throw new ClientNotFoundException();
    }

}
