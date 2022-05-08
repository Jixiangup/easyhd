# easyhd

对`Hadoop`组件的`API`封装, 操作`Hadoop`简单调用定义一个`接口`、一个`方法`就可以完成操作。

# 快速开始

## Easyhd Spring Boot Starter

- latest = 1.0.0
- `latest`表示为推荐使用版本
- maven
```
<dependency>
    <groupId>com.bnyte</groupId>
    <artifactId>easyhd-spring-boot-starter</artifactId>
    <version>${latest}</version>
</dependency>
```
- gradle
```
implementation group: 'com.bnyte', name: 'easyhd-spring-boot-starter', version: ${latest}
```

## 使用

- 在启动器上添加`easyhd包扫描`
```
@SpringBootApplication
@EasyHdScan("com.bnyte.easyhd.hadoop.client") // 扫描EasyHd所有客户端，会循环所有子包
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```

- 定义一个接口作为客户端
> 注意该接口需要被`@EasyHdScan()`配置的包路径包含
```
public interface HdfsClient {

    /**
     * 上传测试
     */
    @FsPut(
            local = "${filepath}",
            address = "hdfs://hadoop100:8020",
            user = "root"
    )
    String put(@Var("filepath") String filepath);
}
```

- 调用测试
```
@RestController
@RequestMapping("/hdfs")
public class HdfsController {

    @Autowired
    HdfsClient hdfsClient;

    @GetMapping("/put")
    String put(@RequestParam(value = "filepath", required = false) String filepath) {
        return hdfsClient.put(filepath);
    }

}
```