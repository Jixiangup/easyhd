package com.bnyte.easyhd.autoconfigure.proxy;

import com.bnyte.easyhd.core.handle.EasyHdHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author bnyte
 * @version 1.0.0
 */
public class EasyHdProxyHandler<T> implements InvocationHandler {

    static Logger log = LoggerFactory.getLogger(EasyHdProxyHandler.class);

    private Class<T> interfaceType;


    public EasyHdProxyHandler(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    /**
     * 当这个类被实例化之后会回调这个方法
     * @param proxy 动态代理对象
     * @param method 当前对象执行的方法
     * @param args 请求参数
     * @return 方法执行之后的结果
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            EasyHdHandler hdHandler = EasyHdHandler.loader(method, args);
            hdHandler.build();
            Object execute = hdHandler.execute();
            return hdHandler.postResponseProcessing(execute);
        }
    }

}
