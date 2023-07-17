package com.pxxy.advice;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.PropertyAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultBindingErrorProcessor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * <h3>参数绑定异常自定义处理控制器增强类</h3>
 *
 * <p>允许自定义在参数绑定异常时执行的操作。</p>
 *
 * @author xw
 * @version 1.0
 */
@ControllerAdvice
public class InitBinderControllerAdvice {

    @InitBinder
    public void dateTypeBinder(WebDataBinder webDataBinder) {

        webDataBinder.setBindingErrorProcessor(new CustomizedBindingErrorProcessor());

    }

    /**
     * <h3>自定义参数绑定异常处理类</h3>
     *
     * <p>忽略分页参数为空时的异常，当分页参数为空时使用默认值。</p>
     *
     * @author xw
     * @version 1.0
     */
    private static class CustomizedBindingErrorProcessor extends DefaultBindingErrorProcessor {

        @Override
        public void processPropertyAccessException(PropertyAccessException e, BindingResult bindingResult) {
            if (("pageNum".equals(e.getPropertyName()) || "pageSize".equals(e.getPropertyName()))
                    && StrUtil.isBlankIfStr(e.getValue())) return;

            super.processPropertyAccessException(e, bindingResult);
        }

    }

}
