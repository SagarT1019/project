package com.project.project.configuration;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class WebConfiguration implements WebMvcConfigurer {
    @Override

    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET","POST","PUT","DELETE").allowedHeaders("*");
    }

    @Override

    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.add(new StringHttpMessageConverter());

        WebMvcConfigurer.super.configureMessageConverters(converters);

        converters.add(new MappingJackson2HttpMessageConverter(

                new Jackson2ObjectMapperBuilder()

                        .dateFormat(new StdDateFormat())

                        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

                        .build()));
    }

}
