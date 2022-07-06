package com.bnyte.easyhd.core.bind;

import java.lang.annotation.*;

/**
 * @author bnyte
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FsMkdir {

    /**
     * 创建文件夹路径(循环递归创建)
     *
     * @return 创建文件夹路径
     */
    String folder();

    /**
     * hdfs name node Api 地址
     *
     * @return hdfs name node Api 地址
     */
    String address() default "localhost:8020";

    /**
     * 操作用户 默认root
     * @return 操作用户 默认root
     */
    String user() default "root";
}
