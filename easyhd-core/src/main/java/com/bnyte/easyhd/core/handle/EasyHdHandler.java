package com.bnyte.easyhd.core.handle;

import com.bnyte.easyhd.core.exception.ClientNotFoundException;
import com.bnyte.easyhd.core.handle.impl.HdfsHandler;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author bnyte
 * @since 2022/5/8 14:21
 */
public interface EasyHdHandler {

    /**
     * 构建器
     */
    void build();

    /**
     * 执行器
     */
    void execute();

    static EasyHdHandler loader(Method method, Object[] args) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (HdfsHandler.hdfs.contains(annotation.annotationType())) {
                return new HdfsHandler(method, args);
            }
        }
        throw new ClientNotFoundException();
    }


}
