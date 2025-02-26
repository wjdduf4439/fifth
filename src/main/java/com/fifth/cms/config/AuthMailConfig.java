package com.fifth.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
public class AuthMailConfig {
    @Bean
    public JavaMailSender javaMailSender() {

        System.out.println("AuthMailConfig 설정 > 메일서버 연결 시작.");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("wjdduf4439@gmail.com");
        mailSender.setPassword("ajzr bdiy zsvx kqgj");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        System.out.println("AuthMailConfig 설정 > 메일서버 연결 완료.");

        return mailSender;
    }
}