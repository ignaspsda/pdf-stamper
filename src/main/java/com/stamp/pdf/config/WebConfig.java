package com.stamp.pdf.config;

import com.stamp.pdf.security.JwtValidationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

/*    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtValidationInterceptor()).addPathPatterns("/api/pdf/**").excludePathPatterns("/api/pdf/test");
    }*/
}
