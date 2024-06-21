package org.development.authms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AuthmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthmsApplication.class, args);
    }

}
