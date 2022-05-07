package com.bnyte.easyhd.autoconfigure.proxy;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author bnyte
 *  FactoryBean是一种特殊的对象，它是一个工厂Bean，它返回的不是某一个实例，而是一类对象，它最大的作用是能让我们自定义Bean的创建过程，注意与BeanFactory区分，理解FactoryBean更有助于我们理解Spring的编程思想。
 * @version 1.0.0
 */
public class EasyHdFactoryBean<T> implements FactoryBean<T>{

    private Class<T> interfaceType;

    private String name;

    private String contextId;

    public EasyHdFactoryBean(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    /**
     * 生成代理对象
     * @return 这里会把生成的代理对象返回
     */
    @Override
    public T getObject() throws Exception {
        InvocationHandler handler = new EasyHdProxyHandler<>(interfaceType);
        T instance =
                (T) Proxy.newProxyInstance(
                        interfaceType.getClassLoader(),
                        new Class[] {interfaceType},
                        handler);
        return instance;
    }

    /**
     * 返回bean的类型
     */
    @Override
    public Class<T> getObjectType() {
        return interfaceType;
    }

    /**
     * 当从IOC拿bean的时候spring会判断当前bean是不是单例，就是调用的这个方法
     * @return TRUE是单例，FALSE不是单例
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    public Class<T> getType() {
        return interfaceType;
    }
    public void setType(Class<T> type) {
        this.interfaceType = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContextId() {
        return contextId;
    }
    public void setContextId(String contextId) {
        this.contextId = contextId;
    }
}
