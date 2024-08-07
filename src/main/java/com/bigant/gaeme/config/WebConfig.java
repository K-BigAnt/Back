package com.bigant.gaeme.config;

import com.bigant.gaeme.repository.enums.AuthCorp;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToAuthCorpConverter());
    }

    static class StringToAuthCorpConverter implements Converter<String, AuthCorp> {

        @Override
        public AuthCorp convert(String source) {
            return AuthCorp.valueOf(source.toUpperCase());
        }

    }

}
