package com.fifth.cms.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class AuthMailConfig {

    private final Environment environment;

    public AuthMailConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public JavaMailSender javaMailSender() {

        System.out.println("AuthMailConfig 설정 > 메일서버 연결 시작.");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("wjdduf4439@gmail.com");
        mailSender.setPassword(environment.getProperty("GMAIL_SMTP_PASSWORD"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        System.out.println("AuthMailConfig 설정 > 메일서버 연결 완료.");

        return mailSender;
    }
}