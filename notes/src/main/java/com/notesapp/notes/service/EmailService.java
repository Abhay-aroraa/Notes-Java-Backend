package com.notesapp.notes.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Async
public void sendOtpEmail(String toEmail,  String otp){
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject("Your OTP for Password Reset");
    message.setText("your otp is " + otp + " is Valid for 5 mins;");
    javaMailSender.send(message);
}

}
