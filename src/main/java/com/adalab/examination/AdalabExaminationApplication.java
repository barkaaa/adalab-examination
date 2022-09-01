package com.adalab.examination;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.adalab.examination.mapper")
public class AdalabExaminationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdalabExaminationApplication.class, args);
    }

}
