package org.example.utils;

import org.apache.commons.lang3.RandomUtils;
import java.util.UUID;

/**
 * UUID工具类
 */
public class UuidUtil {

    /**
     * 生成UUID
     * 使用UUID和随机数组合生成
     */
    public static String generateUuid() {
        // 生成基础UUID
        String baseUuid = UUID.randomUUID().toString();
        // 组合生成最终UUID
        return baseUuid;
    }
}