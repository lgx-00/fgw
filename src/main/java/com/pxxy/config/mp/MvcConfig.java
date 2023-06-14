package com.pxxy.config.mp;

import com.pxxy.utils.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: hesen
 * @Date: 2023-06-05-15:26
 * @Description:
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设置了一个order 其中值越小代表拦截器优先级越高
        registry.addInterceptor(new LoginInterceptor()).
                excludePathPatterns("/user/login","/voucher/**");

    }
}
