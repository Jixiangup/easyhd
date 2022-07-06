package com.bnyte.easyhd.core.json.gson;

import com.google.gson.Gson;

/**
 * gson渲染器
 *
 * @author bnyte
 * @since 1.0.0
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
