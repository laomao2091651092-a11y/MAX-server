package com.sweet.photos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 女朋友的甜蜜网站 - 后端启动类
 * 运行后访问 http://localhost:8080/api/photos
 */
@SpringBootApplication
public class SweetPhotosApplication {
    public static void main(String[] args) {
        SpringApplication.run(SweetPhotosApplication.class, args);
        System.out.println("========================================");
        System.out.println("  后端启动成功！http://localhost:8080");
        System.out.println("  照片接口: /api/photos");
        System.out.println("========================================");
    }
}
