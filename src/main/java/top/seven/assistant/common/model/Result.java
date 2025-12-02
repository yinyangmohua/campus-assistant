package top.seven.assistant.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.seven.assistant.common.constant.RequestCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result(T data) {
        this.code = RequestCode.OK.code();
        this.message = RequestCode.OK.message();
        this.data = data;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message);
    }
}

