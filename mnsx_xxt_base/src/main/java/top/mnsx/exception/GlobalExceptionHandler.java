package top.mnsx.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * 全局异常处理器
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // 针对MethodArgumentNotValidException
    public RestErrorResponse doMethodArgumentNoValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuffer stringBuffer = new StringBuffer();
        fieldErrors.forEach(item -> stringBuffer
                .append(item.getDefaultMessage())
                .append(","));
        return new RestErrorResponse(stringBuffer.toString());
    }

    // 处理XXTException
    @ExceptionHandler(XXTException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 状态码返回500
    public RestErrorResponse doXXTException(XXTException e) {
        log.error("捕获异常：{}", e.getErrMessage());

        return new RestErrorResponse(e.getErrMessage());
    }

    // 捕获不可预知的异常
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 状态码返回500
    @ExceptionHandler(Exception.class)
    public RestErrorResponse doException(Exception e) {
        log.error("捕获异常：{}", e.getMessage());
        e.printStackTrace();

        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
    }
}
