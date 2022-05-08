package com.bnyte.easyhd.core.bind;

import java.lang.annotation.*;

/**
 * @author bnyte
 * @since 2022/5/7 23:03
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FsDelete {

    /**
     * 删除文件夹文件夹路径(循环递归创建)
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

    /**
     * 是否循环递归删除, 默认不循环
     * @return 是否循环递归删除, 默认不循环
     */
    boolean recursive() default false;
}
