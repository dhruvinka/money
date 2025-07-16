package com.example.expenssManeger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private  final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromemail;

    public void  sendEmail(String to,String subject,String body)
    {

        try {

            SimpleMailMessage message=new SimpleMailMessage();
            message.setFrom(fromemail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        }
        catch (Exception e)
        {
            System.out.printf(e.getMessage());
        }


    }


}
