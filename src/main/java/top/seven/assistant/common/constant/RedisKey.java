package top.seven.assistant.common.constant;

public enum RedisKey {
    CHAT_THREAD("chat:thread"),
    CHAT_MSG("chat:msg:%s:%s"),
    LOGIN_CODE("login:code:%s");

    private final String key;

    RedisKey(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

    public String format(Object... args) {
        return String.format(key, args);
    }
}
