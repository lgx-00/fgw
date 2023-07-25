package com.pxxy.advice;

import com.pxxy.advice.annotations.Cached;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础数据缓存切面类
 *
 * 在获取
 *
 * Create time: 2023/7/20 10:37
 *
 * @author xw
 * @version 1.0
 */
@Slf4j
//@Aspect
@Component
public class BaseDataCacheAOP {

    private final Map<String, Object> cacheMapper = new HashMap<>();

    @Around("@within(com.pxxy.advice.annotations.Cached)" +
            " && (execution(public * com.pxxy.service.*.get*(..))" +
            " || execution(public * com.pxxy.service.*.all(..)))")
    public Object get(ProceedingJoinPoint pjp) throws Throwable {
        String name = pjp.getSignature().getName();
        String declaringTypeName = pjp.getSignature().getDeclaringTypeName();

/*
        Object ret = cacheMapper.get(signature);
        if (ret == null) {
            ret = pjp.proceed();
            cacheMapper.put(signature, ret);
        }

        Cached cached = pjp.getTarget().getClass().getAnnotation(Cached.class);
        for (Class<?> parent : cached.parent()) {
            if (cacheMapper.containsKey(parentSignature)) {

            }
        }

        return ret;*/
        return null;
    }

    @Around("@within(com.pxxy.advice.annotations.Cached)" +
            " && (execution(public * com.pxxy.service.*.add*(..))" +
            " || execution(public * com.pxxy.service.*.update*(..))" +
            " || execution(public * com.pxxy.service.*.delete*(..)))")
    public Object set(ProceedingJoinPoint pjp) throws Throwable {
        Cached cached = pjp.getTarget().getClass().getAnnotation(Cached.class);
        String key = pjp.getSignature().toShortString();

        for (Class<?> parent : cached.parent()) {
            parent.toString();
        }

        Object ret = cacheMapper.get(key);
        if (ret == null) {
            ret = pjp.proceed();
            cacheMapper.put(key, ret);
        }

        return ret;
    }

    public void flushCache() {
        // TODO: 2023/7/20 执行刷新缓存的操作
        log.info("缓存已刷新");
    }

}

class CacheMapper extends HashMap<String, Cache> {

}

class Cache extends HashMap<String, Object> {

    boolean valid() {
        return false;
    }

}
