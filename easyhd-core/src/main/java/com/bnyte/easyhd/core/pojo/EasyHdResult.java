package com.bnyte.easyhd.core.pojo;

import com.bnyte.easyhd.core.pojo.hdfs.EmptyStateResult;

import java.io.Serializable;

/**
 * 基础响应
 */
public class EasyHdResult<T> implements Serializable {

    private static final long serialVersionUID = 94264L;

    /**
     * 当前执行状态
     */
    private Boolean state;

    /**
     * 执行成功
     */
    private String message = "success";

    /**
     * 执行结果数据
     */
    private T data;

    /**
     * 执行成功无数据
     * @return 执行成功无数据
     */
    public static EasyHdResult<EmptyStateResult> execute(Boolean state) {
        EasyHdResult<EmptyStateResult> response = new EasyHdResult<>();
        response.setState(true);
        response.setData(new EmptyStateResult(state));
        return response;
    }

    /**
     * 执行成功无数据
     * @return 执行成功无数据
     */
    public static EasyHdResult<Void> ok() {
        EasyHdResult<Void> response = new EasyHdResult<>();
        response.setState(true);
        return response;
    }

    /**
     * 执行成功无数据
     * @return 执行成功无数据
     */
    public static <T> EasyHdResult<T> ok(T data) {
        EasyHdResult<T> response = new EasyHdResult<>();
        response.setState(true);
        response.setData(data);
        return response;
    }

    public EasyHdResult() {
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EasyHdResponse{" +
                "state=" + state +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
