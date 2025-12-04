package org.ba7lgj.ccp.common.enums;

/**
 * 性别枚举。
 */
public enum GenderEnum {
    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    private final int code;
    private final String desc;

    GenderEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static GenderEnum of(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (GenderEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
