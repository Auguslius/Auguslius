package org.example.utils;

public class desensitizeUitl {
    /**
     * 脱敏工具方法
     *
     * @param str          需要脱敏的字符串
     * @param prefixLength 前面保留的字符数
     * @param suffixLength 后面保留的字符数
     * @return 脱敏后的字符串
     */
    public static String desensitize(String str, int prefixLength, int suffixLength) {
        if (str == null || str.length() <= prefixLength + suffixLength) {
            return str; // 如果字符串长度不足以脱敏，直接返回原字符串
        }

        // 截取前面和后面的字符
        String prefix = str.substring(0, prefixLength);
        String suffix = str.substring(str.length() - suffixLength);

        // 中间用星号填充
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < str.length() - prefixLength - suffixLength; i++) {
            sb.append("*");
        }
        sb.append(suffix);

        return sb.toString();
    }
}
