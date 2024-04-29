package com.qgStudio.pedestal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/16
 */
@SpringBootApplication
@EnableScheduling
public class PedestalApplication {
    public static void main(String[] args) {
        SpringApplication.run(PedestalApplication.class, args);
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}
