package com.Harsh.Smart.Courier.Manager.Service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendWelcomeEmail(String email,String name ,String role){
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

            helper.setTo(email);
            helper.setFrom(fromEmail);
            helper.setSubject("Welcome to Smart Courier Manager!");
            helper.setText(buildWelcomeEmailContent(name, role), true);
            mailSender.send(message);
            log.info("Welcome email sent to {}", email);
        } catch (Exception e){
            log.error("Failed to send welcome email to {}: {}", email, e.getMessage());
        }
    }

    private String buildWelcomeEmailContent(String name, String role) {
        return """
                <div style="font-family:sans-serif;max-width:520px;margin:auto;background:#f9fafb;padding:32px;border-radius:12px;">
                  <div style="background:#0a0c10;border-radius:10px;padding:24px;text-align:center;margin-bottom:24px;">
                    <h1 style="color:#f97316;font-size:20px;margin:0;">📦 Smart Courier</h1>
                  </div>
                  <h2 style="color:#111;">Welcome, %s! 🎉</h2>
                  <p style="color:#374151;">Your account has been created successfully.</p>
                  <div style="background:#fff;border-radius:10px;padding:20px;margin:20px 0;border-left:4px solid #f97316;">
                    <p style="margin:0;font-size:14px;color:#6b7280;">Your Role</p>
                    <p style="margin:4px 0 0;font-size:18px;font-weight:700;color:#f97316;">%s</p>
                  </div>
                  <p style="color:#374151;">You can now log in and start using the platform.</p>
                  <p style="color:#6b7280;font-size:13px;text-align:center;margin-top:24px;">
                    Smart Courier Manager · Automated Notification
                  </p>
                </div>
                """.formatted(name, role);
    }
    @Async
    public void sendOrderStatusEmail(String toEmail, String customerName,
                                     int orderId, String status) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Order #" + orderId + " — " + formatStatus(status));
            helper.setText(buildOrderEmailHtml(customerName, orderId, status), true);

            mailSender.send(message);
            log.info("Order status email sent to {} for order #{}", toEmail, orderId);

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }

    private String formatStatus(String status) {
        return switch (status.toUpperCase()) {
            case "ASSIGNED"   -> "Assigned to Agent";
            case "IN_TRANSIT" -> "Out for Delivery";
            case "DELIVERED"  -> "Delivered Successfully";
            case "CANCELLED"  -> "Cancelled";
            default -> status;
        };
    }
    private String buildOrderEmailHtml(String name, int orderId, String status) {
        String color = switch (status.toUpperCase()) {
            case "ASSIGNED"   -> "#f97316";
            case "IN_TRANSIT" -> "#3b82f6";
            case "DELIVERED"  -> "#22c55e";
            case "CANCELLED"  -> "#ef4444";
            default           -> "#6b7280";
        };

        String icon = switch (status.toUpperCase()) {
            case "ASSIGNED"   -> "📦";
            case "IN_TRANSIT" -> "🚚";
            case "DELIVERED"  -> "✅";
            case "CANCELLED"  -> "❌";
            default           -> "📋";
        };

        return """
            <div style="font-family:sans-serif;max-width:520px;margin:auto;background:#f9fafb;padding:32px;border-radius:12px;">
              <div style="background:#0a0c10;border-radius:10px;padding:24px;text-align:center;margin-bottom:24px;">
                <h1 style="color:#f97316;font-size:20px;margin:0;">📦 Smart Courier</h1>
              </div>
              <h2 style="color:#111;font-size:18px;">Hi %s,</h2>
              <p style="color:#374151;">Your order status has been updated.</p>
              <div style="background:#fff;border-radius:10px;padding:20px;margin:20px 0;border-left:4px solid %s;">
                <p style="margin:0;font-size:14px;color:#6b7280;">Order ID</p>
                <p style="margin:4px 0 12px;font-size:18px;font-weight:700;color:#111;">#%d</p>
                <p style="margin:0;font-size:14px;color:#6b7280;">Status</p>
                <p style="margin:4px 0 0;font-size:18px;font-weight:700;color:%s;">%s %s</p>
              </div>
              <p style="color:#6b7280;font-size:13px;text-align:center;margin-top:24px;">
                Smart Courier Manager · Automated Notification
              </p>
            </div>
            """.formatted(name, color, orderId, color, icon, formatStatus(status));
    }

}
