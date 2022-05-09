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
    Object execute();

    /**
     * 执行完毕之后的后置响应结果处理，通过在接口指定的返回结果类型做转换
     * @param object 执行器执行结果
     * @return 返回响应处理结果
     */
    Object postResponseProcessing(Object object);

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
