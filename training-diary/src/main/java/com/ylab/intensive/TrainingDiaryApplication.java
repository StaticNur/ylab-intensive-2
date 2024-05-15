package com.ylab.intensive;

import io.ylab.loggingspringbootstarter.annotation.EnableLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableLogging
@EnableTransactionManagement
public class TrainingDiaryApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrainingDiaryApplication.class, args);
    }
}
