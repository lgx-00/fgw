package com.pxxy.config;

import com.pxxy.intercepter.LoginInterceptor;
import com.pxxy.intercepter.PermissionInterceptor;
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
            "json/**", "fonts/**","/*.html","/webjars/**","/swagger-resources/**", "/log/**");

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设置了一个order 其中值越小代表拦截器优先级越高
        registry.addInterceptor(new LoginInterceptor()).
                excludePathPatterns(EXCLUDE_PATH).order(0);

        //权限拦截器
        registry.addInterceptor(new PermissionInterceptor()).excludePathPatterns(EXCLUDE_PATH).order(1);
    }

//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
//        return builder -> {
//            JavaTimeModule timeModule = new JavaTimeModule();
//            timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            builder.modules(timeModule);
//        };
//    }
}
