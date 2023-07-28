package com.pxxy.advice;

import com.pxxy.advice.annotations.Cached;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashMap;

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
@Aspect
@Component
public class BaseDataCacheAOP {

    private final CacheMapper cacheMapper = new CacheMapper();

    @Around("(@within(com.pxxy.advice.annotations.Cached)" +
            " && (execution(public * com.pxxy.service.*.get*())" +
            " || execution(public * com.pxxy.service.*.all())))" +
            " || execution(public * com.pxxy.service.BaseService.query())")
    public Object get(ProceedingJoinPoint pjp) throws Throwable {
        String name = pjp.getSignature().getName();

        // 忽略没有 Cached 注解的类
        Class<?> targetClass = pjp.getTarget().getClass();
        Cached cached = targetClass.getAnnotation(Cached.class);
        if (cached == null) {
            return pjp.proceed();
        }

        // 执行 query 方法视为开始对该类缓存
        String declaringTypeName = targetClass.getName();
        if (name.equals("query")) {
            if (cacheMapper.get(declaringTypeName) == null) {
                cacheMapper.put(declaringTypeName, new Cache());
            }
            return pjp.proceed();
        }

        // 获取类自己的缓存
        Cache cache = cacheMapper.get(declaringTypeName);
        if (cache == null) {
            // 若缓存为空则创建
            cache = new Cache();
            cacheMapper.put(declaringTypeName, cache);
            Object ret = pjp.proceed();
            cache.put(name, ret);
            return ret;
        }

        // 检查缓存的上游依赖缓存是否存在
        for (Class<?> parent : cached.parent()) {
            Cache parentCache = cacheMapper.get(parent.getTypeName());
            if (parentCache == null) {
                // 上游依赖缓存不存在，抛弃旧缓存，创建新缓存
                cache = new Cache();
                cacheMapper.put(declaringTypeName, cache);
                Object ret = pjp.proceed();
                cache.put(name, ret);
                return ret;
            }
        }

        // 从缓存中获取数据
        Object ret = cache.get(name);
        if (ret == null) {
            // 未命中缓存
            ret = pjp.proceed();
            cache.put(name, ret);
        } else {
            log.info("【缓存切面】 执行方法 {}.{}() 时命中缓存，使用缓存数据。", declaringTypeName, name);
        }
        return ret;
    }

    @After("@within(com.pxxy.advice.annotations.Cached)" +
            " && (execution(public * com.pxxy.service.*.add*(..))" +
            " || execution(public * com.pxxy.service.*.update*(..))" +
            " || execution(public * com.pxxy.service.*.delete*(..)))")
    public void set(JoinPoint jp) {
        // 修改了数据意味着缓存过期了，移除过期的缓存
        if (cacheMapper.remove(jp.getSignature().getDeclaringTypeName()) != null) {
            log.info("【缓存切面】 缓存失效，原因：执行方法 {}.{}(..)。", jp.getSignature().getDeclaringTypeName(),
                    jp.getSignature().getName());
        }
    }

    public void flushCache() {
        cacheMapper.clear();
        log.info("【缓存切面】 缓存已刷新。");
    }

}

class CacheMapper extends HashMap<String, Cache> {}
class Cache extends HashMap<String, Object> {}
