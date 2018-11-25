package com.lemon.mixin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//mapper扫描包
@MapperScan("com.lemon.mixin.mapper")
//开启Springboot事务支持 需要开启的事务方法中加上 @Transactional
@EnableTransactionManagement
public class MixinApplication {

    public static void main(String[] args) {
        SpringApplication.run(MixinApplication.class, args);

    }
}
