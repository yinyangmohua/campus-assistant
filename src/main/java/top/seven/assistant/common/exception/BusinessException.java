package top.seven.assistant.common.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import top.seven.assistant.common.constant.RequestCode;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(@NotNull RequestCode errorCode) {
        super(errorCode.message());
        this.code = errorCode.code();
    }

    public BusinessException(@NotNull RequestCode errorCode, String message) {
        super(message);
        this.code = errorCode.code();
    }

    public BusinessException(@NotNull RequestCode errorCode, Throwable cause) {
        super(errorCode.message(), cause);
        this.code = errorCode.code();
    }
}

