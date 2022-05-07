package com.bnyte.easyhd.autoconfigure.scanner;

import com.bnyte.easyhd.autoconfigure.proxy.EasyHdFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Arrays;
import java.util.Set;

public class EasyHdClientScanner extends ClassPathBeanDefinitionScanner {

    private BeanDefinitionRegistry registry;

    public EasyHdClientScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        if (this.registry == null) {
            setRegistry(registry);
        }
        registerFilters();
    }

    public void setRegistry(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    /**
     * 重写过滤条件
     */
    public void registerFilters() {
        // 判断用户是否配置需要全局扫描
        addIncludeFilter(new EasyHdClientIncludeFilter());
//        addIncludeFilter(new AnnotationTypeFilter(RepostClient.class));

        addExcludeFilter(new EasyHdClientExcludeFilter());
    }


    /**
     * 重写扫描逻辑
     * @param basePackages 请求接口类所在的包路径，只能是第一层的包，不包含子包
     * @return BeanDefinitionHolder实例集合
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        // 扫描完成之后我们获得了所有需要注册的类
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        for (BeanDefinitionHolder beanDefinition : beanDefinitions) {

            registerProxy(registry, beanDefinition);

        }

        if (beanDefinitions.isEmpty()) {
            logger.warn("[Repost] No Forest client is found in package '" + Arrays.toString(basePackages) + "'.");
        }
        processBeanDefinitions(beanDefinitions);
        return beanDefinitions;
    }


    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (logger.isDebugEnabled()) {
                logger.debug("[Repost] Creating Forest Client Bean with name '" + holder.getBeanName()
                        + "' and Proxy of '" + definition.getBeanClassName() + "' client interface");
            }

            String beanClassName = definition.getBeanClassName();

            logger.info("[Repost] Created Repost Client Bean with name '" + holder.getBeanName()
                    + "' and Proxy of '" + beanClassName + "' client interface");

        }
    }

    public void registerProxy(BeanDefinitionRegistry registry, BeanDefinitionHolder beanDefinitionHolder) {
        String beanName = beanDefinitionHolder.getBeanName();
        String beanClassName = beanDefinitionHolder.getBeanDefinition().getBeanClassName();
        Class<?> clazz = null;
        try {
            clazz = Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        beanDefinition.setBeanClass(EasyHdFactoryBean.class);
        // 使用RepostFactoryBean类的构造器进行实例化
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(clazz);
        // 将对象注入到IOC容器当中
        if (registry.isBeanNameInUse(beanName)) {
            registry.removeBeanDefinition(beanName);
        }
        registry.registerBeanDefinition(beanName, beanDefinition);
    }


    /**
     * 这一行必须添加否则接口会被过滤掉
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        // 如果为真则会扫描所有，包括类，但是类会抛出异常，也就是动态代理失败
//        if (repostProperties.isAllInterfaces()) {
//            return true;
//        } else {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
//        }
    }
}
