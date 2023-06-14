package com.pxxy;
import com.pxxy.utils.ComputerInfoUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.io.IOException;
@MapperScan
@SpringBootApplication
public class FGWApplication {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(FGWApplication.class, args);
        Environment environment =  context.getBean(Environment.class);
        System.out.println("==========启动成功==========");
        System.out.println("启动成功，后端服务API地址：http://" + ComputerInfoUtil.getIpAddr() + ":"
                + environment.getProperty("server.port") + "/doc.html");
    }
}
