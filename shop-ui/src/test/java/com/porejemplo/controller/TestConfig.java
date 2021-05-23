package com.porejemplo.controller;

import com.porejemplo.service.CartService;
import com.porejemplo.service.CartServiceImpl;
import com.porejemplo.service.model.LineItem;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.messaging.simp.SimpSessionScope;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;

@Configuration
public class TestConfig {

    /*@Bean
    public CustomScopeConfigurer customScopeConfigurer(){
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        configurer.addScope("session", new SimpleThreadScope());
        return configurer;
    }*/

}
