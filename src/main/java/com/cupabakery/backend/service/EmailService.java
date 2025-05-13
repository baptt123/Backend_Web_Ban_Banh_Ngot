package com.cupabakery.backend.service;

import com.cupabakery.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(User user, String token) {
        String url = "http://localhost:5173/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom("trungvan.me@gmail.com");
        message.setSubject("Xác thực tài khoản");
        message.setText("Nhấn vào liên kết để xác thực tài khoản: " + url);
        mailSender.send(message);
    }

    public void sendResetPasswordEmail(User user, String token) {
        String url = "http://localhost:5173/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom("trungvan.me@gmail.com");
        message.setSubject("Đặt lại mật khẩu");
        message.setText("Nhấn vào liên kết để đặt lại mật khẩu: " + url);
        mailSender.send(message);
    }
}

