package com.pxxy.exception;
import com.pxxy.utils.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description 全局异常捕获器
 * @author hs
 * @date 2023/4/15 21:51
 * @params
 * @return
 */
@Slf4j
@CrossOrigin
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     *参数校验
     *规范中的验证异常，嵌套检验问题 不带英文方法 类名
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultResponse<?> constraintViolationException(ConstraintViolationException e){
        log.warn("ConstraintViolationException:", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("/"));
        return ResultResponse.fail(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResultResponse<?> error(HttpMessageNotReadableException e){
        log.warn("参数错误(json)"+e);
        return ResultResponse.fail("参数错误(json)");
    }
    /**
     * spring 封装的参数验证异常， 在controller中没有写BindingResult(实际开发不常用)参数时，会进入
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultResponse<?> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        String messages = objectError.getDefaultMessage();
        log.error("MethodArgumentNotValidException异常:-------------->{}", messages);
        return ResultResponse.fail(messages);
    }
}
