package com.bnyte.easyhd.core.build;

import com.bnyte.easyhd.core.bind.FsPut;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Hdfs;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author bnyte
 * @since 2022/5/8 1:59
 */
public class HdfsBuild {
    public static void build(FsPut fsPut) {
        try {
            URI uri = new URI(fsPut.address());
            Configuration conf = new Configuration();
            FileSystem root = FileSystem.get(uri, conf, "root");
            String remote = fsPut.remote();
            if (StringUtils.isEmpty(remote)) remote = "/tmp";
            Path remotePath = new Path(remote);
            Path localPath = new Path(fsPut.local());
            root.copyFromLocalFile(fsPut.remove(), fsPut.overwrite(), localPath, remotePath);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
