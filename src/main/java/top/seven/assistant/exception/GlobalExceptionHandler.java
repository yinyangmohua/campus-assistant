package top.seven.assistant.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.seven.assistant.model.RequestCode;
import top.seven.assistant.model.Result;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException ex) {
        return Result.fail(ex.getCode(), ex.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    public Result<Void> handleException(Exception ex) {
//        return Result.fail(RequestCode.INTERNAL_ERROR.code(), RequestCode.INTERNAL_ERROR.message());
//    }
}

