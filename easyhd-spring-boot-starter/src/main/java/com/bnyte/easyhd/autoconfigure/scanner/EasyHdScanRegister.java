package com.bnyte.easyhd.autoconfigure.scanner;

import com.bnyte.easyhd.autoconfigure.annotation.EasyHdScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EasyHdScanRegister implements
        ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware, BeanFactoryAware {
    static Logger log = LoggerFactory.getLogger(EasyHdScanRegister.class);

    /**
     * 需要扫描的包集合
     */
    public static List<String> basePackages = new LinkedList<>();

    private BeanFactory beanFactory;
    private Environment environment;
    private ResourceLoader resourceLoader;

    public EasyHdScanRegister() {
    }

    /**
     * 获取所有的需要扫描的Bean包路径
     * @param importingClassMetadata 元数据
     * @param registry Spring的Bean注册工厂对象
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EasyHdScan.class.getName());
        if (annotationAttributes == null) {
            EasyHdScanRegister.basePackages.addAll(AutoConfigurationPackages.get(beanFactory));
        } else {
            AnnotationAttributes attributes =
                    AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EasyHdScan.class.getName()));

            /**
             * 获取basePackages属性
             */
            Arrays.stream(attributes.getStringArray("basePackages"))
                    .filter(StringUtils::hasText)
                    .peek(basePackage -> EasyHdScanRegister.basePackages.add(basePackage))
                    .collect(Collectors.toList());

            /**
             * 获取basePackageClasses属性
             */
            Arrays.stream(attributes.getClassArray("basePackageClasses"))
                    .peek(clazz -> EasyHdScanRegister.basePackages.add(ClassUtils.getPackageName(clazz)))
                    .collect(Collectors.toList());

            /**
             * 获取value属性
             */
            Arrays.stream(attributes.getStringArray("value"))
                    .filter(StringUtils::hasText)
                    .peek(basePackage -> EasyHdScanRegister.basePackages.add(basePackage))
                    .collect(Collectors.toList());
            log.info("[Repost] scanner basePackages have < " + EasyHdScanRegister.basePackages.toString() + " >");
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
