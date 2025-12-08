package org.ba7lgj.ccp.common.enums;

/**
 * 平台实名状态枚举。
 */
public enum RealAuthStatusEnum {
    NOT_AUTH(0, "未认证"),
    PENDING(1, "待审核"),
    APPROVED(2, "审核通过"),
    REJECTED(3, "审核不通过");

    private final int code;
    private final String desc;

    RealAuthStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static RealAuthStatusEnum of(Integer code) {
        if (code == null) {
            return NOT_AUTH;
        }
        for (RealAuthStatusEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return NOT_AUTH;
    }
}
