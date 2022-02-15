package auth.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
        (scanBasePackages = "auth.service.*")
////        , exclude = { SecurityAutoConfiguration.class }
//)
public class AuthServiceApp {
    private static Class<AuthServiceApp> application = AuthServiceApp.class;
    public static void main(String[] args) {
        SpringApplication.run(application, args);
    }

}
