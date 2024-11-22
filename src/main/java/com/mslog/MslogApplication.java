package com.mslog;

import com.mslog.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class MslogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MslogApplication.class, args);
    }

}
