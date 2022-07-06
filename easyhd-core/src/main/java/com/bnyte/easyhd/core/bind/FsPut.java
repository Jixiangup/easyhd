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
public @interface FsPut {

    /**
     * hdfs name node Api 地址
     *
     * @return hdfs name node Api 地址
     */
    String address() default "localhost:8020";

    /**
     * 需要上传的本地文件地址
     * @return 需要上传的本地文件绝对路径
     */
    String local() default "";

    /**
     * 需要上传的远程文件地址, 默认/easyhd/${filename}
     * @return 需要上传的远程文件地址
     */
    String remote() default "";

    /**
     * 是否覆盖远程文件
     *
     * @return 是否覆盖远程文件 默认覆盖
     */
    boolean overwrite() default true;

    /**
     * 是否删除本地文件 默认不删除
     * @return 是否删除本地文件 默认不删除
     */
    boolean remove() default false;

    /**
     * 操作用户 默认root
     * @return 操作用户 默认root
     */
    String user() default "root";

}
