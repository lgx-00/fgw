package com.pxxy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-06-05-15:26
 * @Description:
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    //不做拦截的路径
    //上面一行是静态资源路径
    //下面一行是接口访问路径
    private static final List<String> EXCLUDE_PATH = Arrays.asList("/", "css/**", "js/**", "img/**",
            "json/**", "fonts/**","/*.html","/webjars/**","/swagger-resources/**", "/log/**","/summary/exportExcel");

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        //设置了一个order 其中值越小代表拦截器优先级越高
//        registry.addInterceptor(new LoginInterceptor()).
//                excludePathPatterns(EXCLUDE_PATH).order(0);
//
//        // 权限拦截器
//        registry.addInterceptor(new PermissionInterceptor()).excludePathPatterns(EXCLUDE_PATH).order(1);
    }


//    /**
//     * 跨域支持
//     *
//     * @param registry
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//
//        registry.addMapping("/**")
//
//                .allowedOrigins("*")
//
//                .allowCredentials(true)
//
//                .allowedMethods("GET", "POST", "DELETE", "PUT")
//
//                .allowedHeaders("*")
//
//                .maxAge(3600 * 24);
//
//    }

}
