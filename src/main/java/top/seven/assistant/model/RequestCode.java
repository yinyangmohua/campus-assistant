package top.seven.assistant.model;

public enum RequestCode {
    OK(200, "成功"),
    INVALID_CREDENTIALS(401, "账号或密码错误"),
    USER_NOT_FOUND(404, "用户不存在"),
    USER_DELETED(403, "用户已被禁用"),
    WRONG_CURRENT_PASSWORD(401, "当前密码错误"),
    SESSION_NOT_FOUND(404, "会话不存在"),
    PIN_LIMIT_EXCEEDED(409, "置顶数量已达上限(最多3个)"),
    SESSION_IS_PINNED(409, "会话已置顶"),
    BAD_REQUEST(400, "请求参数错误"),
    INTERNAL_ERROR(500, "服务器内部错误");

    private final int code;
    private final String message;

    RequestCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
