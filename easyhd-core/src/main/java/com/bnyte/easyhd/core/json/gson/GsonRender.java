package com.bnyte.easyhd.core.json.gson;

import com.google.gson.Gson;

/**
 * gson构建器
 *
 * @author bnyte
 * @since 2022/5/9 20:46
 */
public class GsonRender {

    private static final Gson gson = new Gson();

    public static String toString(Object source) {
        return GsonRender.gson.toJson(source);
    }

    public static <T> T toJava(String source, Class<T> targetType) {
        return GsonRender.gson.fromJson(source, targetType);
    }

}
