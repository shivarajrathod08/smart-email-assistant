package org.emailwriter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Add BOTH your main URL and the generated Vercel URL to be safe
                .allowedOrigins(
                        "https://smart-email-app.vercel.app",
                        "https://smart-email-assistant-1-xn8a.vercel.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // Use "*" for headers, NOT a URL
                .allowCredentials(true);
    }
}
