package org.ba7lgj.ccp.miniprogram.exception;

public class MpServiceException extends RuntimeException {
    private final int code;

    public MpServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
