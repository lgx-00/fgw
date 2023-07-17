package com.pxxy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.InetAddress;

@MapperScan("com.pxxy.mapper")
@SpringBootApplication
public class FGWApplication {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(FGWApplication.class, args);
        Environment environment =  context.getBean(Environment.class);
        System.out.println("==========启动成功==========");
        System.out.printf("启动成功，后端服务API地址：http://%s:%s/doc.html%n",
                InetAddress.getLocalHost().getHostAddress(), environment.getProperty("server.port"));
    }
}
