package org.example;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 登录模块应用启动类
 *
 * @author lyx
 */
@SpringBootApplication
public class AlzheimerClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlzheimerClientApplication.class, args);
    }
}
