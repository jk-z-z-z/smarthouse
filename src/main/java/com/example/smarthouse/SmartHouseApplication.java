package com.example.smarthouse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.smarthouse.mapper")
public class SmartHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartHouseApplication.class, args);
    }

}
