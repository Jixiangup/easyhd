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
        /*String className = metadataReader.getClassMetadata().getClassName();
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException ignored) { }
        return clazz != null;*/
        return true;
    }


}
