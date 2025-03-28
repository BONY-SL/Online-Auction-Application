package com.example.BidZone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;


    @Async
    synchronized public void sendUserOTPcODE(String to, String subject, String text) {

        System.out.println(to+subject+text);
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("usernamex1995@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
