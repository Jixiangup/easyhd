package com.bnyte.easyhd.core.handle.impl;

import com.bnyte.easyhd.core.bind.FsPut;
import com.bnyte.easyhd.core.client.HdfsClient;
import com.bnyte.easyhd.core.exception.ClientNotFoundException;
import com.bnyte.easyhd.core.exception.OperateMethodException;
import com.bnyte.easyhd.core.handle.EasyHdHandler;
import com.bnyte.easyhd.core.render.HdfsRender;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
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
                    FsPut.class
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
    public void execute() {
        FileSystem fileSystem = HdfsRender.build(this.getHdfsClient());
        try {
            switch (this.getHdfsClient().getMethod()) {
                case GET:
                    executeGet(fileSystem);
                    break;
                case PUT:
                    executePut(fileSystem);
                    break;
                case MOVE:
                    executeMove(fileSystem);
                    break;
                case DELETE:
                    executeDelete(fileSystem);
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
    }

    private void executeDelete(FileSystem fileSystem) {

    }

    private void executeMove(FileSystem fileSystem) {
    }

    private void executePut(FileSystem fileSystem) throws IOException {
        HdfsClient hdfsClient = this.getHdfsClient();
        Path localPath = new Path(hdfsClient.getLocal());
        Path remotePath = new Path(hdfsClient.getRemote());
        fileSystem.copyFromLocalFile(hdfsClient.getRemove(), hdfsClient.getOverwrite(), localPath, remotePath);
    }

    private void executeGet(FileSystem fileSystem) {

    }

    private HdfsClient renderHdfsClient() {
        FsPut fsPut = getMethod().getAnnotation(FsPut.class);
        if (null != fsPut) return renderPut(fsPut);
        throw new ClientNotFoundException();
    }

    private HdfsClient renderPut(FsPut fsPut) {
        return HdfsClient.build(fsPut, args, getMethod());
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
