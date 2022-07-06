package com.bnyte.easyhd.core.execute;

import com.bnyte.easyhd.core.client.HdfsClient;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * @author bnyte
 * @since 1.0.0
 */
public class HdfsActuator {

    /**
     * 执行文件下载
     *
     * @param fileSystem 文件系统对象
     * @param hdfsClient hdfs客户端对象
     * @return 执行结果
     *
     * @throws IOException 失败异常
     */
    public static Boolean executeDownload(FileSystem fileSystem, HdfsClient hdfsClient) throws IOException {
        Path remoteFilepath = new Path(hdfsClient.getRemote());
        Path localFolderPath = new Path(hdfsClient.getLocal());
        fileSystem.copyToLocalFile(hdfsClient.getRemove(), remoteFilepath, localFolderPath, hdfsClient.getUseRawLocalFileSystem());
        return true;
    }

    /**
     * 执行创建文件目录
     *
     * @param fileSystem 文件系统对象
     */
    public static Boolean executeMkdir(FileSystem fileSystem, HdfsClient hdfsClient) throws IOException {
        Path path = new Path(hdfsClient.getFolder());
        return fileSystem.mkdirs(path);
    }

    public static Boolean executeDelete(FileSystem fileSystem, HdfsClient hdfsClient) throws IOException {
        Path path = new Path(hdfsClient.getFolder());
        return fileSystem.delete(path, hdfsClient.getRecursive());
    }

    public static Boolean executeMove(FileSystem fileSystem, HdfsClient hdfsClient) {
        return true;
    }

    /**
     * 执行上传文件
     *
     * @param fileSystem hdfs文件系统对象
     * @throws IOException 上传失败
     */
    public static Boolean executePut(FileSystem fileSystem, HdfsClient hdfsClient) throws IOException {
        Path localPath = new Path(hdfsClient.getLocal());
        Path remotePath = new Path(hdfsClient.getRemote());
        fileSystem.copyFromLocalFile(hdfsClient.getRemove(), hdfsClient.getOverwrite(), localPath, remotePath);
        return true;
    }

}
