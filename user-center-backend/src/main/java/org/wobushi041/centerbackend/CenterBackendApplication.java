package org.wobushi041.centerbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 用户中心后端应用启动入口
 *
 * @author wobushi041
 */
@SpringBootApplication(scanBasePackages = {"org.wobushi041.centerbackend", "service"})
@MapperScan("org.wobushi041.centerbackend.mapper")
public class CenterBackendApplication {

    /**
     * 启动 Spring Boot 应用
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(CenterBackendApplication.class, args);
    }

}
