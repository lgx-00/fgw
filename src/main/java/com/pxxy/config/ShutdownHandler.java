package com.pxxy.config;

import com.pxxy.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * Class name: ShutdownHandler
 *
 * Create time: 2023/7/29 19:17
 *
 * @author xw
 * @version 1.0
 */
@Slf4j
@Component
public class ShutdownHandler {

    @PreDestroy
    public void preDestroy() {
        log.info("接收到关闭信号，开始清理缓存。");
        // 强制下线所有用户
        TokenUtil.invalidateAll();
        log.info("清理完成。");
    }

}
