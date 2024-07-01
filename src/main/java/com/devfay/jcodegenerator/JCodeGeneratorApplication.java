package com.devfay.jcodegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JCodeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(JCodeGeneratorApplication.class, args);
    }

}
