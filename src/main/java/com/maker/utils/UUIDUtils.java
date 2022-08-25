package com.maker.utils;

import java.util.UUID;

/**
 *生成用户的唯一标识
 *
 */
public class UUIDUtils {

    /**
     * 生成去除‘-’符号的uuid
     * @return
     */
    public static final String get() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成完整的uuid
     * @return
     */
    public static final String getWithDash() {
        return UUID.randomUUID().toString();
    }
}
