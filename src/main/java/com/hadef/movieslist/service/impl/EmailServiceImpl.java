package com.hadef.movieslist.service.impl;

import com.hadef.movieslist.domain.dto.MailBody;
import com.hadef.movieslist.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Override
    public void sendSimpleEmail(MailBody mail) {
        SimpleMailMessage  message = new SimpleMailMessage();
        message.setTo(mail.to());
        message.setFrom(sender);
        message.setSubject(mail.subject());
        message.setText(mail.text());
        mailSender.send(message);
    }
}
