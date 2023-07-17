package com.pxxy.advice;

import com.pxxy.utils.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
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
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     *参数校验
     *规范中的验证异常，嵌套检验问题 不带英文方法 类名
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResultResponse<?> constraintViolationException(ConstraintViolationException e) {
        log.warn("ConstraintViolationException:", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("/"));
        return ResultResponse.fail(message);
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultResponse<?> error(HttpMessageNotReadableException e) {
        log.warn("请求参数无法识别：" + e);
        return ResultResponse.fail("请求参数无法识别");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultResponse<?> error(MissingServletRequestParameterException e) {
        log.warn("缺少必要参数", e);
        return ResultResponse.fail("缺少必要参数");
    }

    /**
     * spring 封装的参数验证异常， 在controller中没有写BindingResult(实际开发不常用)参数时，会进入
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResultResponse<?> methodArgumentNotValidException(Exception ex, HttpServletRequest request) {
        BindingResult bindingResult = null;
        if (ex instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
        }
        if (ex instanceof BindException) {
            bindingResult = ((BindException) ex).getBindingResult();
        }

        if (bindingResult == null) {
            log.warn("参数校验失败，错误信息：{}", ex.getMessage());
            return ResultResponse.fail(ex.getMessage());
        }

        List<ObjectError> errors = bindingResult.getAllErrors();
        Object target = bindingResult.getTarget();
        String className = target != null ? target.getClass().getSimpleName() : null;
        if (errors.isEmpty()) {
            log.warn("参数校验失败，对象名称：{}，对象类型：{}",
                    bindingResult.getObjectName(), className);
            return ResultResponse.fail("参数错误");
        }
        ObjectError objectError = errors.get(0);
        String messages = objectError.getDefaultMessage();
        log.warn("参数校验失败，对象名称：{}，对象类型：{}，错误信息：{}",
                bindingResult.getObjectName(), className, messages);
        return ResultResponse.fail(messages);
    }

}
