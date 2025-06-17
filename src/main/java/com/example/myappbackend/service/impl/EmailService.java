package com.example.myappbackend.service.impl;

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
    @Value("${app.frontend.primaryurl}")
    private String primaryFrontendUrl;
    @Value("${app.frontend.secondaryurl}")
    private String secondaryFrontendUrl;
    public void sendVerificationEmail(User user, String token) {
        String url;
        try {
            // Có thể ping thử URL chính (ví dụ: 5173) để kiểm tra đang hoạt động (nâng cao)
            url = primaryFrontendUrl + "/verify?token=" + token;
        } catch (Exception e) {
            url = secondaryFrontendUrl + "/verify?token=" + token;
        }
//        String url = "http://localhost:5173/verify?token=" + token;
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

