package com.bnyte.easyhd.core.handle.impl;

import com.bnyte.easyhd.core.bind.FsDelete;
import com.bnyte.easyhd.core.bind.FsDownload;
import com.bnyte.easyhd.core.bind.FsMkdir;
import com.bnyte.easyhd.core.bind.FsPut;
import com.bnyte.easyhd.core.client.HdfsClient;
import com.bnyte.easyhd.core.exception.ClientNotFoundException;
import com.bnyte.easyhd.core.exception.OperateMethodException;
import com.bnyte.easyhd.core.execute.HdfsActuator;
import com.bnyte.easyhd.core.handle.Handler;
import com.bnyte.easyhd.core.json.gson.GsonRender;
import com.bnyte.easyhd.core.pojo.EasyHdResult;
import com.bnyte.easyhd.core.render.HdfsRender;
import org.apache.hadoop.fs.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author bnyte
 * @since 1.0.0
 */
public class HdfsHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(HdfsHandler.class);
    public static final List<Class<? extends Annotation>> hdfs;

    static {
        hdfs = Stream.of(
                    FsPut.class,
                    FsDelete.class,
                    FsDownload.class,
                    FsMkdir.class
                )
                .collect(Collectors.toList());
    }

    private Method method;
    private HdfsClient hdfsClient;
    private List<Object> args;

    public HdfsHandler(Method method, Object[] args) {
        this.method = method;
        this.args = List.of(args);
    }

    @Override
    public Handler build() {
        setHdfsClient(renderHdfsClient());
        return this;
    }

    @Override
    public Object execute() {
        FileSystem fileSystem = HdfsRender.build(this.getHdfsClient());
        try {
            switch (this.getHdfsClient().getMethod()) {
                case DOWNLOAD:
                    return HdfsActuator.executeDownload(fileSystem, this.getHdfsClient());
                case MKDIR:
                    return HdfsActuator.executeMkdir(fileSystem, this.getHdfsClient());
                case PUT:
                    return HdfsActuator.executePut(fileSystem, this.getHdfsClient());
                case MOVE:
                    return HdfsActuator.executeMove(fileSystem, this.getHdfsClient());
                case DELETE:
                    return HdfsActuator.executeDelete(fileSystem, this.getHdfsClient());
                default:throw new OperateMethodException("HDFS 客户端操作方式未找到");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(fileSystem)) {
                try {
                    fileSystem.close();
                } catch (IOException e) {
                    log.error("close hdfs file system error {}", e.getMessage(), e);
                }
            }
        }
        return null;
    }

    @Override
    public Object postResponseProcessing(Object object) {
        if (Objects.isNull(object)) return null;

        return convertResponseResult(object);

    }

    private Object convertResponseResult(Object object) {
        Method method = this.getMethod();
        Class<?> invokeReturnType = method.getReturnType();

        if (invokeReturnType.getTypeName().equals(Void.class.getTypeName())) return null;

        // 构建easyhd响应结果
        if (invokeReturnType.getTypeName().equals(EasyHdResult.class.getTypeName())) {
            if (object instanceof Boolean) return EasyHdResult.ok(object);
        }

        // 响应结果和接口响应结果相同，直接返回
        if (invokeReturnType.getTypeName().equals(object.getClass().getTypeName())) return object;

        // 响应字符串
        if (invokeReturnType.getTypeName().equals(String.class.getTypeName())) {
            EasyHdResult<Object> result = EasyHdResult.ok(object);
            return GsonRender.toString(result);
        }

        // 指定了方法返回值
        EasyHdResult<Object> result = EasyHdResult.ok(object);
        return GsonRender.toJava(GsonRender.toString(result), invokeReturnType);

    }

    private HdfsClient renderHdfsClient() {
        FsPut fsPut = getMethod().getAnnotation(FsPut.class);
        if (Objects.nonNull(fsPut)) return HdfsRender.renderPut(fsPut, this);

        FsMkdir fsMkdir = getMethod().getAnnotation(FsMkdir.class);
        if (Objects.nonNull(fsMkdir)) return HdfsRender.renderMkdir(fsMkdir, this);

        FsDelete fsDelete = getMethod().getAnnotation(FsDelete.class);
        if (Objects.nonNull(fsDelete)) return HdfsRender.renderDelete(fsDelete, this);

        FsDownload fsDownload = getMethod().getAnnotation(FsDownload.class);
        if (Objects.nonNull(fsDownload)) return HdfsRender.renderDownload(fsDownload, this);

        throw new ClientNotFoundException();
    }

    public HdfsClient getHdfsClient() {
        return hdfsClient;
    }

    public void setHdfsClient(HdfsClient hdfsClient) {
        this.hdfsClient = hdfsClient;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }
}
