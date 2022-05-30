package org.hj.chain.platform;

public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "数据异常，请联系管理员"),
    BUSI_FAILED(501, "系统异常"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    KICKED(405, "账号在另外的地方登录");

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    private final Integer code;
    private final String message;

    ResultCode(Integer errCode, String msg) {
        this.code = errCode;
        this.message = msg;
    }


}
