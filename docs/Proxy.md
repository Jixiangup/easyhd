# 基于`SpringBoot`,`Spring`,`JDK Proxy`动态代理

# 代码分析

## 功能前瞻

- 配置启动类

```java
@SpringBootApplication
@EasyHdScan("com.bnyte.easyhd.hadoop.client") // 配置需要扫描的接口所在包路径,添加之后会扫描包路径下的所有独立且没有实现类的独立接口
public class Main {
    public static void main(String[] args){
      SpringApplication.run(Main.class, args);
    }
}
```

- 创建接口

```java
package com.bnyte.easyhd.hadoop.client; // 注意包路径必须是@EasyHdScan的子包

public interface HdfsClient {

        /**
         * 上传测试
         */
        @FsPut(
                local = "${filepath}" // 指定本地文件路径
        )
        String put(@Var("filepath") String filepath);

}
```

- 执行测试

```java
@RestController
@RequestMapping("/hdfs")
public class HdfsController {

    @Autowired
    HdfsClient hdfsClient;

    @GetMapping("/put")
    String put(@RequestParam("filepath") String filepath) {
        return hdfsClient.put(filepath); // 上传结果    }

}
```

## 源码解析

> 下面解析是按照程序执行流程进行解析的, 如果不是很了解`SpringBoot`以及`Spring`的话请按照顺序酌情查阅

### 名词介绍

- @EasyHdScan

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(EasyHdScanRegister.class) // 提前加载EasyHdScanRegister(这个功能依赖了spring 如果脱离了spring就没法完了)
public @interface EasyHdScan {

    String[] basePackages() default {};

    String[] value() default {};

    Class<?>[] basePackageClasses() default {};
}
```

- EasyHdScanRegister
```java
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

        assert attributes != null;
        /*
         * 将配置的basePackages添加到包路径集合中
         */
        basePackages.addAll(
            Arrays.stream(attributes.getStringArray("basePackages"))
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toList())
        );

        /*
         * 获取basePackageClasses属性
         */
        basePackages.addAll(
            Arrays.stream(attributes.getClassArray("basePackageClasses"))
                    .map(ClassUtils::getPackageName)
                    .collect(Collectors.toList())
        );

        /*
         * 获取value属性
         */
        basePackages.addAll(
                Arrays.stream(attributes.getStringArray("value"))
                        .filter(StringUtils::hasText)
                        .collect(Collectors.toList())
        );
        ;
        log.info("[EasyHd] scanner basePackages have < " + EasyHdScanRegister.basePackages.toString() + " >");
    }
}
```

- spring.factories

`spring.factories`文件是`SpringBoot`框架给我们的功能特性,包含了非常多的功能,最常用的就是`AutoConfiguration`

### 流程解析

- spring.factories

看`easyhd-spring-boot-starter`包下的`resources/META-INF/spring.factories`文件
```
# \是换行
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.bnyte.easyhd.autoconfigure.auto.EasyHdAutoConfiguration

# 上面代码的另一种写法
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.bnyte.easyhd.autoconfigure.auto.EasyHdAutoConfiguration
```
从上面的配置可以看到这个位置就是将`EasyHdAutoConfiguration`自动配置了

- EasyHdAutoConfiguration

```java
@Configuration
//@Import({EasyHdScanRegister.class}) // 开启后不添加EasyHdScan也会将所有接口添加到IOC容器中
public class EasyHdAutoConfiguration {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    /**
     * 这个位置会将EasyHdBeanRegister注册到容器中,看看注册容器的实现逻辑
     */
    @Bean
    public EasyHdBeanRegister repostBeanRegister() {
        EasyHdBeanRegister easyHdBeanRegister = new EasyHdBeanRegister(applicationContext);
        easyHdBeanRegister.registerScanner();
        return easyHdBeanRegister;
    }
}
```

- easyHdBeanRegister.registerScanner()方法调用

```java
public EasyHdClientScanner registerScanner() {
    // 获取所有需要扫描的包路径
    List<String> basePackages = EasyHdScanRegister.basePackages;
    
    BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getBeanFactory();

    // easyhd客户端扫描器
    EasyHdClientScanner scanner = new EasyHdClientScanner(registry);
    // this check is needed in Spring 3.1
    if (resourceLoader != null) {
        scanner.setResourceLoader(resourceLoader);
    }

    if (CollectionUtils.isEmpty(basePackages)) {
        return scanner;
    }
    // 执行扫描包路径并配置动态代理
    scanner.doScan(StringUtils.toStringArray(basePackages));

    return scanner;
}
```

- EasyHdClientScanner

> 主要看几个比较核心的方法

```java
public EasyHdClientScanner(BeanDefinitionRegistry registry) {
    super(registry, false); // 很关键 关闭spring默认bean注册逻辑，我们后面要重写，所以不用他的 他会屏蔽掉接口的注册
    if (this.registry == null) {
        setRegistry(registry);
    }
    registerFilters(); // 对扫描到的所有资源添加过滤条件
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
        logger.warn("[EasyHd] No EasyHd client is found in package '" + Arrays.toString(basePackages) + "'.");
    }
    processBeanDefinitions(beanDefinitions);
    return beanDefinitions;
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
    // 指定当前bean的class
    beanDefinition.setBeanClass(EasyHdFactoryBean.class);
    // 使用EasyHdFactoryBean类的构造器进行实例化
    assert clazz != null;
//        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(clazz);
    
    // 也可以用其他方式比如
    beanDefinition.getPropertyValues().add("interfaceType", clazz); // 推荐使用这种方法
    
    // 将对象注入到IOC容器当中, 校验对象是否已经被注册 如果已经被注册删掉 让他用我们的代理对象
    if (registry.isBeanNameInUse(beanName)) {
        registry.removeBeanDefinition(beanName);
    }
    
    // 注入对象
    registry.registerBeanDefinition(beanName, beanDefinition);
}
```


