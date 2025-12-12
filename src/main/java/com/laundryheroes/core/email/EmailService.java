package com.laundryheroes.core.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.Map;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    private final String fromAddress = "noreply@laundryheroes.org";
    private final String supportEmail = "support@laundryheroes.org";
    private final String logoUrl = "https://laundry-assets.s3.us-east-1.amazonaws.com/logo.png"; // TODO: replace

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /** Backward compatible: body here = plain text email */
    public void sendEmail(String to, String subject, String body) {
        sendPlainText(to, subject, body);
    }

    /** New: send beautiful templated HTML email */
    public void sendTemplatedEmail(
        String to,
        String subject,
        EmailTemplateModel model
    ) {
        log.info("Sending templated email to {}", to);

        try {
            String html = loadTemplate("templates/base-email.html");

            html = html
                .replace("{{TITLE}}", escapeHtml(model.title()))
                .replace("{{MESSAGE}}", model.message()) // allow HTML here
                .replace("{{SECONDARY_TEXT}}",
                    model.secondaryText() == null ? "" : escapeHtml(model.secondaryText()))
                .replace("{{SECONDARY_DISPLAY}}",
                    model.secondaryText() == null || model.secondaryText().isBlank()
                        ? "none"
                        : "block")
                .replace("{{YEAR}}", String.valueOf(Year.now().getValue()));

            sendHtml(to, subject, html);

        } catch (Exception e) {
            log.error("Email send failed", e);
            throw new RuntimeException("Email failed", e);
        }
    }

    private void sendHtml(String to, String subject, String html) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        helper.setFrom(fromAddress);
        helper.setTo(to);
        helper.setSubject(subject);

        // true = HTML
        helper.setText(html, true);

        mailSender.send(message);
    }

    private void sendPlainText(String to, String subject, String body) {
        log.info("Attempting to send plain email to {}", to);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);

            mailSender.send(message);
            log.info("Email successfully sent to {}", to);

        } catch (MessagingException e) {
            log.error("Failed to send email to {}", to, e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    private String loadTemplate(String classpathLocation) throws Exception {
        ClassPathResource resource = new ClassPathResource(classpathLocation);
        try (var in = resource.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /** Very simple placeholder replacement: {{key}} */
    private String render(String template, Map<String, String> values) {
        String out = template;
        for (var entry : values.entrySet()) {
            out = out.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return out;
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String escapeAttr(String s) {
        // for href attribute – basic safe
        return escapeHtml(s);
    }
}
