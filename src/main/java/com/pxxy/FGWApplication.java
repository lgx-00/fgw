package com.pxxy;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.InetAddress;

@Slf4j
@MapperScan("com.pxxy.mapper")
@SpringBootApplication
public class FGWApplication {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(FGWApplication.class, args);
        Environment environment =  context.getBean(Environment.class);
        log.info("启动成功，后端服务API地址：http://{}:{}/doc.html",
                InetAddress.getLocalHost().getHostAddress(), environment.getProperty("server.port"));
    }
}
