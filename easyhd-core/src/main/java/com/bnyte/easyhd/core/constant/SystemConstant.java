package com.bnyte.easyhd.core.constant;

import org.springframework.util.PropertyPlaceholderHelper;

/**
 * @author bnyte
 * @since 1.0.0
 */
public class SystemConstant {

    /**
     * spring字符模板串 ${key:default_value}
     */
    public static final PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}", ":", false);
}
