package com.bnyte.easyhd.core.handle.impl;

import com.bnyte.easyhd.core.bind.FsDelete;
import com.bnyte.easyhd.core.bind.FsDownload;
import com.bnyte.easyhd.core.bind.FsMkdir;
import com.bnyte.easyhd.core.bind.FsPut;
import com.bnyte.easyhd.core.client.HdfsClient;
import com.bnyte.easyhd.core.exception.ClientNotFoundException;
import com.bnyte.easyhd.core.exception.OperateMethodException;
import com.bnyte.easyhd.core.execute.HdfsExecute;
import com.bnyte.easyhd.core.handle.EasyHdHandler;
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
 * @since 2022/5/8 14:33
 */
public class HdfsHandler implements EasyHdHandler {

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
    public void build() {
        setHdfsClient(renderHdfsClient());
    }

    @Override
    public Object execute() {
        FileSystem fileSystem = HdfsRender.build(this.getHdfsClient());
        try {
            switch (this.getHdfsClient().getMethod()) {
                case DOWNLOAD:
                    return HdfsExecute.executeDownload(fileSystem, this.getHdfsClient());
                case MKDIR:
                    HdfsExecute.executeMkdir(fileSystem, this.getHdfsClient());
                    break;
                case PUT:
                    HdfsExecute.executePut(fileSystem, this.getHdfsClient());
                    break;
                case MOVE:
                    HdfsExecute.executeMove(fileSystem, this.getHdfsClient());
                    break;
                case DELETE:
                    HdfsExecute.executeDelete(fileSystem, this.getHdfsClient());
                    break;
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
        // 默认空值响应
        if (object instanceof Boolean) {
            return EasyHdResult.execute((Boolean) object);
        }
        return null;
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
