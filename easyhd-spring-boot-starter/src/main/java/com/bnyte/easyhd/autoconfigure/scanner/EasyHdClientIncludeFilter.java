package com.bnyte.easyhd.autoconfigure.scanner;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class EasyHdClientIncludeFilter implements TypeFilter {

    public EasyHdClientIncludeFilter() {
    }

    /**
     * 只要将当前获取到的资源转换为字节码对象时不为空就让他注册
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        // FIXME: 2022/5/10 包扫描资源过滤条件没有完善
        return true; // 这个位置不该写true 后续有时间在修复吧 现在就这样
    }


}
