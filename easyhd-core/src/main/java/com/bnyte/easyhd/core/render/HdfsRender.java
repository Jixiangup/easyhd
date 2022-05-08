package com.bnyte.easyhd.core.render;

import com.bnyte.easyhd.core.client.HdfsClient;
import com.bnyte.easyhd.core.exception.ClientConfigurationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author bnyte
 * @since 2022/5/8 15:25
 */
public class HdfsRender {

    private static FileSystem fileSystem;

    public static FileSystem build(HdfsClient hdfsClient) {
        try {
            URI uri = new URI(hdfsClient.getAddress());
            Configuration conf = new Configuration();
            fileSystem = FileSystem.get(uri, conf, hdfsClient.getUser());
            return HdfsRender.fileSystem;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new ClientConfigurationException(e.getMessage());
        }
    }


}
