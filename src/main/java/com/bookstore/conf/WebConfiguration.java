package com.bookstore.conf;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {
    /*@Autowired
    LoginInterceptor loginInterceptor;*/

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/user/login", "/user/register");
    }

    @Bean
    public JsonMapper jsonMapper(){
        return JsonMapper.builder().build();
    }
}
