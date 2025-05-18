package com.example.myappbackend.service;

import com.example.myappbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationEmail(User user, String token) {
        String url = "http://localhost:5173/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom(fromEmail);
        message.setSubject("Xác thực tài khoản");
        message.setText("Nhấn vào liên kết để xác thực tài khoản: " + url);
        mailSender.send(message);
    }

    public void sendResetPasswordEmail(User user, String token) {
        String url = "http://localhost:5173/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom(fromEmail);
        message.setSubject("Đặt lại mật khẩu");
        message.setText("Nhấn vào liên kết để đặt lại mật khẩu: " + url);
        mailSender.send(message);
    }
}

