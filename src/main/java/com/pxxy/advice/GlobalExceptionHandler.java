package com.pxxy.advice;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.exc.InputCoercionException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pxxy.exceptions.BaseRuntimeException;
import com.pxxy.exceptions.BadRequestException;
import com.pxxy.exceptions.ForbiddenException;
import com.pxxy.exceptions.NotFoundException;
import com.pxxy.utils.ResultResponse;
import io.swagger.annotations.ApiModelProperty;
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

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常捕获器
 * @author hs
 * 2023/4/15 21:51
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
        log.warn("【全局异常处理】ConstraintViolationException:", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("/"));
        return ResultResponse.fail(message);
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultResponse<?> error(HttpMessageNotReadableException e) {
        Throwable ex = e.getCause();
        Class<? extends Throwable> exClass = Objects.isNull(ex) ? null : ex.getClass();

        if (InvalidFormatException.class.equals(exClass)) {
            InvalidFormatException ife = (InvalidFormatException) ex;
            Class<?> targetType = ife.getTargetType();
            if (Integer.class.equals(targetType)) {
                JsonMappingException.Reference ref = ife.getPath().get(ife.getPath().size() - 1);
                try {
                    Class<?> model = ref.getFrom().getClass();
                    String fieldName = ref.getFieldName();

                    ApiModelProperty prop = model.getDeclaredField(fieldName).getAnnotation(ApiModelProperty.class);
                    String propName = prop == null ? fieldName : prop.value();
                    log.warn("【全局异常处理】无法通过分析给定的字符串得到 32 位整型数值。给定的字符串为 \"{}\"，" +
                            "数据绑定模型为 {}，字段名称为 {}, 模型字段名称为 {}。", ife.getValue(),
                            model, fieldName, propName, e);
                    return ResultResponse.fail(String.format("“%s”的内容不合法，请输入正确的整数。", propName));
                } catch (NoSuchFieldException ignored) {}
                return ResultResponse.fail("请输入正确的整数。");
            }
            log.warn("【全局异常处理】无法分析字符串 \"{}\" 得到 \"{}\" 类型的值。", ife.getValue(), targetType.getName(), e);
        }

        if (JsonMappingException.class.equals(exClass)) {
            JsonMappingException jme = (JsonMappingException) ex;
            exClass = jme.getCause().getClass();

            JsonMappingException.Reference ref = jme.getPath().get(jme.getPath().size() - 1);
            try {
                ApiModelProperty prop = ref.getFrom().getClass().getDeclaredField(ref.getFieldName())
                        .getAnnotation(ApiModelProperty.class);
                String msg = String.format("【全局异常处理】“%s”的内容不合法。", prop.value());
                log.warn(msg, e);
                return ResultResponse.fail(msg);
            } catch (NoSuchFieldException ignored) {}
        }

        if (Objects.isNull(exClass) || InputCoercionException.class.equals(exClass)
                || JsonParseException.class.equals(exClass)) {
            log.warn("【全局异常处理】请求参数无法识别。", e);
        } else {
            log.warn("【全局异常处理】未知异常。", e);
        }
        return ResultResponse.fail("请求参数无法识别");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultResponse<?> error(MissingServletRequestParameterException e) {
        log.warn("【全局异常处理】缺少必要参数。", e);
        return ResultResponse.fail("缺少必要参数");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResultResponse<?> error(BadRequestException e) {
        ResultResponse<Object> ret = ResultResponse.fail(e.getMessage());
        log.info("【返回结果】 {}", ret);
        return ret;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ResultResponse<?> error(ForbiddenException e) {
        ResultResponse<Object> ret = ResultResponse.fail(e.getMessage());
        log.info("【返回结果】 {}", ret);
        return ret;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResultResponse<?> error(NotFoundException e) {
        ResultResponse<Object> ret = ResultResponse.fail(e.getMessage());
        log.info("【返回结果】 {}", ret);
        return ret;
    }

    @ResponseBody
    @ExceptionHandler(BaseRuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultResponse<?> error(BaseRuntimeException e) {
        ResultResponse<Object> ret = ResultResponse.fail(e.getMessage());
        log.info("【返回结果】 {}", ret);
        return ret;
    }

    /**
     * spring 封装的参数验证异常， 在controller中没有写BindingResult(实际开发不常用)参数时，会进入
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResultResponse<?> methodArgumentNotValidException(Exception ex) {
        BindingResult bindingResult = null;
        if (ex instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
        }
        if (ex instanceof BindException) {
            bindingResult = ((BindException) ex).getBindingResult();
        }

        if (bindingResult == null) {
            log.warn("【全局异常处理】参数校验失败，错误信息：{}", ex.getMessage());
            return ResultResponse.fail(ex.getMessage());
        }

        List<ObjectError> errors = bindingResult.getAllErrors();
        Object target = bindingResult.getTarget();
        String className = target != null ? target.getClass().getSimpleName() : null;
        if (errors.isEmpty()) {
            log.warn("【全局异常处理】参数校验失败，对象名称：{}，对象类型：{}",
                    bindingResult.getObjectName(), className);
            return ResultResponse.fail("部分参数不合法");
        }
        ObjectError objectError = errors.get(0);
        String messages = objectError.getDefaultMessage();
        log.warn("【全局异常处理】参数校验失败，对象名称：{}，对象类型：{}，错误信息：{}",
                bindingResult.getObjectName(), className, messages);
        if (messages != null && messages.length() > 50) {
            return ResultResponse.fail("部分参数不合法");
        }
        return ResultResponse.fail(messages);
    }

}
