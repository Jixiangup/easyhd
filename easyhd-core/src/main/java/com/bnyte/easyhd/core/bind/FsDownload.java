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
public @interface FsDownload {

    // 删除远程文件 源文件路径 下载路径 本地文件校验

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
     * 下载的文件需要保存到的本地文件地址
     * @return 下载的文件需要保存到的本地文件地址默认下载到本地文件的/${user.home}/easyhd
     */
    String local() default "";

    /**
     * 远程文件地址, 默认
     * @return 需要上传的远程文件地址
     */
    String remote();

    /**
     * 是否开启文件校验
     *
     * @return 是否开启文件校验 默认不开启
     */
    boolean useRawLocalFileSystem() default true;

    /**
     * 是否删除本地文件 默认不删除
     * @return 是否删除本地文件 默认不删除
     */
    boolean remove() default false;

}
