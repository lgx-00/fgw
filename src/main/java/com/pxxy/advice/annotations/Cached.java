package com.pxxy.advice.annotations;

import java.lang.annotation.*;

/**
 * <h3>缓存注解</h3>
 * <p>指定参加缓存的类与方法。被注解的类会参与到 BaseDataCacheAOP 类的基础数据缓存管理中；
 * 被注解的方法会根据是否更新缓存的注解内部值来确定方法执行后的操作。被注解的方法只有在被注解的类中才会生效。</p>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cached {
    /**
     * 缓存依赖的父缓存，当父缓存失效，该缓存也应失效。
     * @return 类
     */
    Class<?>[] parent() default {};
}
