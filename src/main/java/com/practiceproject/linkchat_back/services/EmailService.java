package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.dtos.EmailRequest;
import com.practiceproject.linkchat_back.dtos.SimpleEmailRequest;
import com.practiceproject.linkchat_back.model.InviteEmailEntry;
import com.practiceproject.linkchat_back.viewModels.ChatForm;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendSimpleMessage(String to, String subject, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessage = new MimeMessageHelper(message, true, "UTF-8");
        mimeMessage.setFrom("sysportnov@gmail.com");
        mimeMessage.setTo(to);
        mimeMessage.setSubject(subject);
        mimeMessage.setText(text, true);
        emailSender.send(message);
    }

    public void sendWithTemplate(EmailRequest request) throws Exception {
        ClassPathResource resource = new ClassPathResource("templates/" + request.getTemplateName() + ".html");
        String templateContent = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);

        for (Map.Entry<String, String> entry : request.getTemplateVariables().entrySet()) {
            templateContent = templateContent.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(request.getTo());
        helper.setSubject(request.getSubject());
        helper.setText(templateContent, true);

        emailSender.send(message);
    }

    public void sendInvite(String to, String username, String link, int ttlHours) throws Exception {
        // Load the invite email template
        ClassPathResource res = new ClassPathResource("templates/inviteToTheChat.html");
        String html = java.nio.file.Files.readString(res.getFile().toPath(), java.nio.charset.StandardCharsets.UTF_8);

        // Replace placeholders with actual values
        html = html.replace("{{username}}", username)
                .replace("{{sender}}", "sysportnov@gmail.com")
                .replace("{{link}}", link)
                .replace("{{ttlHours}}", String.valueOf(ttlHours))
                .replace("{{inviteId}}", java.util.UUID.randomUUID().toString());

        // Prepare and send the email
        jakarta.mail.internet.MimeMessage msg = emailSender.createMimeMessage();
        org.springframework.mail.javamail.MimeMessageHelper h = new org.springframework.mail.javamail.MimeMessageHelper(msg, true, "UTF-8");
        h.setFrom("sysportnov@gmail.com");
        h.setTo(to);
        h.setSubject("You’ve got a chat invite!");
        h.setText(html, true);

        emailSender.send(msg);
    }
    public void sendInviteEmail(SimpleEmailRequest emailRequest, ChatForm form) throws MessagingException, IOException {
        Context context = new Context();
        String baseUrl = "https://fs-dev.portnov.com";
        context.setVariable("title", form.getTitle());
        context.setVariable("chatLink", baseUrl + form.getLink());

        String html = templateEngine.process("invite-email-template", context);
        String[] recipients = form.getInviteEmails().stream()
                .map(InviteEmailEntry::getEmail)
                .toArray(String[]::new);

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeHelper.setFrom("sysportnov@gmail.com");
        mimeHelper.setTo(recipients);
        mimeHelper.setSubject("Link Chat Invite");
        mimeHelper.setText(html, true);

        MimeMultipart multipart = getMimeMultipart(html);

        mimeMessage.setContent(multipart);
        emailSender.send(mimeMessage);
    }

    private static MimeMultipart getMimeMultipart(String html) throws MessagingException, IOException {
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(html, "text/html; charset=UTF-8");

        MimeBodyPart imagePart = new MimeBodyPart();
        imagePart.attachFile(new ClassPathResource("static/images/linkchat-logo-150x150.png").getFile());
        imagePart.setContentID("<linkchatLogo>");
        imagePart.setDisposition(MimeBodyPart.INLINE);

        MimeMultipart multipart = new MimeMultipart("related");
        multipart.addBodyPart(htmlPart);
        multipart.addBodyPart(imagePart);
        return multipart;
    }
}
