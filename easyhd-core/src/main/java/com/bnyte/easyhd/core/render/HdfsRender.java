package com.bnyte.easyhd.core.render;

import com.bnyte.easyhd.core.bind.FsDelete;
import com.bnyte.easyhd.core.bind.FsDownload;
import com.bnyte.easyhd.core.bind.FsMkdir;
import com.bnyte.easyhd.core.bind.FsPut;
import com.bnyte.easyhd.core.client.HdfsClient;
import com.bnyte.easyhd.core.exception.ClientConfigurationException;
import com.bnyte.easyhd.core.handle.impl.HdfsHandler;
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

    public static HdfsClient renderDownload(FsDownload fsDownload, HdfsHandler hdfsHandler) {
        return render(fsDownload, hdfsHandler);
    }

    public static HdfsClient renderDelete(FsDelete fsDelete, HdfsHandler hdfsHandler) {
        return render(fsDelete, hdfsHandler);
    }

    public static HdfsClient renderMkdir(FsMkdir fsMkdir, HdfsHandler hdfsHandler) {
        return render(fsMkdir, hdfsHandler);
    }

    public static HdfsClient renderPut(FsPut fsPut, HdfsHandler hdfsHandler) {
        return render(fsPut, hdfsHandler);
    }

    private static HdfsClient render(Object annotation, HdfsHandler hdfsHandler) {
        return HdfsClient.build(annotation, hdfsHandler.getArgs(), hdfsHandler.getMethod());
    }


}
