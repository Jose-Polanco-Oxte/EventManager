package jpolanco.springbootapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringToEnumConverterFactory converter;

    @Override
    public void addFormatters(org.springframework.format.FormatterRegistry registry) {
        registry.addConverterFactory(converter);
    }
}
