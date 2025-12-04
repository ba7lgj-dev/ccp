package org.ba7lgj.ccp.common.util;

/**
 * 简单校验工具类，不依赖 Spring。
 */
public final class CcpValidateUtils {

    private CcpValidateUtils() {
    }

    public static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static boolean isNotBlank(String text) {
        return !isBlank(text);
    }

    public static String maskPhone(String phone) {
        if (isBlank(phone) || phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }
}
