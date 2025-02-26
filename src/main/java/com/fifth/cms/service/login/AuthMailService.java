package com.fifth.cms.service.login;

import java.util.HashMap;
import java.util.Random;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.login.AuthMailMapper;
import com.fifth.cms.model.login.AuthMailVO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AuthMailService {
    private final AuthMailMapper authMailDAO;  
    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "wjdduf4439@gmail.com";

    public AuthMailService(JavaMailSender javaMailSender, AuthMailMapper authMailDAO) {
        this.javaMailSender = javaMailSender;
        this.authMailDAO = authMailDAO;
    }

    public AuthMailVO getAuthMailInfo(HashMap<String, String> stringJson) {
        return authMailDAO.getAuthMailInfo(stringJson);
    }

    public void insertAuthMailInfo(AuthMailVO authMailVO) {
        authMailDAO.insertAuthMail(authMailVO);
    }

    public void deleteAuthMailInfo(String uid) {
        authMailDAO.deleteAuthMail(uid);
    }

    // 메일 발송
    public String sendSimpleMessage(String sendEmail) throws MessagingException {
        String number = createNumber(); // 랜덤 인증번호 생성

        MimeMessage message = createMail(sendEmail, number); // 메일 생성
        try {
            javaMailSender.send(message); // 메일 발송
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("메일 발송 중 오류가 발생했습니다.");
        }

        AuthMailVO authMailVO = new AuthMailVO();
        authMailVO.setEmail(sendEmail);
        authMailVO.setAuthkey(number);
        insertAuthMailInfo(authMailVO);

        return number; // 생성된 인증번호 반환
    }

    // 랜덤으로 숫자 생성
    public String createNumber() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) { // 인증 코드 8자리
            int index = random.nextInt(3); // 0~2까지 랜덤, 랜덤값으로 switch문 실행

            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 97)); // 소문자
                case 1 -> key.append((char) (random.nextInt(26) + 65)); // 대문자
                case 2 -> key.append(random.nextInt(10)); // 숫자
            }
        }
        return key.toString();
    }

    public MimeMessage createMail(String mail, String number) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("이메일 인증");
        String body = "";
        body += "<h3>요청하신 인증 번호입니다.</h3>";
        body += "<h1>" + number + "</h1>";
        body += "<h3>감사합니다.</h3>";
        message.setText(body, "UTF-8", "html");

        return message;
    }
}
