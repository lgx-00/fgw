package com.pxxy.config;

import com.pxxy.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Class name: Hooks
 *
 * Create time: 2023/7/29 19:17
 *
 * @author xw
 * @version 1.0
 */
@Slf4j
@Component
public class Hooks implements CommandLineRunner, DisposableBean {

    @Override
    public void run(String... args) throws Exception {
        log.info("启动完成，开始载入令牌信息。");
        TokenUtil.load();
        log.info("加载完成。");
    }

    @Override
    public void destroy() {
        log.info("接收到关闭信号，开始清理缓存。");
        TokenUtil.cleanup();
        log.info("清理完成。");
    }

}
