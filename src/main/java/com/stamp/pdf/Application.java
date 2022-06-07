package com.stamp.pdf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(name = "JWT Token", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(info = @Info(title = "PDF Stamp API", version = "1.0"))
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}