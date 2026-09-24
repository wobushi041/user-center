package org.wobushi041.centerbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置 Web MVC 跨域访问规则
 *
 * @author wobushi041
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 注册允许携带 Cookie 的跨域访问规则
     *
     * @param registry 跨域规则注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 覆盖所有请求
        registry.addMapping("/**")
                // 允许发送 Cookie
                .allowCredentials(true)
                // 允许匹配任意来源域名
                .allowedOriginPatterns("*")
                // 设置允许的 HTTP 方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 设置预检请求缓存时间
                .maxAge(3600);
    }

}
