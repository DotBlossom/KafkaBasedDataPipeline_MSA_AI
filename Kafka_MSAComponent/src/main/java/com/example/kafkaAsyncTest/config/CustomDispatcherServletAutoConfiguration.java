package com.example.kafkaAsyncTest.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.core.annotation.Order;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.DispatcherServlet;


@Configuration
public class CustomDispatcherServletAutoConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.addInitializers(new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) {
                ServletRegistration.Dynamic dispatcherServlet = (ServletRegistration.Dynamic) servletContext.getServletRegistration("dispatcherServlet");
                dispatcherServlet.setInitParameter("threadContextInheritable", "true");
                System.out.println("----------------------gssg-");
            }
        });
    }
}