package com.bnyte.easyhd.autoconfigure.scanner;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

public class EasyHdBeanRegister implements ResourceLoaderAware, BeanPostProcessor {

    private ResourceLoader resourceLoader;

    private final ConfigurableApplicationContext applicationContext;

    public EasyHdBeanRegister(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public EasyHdClientScanner registerScanner() {
        List<String> basePackages = EasyHdScanRegister.basePackages;
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getBeanFactory();

        EasyHdClientScanner scanner = new EasyHdClientScanner(registry);
        // this check is needed in Spring 3.1
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        if (CollectionUtils.isEmpty(basePackages)) {
            return scanner;
        }
        scanner.doScan(StringUtils.toStringArray(basePackages));

        return scanner;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
