package com.pzhu.iacaa2_0;

import com.gapache.security.annotation.EnableAuthResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.pzhu.iacaa2_0.**.mapper")
@EnableAuthResourceServer("Iacaa20Server")
@SpringBootApplication
public class Iacaa20Application {
    public static void main(String[] args) {
        SpringApplication.run(Iacaa20Application.class, args);
    }
}
