package org.example.ybb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.ybb.mapper")
public class YbbApplication {

    public static void main(String[] args) {
        SpringApplication.run(YbbApplication.class, args);
    }

}
