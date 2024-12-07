package com.example.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableTransactionManagement // 开启注解方式的事务管理
@SpringBootApplication(scanBasePackages = "com.example.system")  // mapper 包
public class CSApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(CSApplication.class, args);
    }

}
