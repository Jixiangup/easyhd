package com.bnyte.easyhd.core.constant;

import org.springframework.util.PropertyPlaceholderHelper;

/**
 * @author bnyte
 * @since 2022/5/8 16:01
 */
public class SystemConstant {
    public static final PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}", ":", false);
}
