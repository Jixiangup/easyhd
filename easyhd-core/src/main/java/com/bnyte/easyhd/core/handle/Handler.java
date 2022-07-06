package com.bnyte.easyhd.core.handle;

/**
 * @author bnyte
 * @since 1.0.0
 */
public interface Handler {

    /**
     * 构建器
     */
    Handler build();

    /**
     * 执行器
     */
    Object execute();

    /**
     * 执行完毕之后的后置响应结果处理，通过在接口指定的返回结果类型做转换
     * @param object 执行器执行结果
     * @return 返回响应处理结果
     */
    Object postResponseProcessing(Object object);


}
