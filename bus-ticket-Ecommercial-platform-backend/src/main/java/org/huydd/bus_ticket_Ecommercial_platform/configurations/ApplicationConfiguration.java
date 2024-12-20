package org.huydd.bus_ticket_Ecommercial_platform.configurations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private final Environment environment;

    @Bean
    public Cloudinary cloudinary() {
        String cloudName = environment.getProperty("cloudinary.cloudName");
        String apiKey = environment.getProperty("cloudinary.apiKey");
        String apiSerect = environment.getProperty("cloudinary.apiSerect");
        Cloudinary c = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSerect,
                "secure", true
        ));

        return c;

    }

}
