package com.Yana.Buddy.service;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Log4j2
@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    JavaMailSender emailSender;

    public static String ePw = null;

    private MimeMessage createMessage(String to)throws Exception{

        ePw= createKey();
        log.info("보내는 대상 : "+to);
        log.info("인증 번호 : "+ePw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO,to);
        message.setSubject("buddy 회원가입 이메일 인증");

        String msgg="";
        msgg+= "<img align='center' src='https://buddy-image-server.s3.ap-northeast-2.amazonaws.com/email_check_template.jpg'/>";
        msgg+= "<div style='margin:100px;'>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= ePw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg,"utf-8","html");
        message.setFrom(new InternetAddress("namtndus@gmail.con","buddy"));
        return message;
    }

    private static String createKey() {
        StringBuffer key = new StringBuffer();
        SecureRandom rnd  = null;
        try{
            rnd = SecureRandom.getInstance("SHA1PRNG");
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        for(int i = 0; i< 8; i++){
            int index = rnd.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int)(rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 64));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }
        return key.toString();
    }

    @Override
    public String sendSimpleMessage(String to) throws Exception {
        MimeMessage message = createMessage(to);
        try{
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalAccessException();
        }
        return ePw;
    }
}
