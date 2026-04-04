package com.app.service.impl;

import com.app.model.dto.request.EmailRequestDto;
import com.app.service.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class EmailSenderImpl implements EmailSender {

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Value("${support.email}")
    private String from;

    public EmailSenderImpl( TemplateEngine templateEngine,JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }


    @Override
    public void sendPasswordResetEmail(String email, String resetLink) {
        Context context = new Context();
        context.setVariable("resetLink", resetLink);

        String htmlContent = templateEngine.process("reset-password", context);

        send(
                EmailRequestDto.builder()
                        .from(from)
                        .to(email)
                        .subject("Parolanızı Sıfırlayın")
                        .replyTo(from)
                        .message(htmlContent)
                        .build());
    }

    @Override
    public void sendUserVerifyEmail(String email, String verifyLink) {
        Context context = new Context();
        context.setVariable("verifyLink", verifyLink);

        String htmlContent = templateEngine.process("verify-user", context);

        send(
                EmailRequestDto.builder()
                        .from(from)
                        .to(email)
                        .replyTo(from)
                        .message(htmlContent)
                        .subject("Hesabınızı doğrulayın")
                        .build());
    }

    @Override
    public void send(EmailRequestDto request) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(request.getTo());
            helper.setFrom(request.getFrom());
            helper.setReplyTo(request.getReplyTo());
            helper.setSubject(request.getSubject());
            helper.setText(request.getMessage(), true);

            mailSender.send(mimeMessage);
            log.info("ActionLog.send.Email send successfully");
        } catch (MessagingException | MailException e) {
            log.error("ActionLog.send.error Email send failed", e);
        }
    }
}
