package fr.uca.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration(proxyBeanMethods = false)
@SpringBootApplication(scanBasePackages = "fr.uca.auth.repository"
//        , exclude = { SecurityAutoConfiguration.class }
)
public class AuthServiceApp {
    private static Class<AuthServiceApp> application = AuthServiceApp.class;
    public static void main(String[] args) {
        SpringApplication.run(application, args);
    }

}
