package com.porejemplo.controller;

import com.porejemplo.controller.repr.ProductRepr;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class TestConfig {

    /*@Bean
    public CustomScopeConfigurer customScopeConfigurer(){
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        configurer.addScope("session", new SimpleThreadScope());
        return configurer;
    }*/

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public List<LineItem> lineItems(){
        ProductRepr expectedProduct = new ProductRepr();
        expectedProduct.setId(1L);
        expectedProduct.setPrice(new BigDecimal(123));
        expectedProduct.setTitle("Product title");

        LineItem lineItem = new LineItem(expectedProduct, 2, "color", "material", "M");
        List<LineItem> lineItems = new ArrayList<>();
        lineItems.add(lineItem);

        return lineItems;
    }
}
