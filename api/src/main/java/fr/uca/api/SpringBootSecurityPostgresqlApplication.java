package fr.uca.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "fr.uca.api")
public class SpringBootSecurityPostgresqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityPostgresqlApplication.class, args);
    }
}