package org.ba7lgj.ccp.miniprogram.vo;

public class MpResult<T> {
    private int code;
    private String msg;
    private T data;

    public MpResult() {
    }

    public MpResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> MpResult<T> ok(T data) {
        return new MpResult<>(0, "success", data);
    }

    public static <T> MpResult<T> ok() {
        return new MpResult<>(0, "success", null);
    }

    public static <T> MpResult<T> error(int code, String msg) {
        return new MpResult<>(code, msg, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
