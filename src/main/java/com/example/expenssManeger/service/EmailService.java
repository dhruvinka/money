package com.example.expenssManeger.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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


    public void sendEmailWithAttachment(String to, String subject, String message, byte[] pdfBytes, String filename) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromemail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message);
        helper.addAttachment(filename, new ByteArrayResource(pdfBytes));
        mailSender.send(mimeMessage);
    }


}
