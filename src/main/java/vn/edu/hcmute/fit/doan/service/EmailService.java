package vn.edu.hcmute.fit.doan.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class EmailService {

    private static final String APPLICATION_NAME = "DoAnTotNghiep App";

    private static Credential getCredentials() throws Exception {
        Credential credential = GoogleAuthService.getFlow().loadCredential("user");
        if (credential == null || (credential.getExpiresInSeconds() != null && credential.getExpiresInSeconds() <= 60)) {
            throw new IOException("Credential không hợp lệ hoặc đã hết hạn. Vui lòng chạy quy trình xác thực.");
        }
        return credential;
    }

    private static MimeMessage createEmail(String to, String subject, String bodyText) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("me"));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject, "UTF-8");
        email.setText(bodyText, "UTF-8");
        return email;
    }

    private static Message createMessageWithEmail(MimeMessage emailContent) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public static void sendOtpEmail(String recipientEmail, String otp) {
        try {
            Credential credential = getCredentials();

            Gmail service = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            String subject = "Yêu cầu đặt lại mật khẩu Honda";
            String bodyText = "Xin chào,\n\nMã OTP để đặt lại mật khẩu của bạn là: " + otp
                    + "\n\nMã này sẽ hết hạn sau 15 phút."
                    + "\n\nTrân trọng,\nĐội ngũ Honda.";
            MimeMessage mimeMessage = createEmail(recipientEmail, subject, bodyText);

            Message message = createMessageWithEmail(mimeMessage);
            service.users().messages().send("me", message).execute();

            System.out.println("✅ Gửi email API thành công đến " + recipientEmail);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email qua Gmail API:");
            e.printStackTrace();
        }
    }
}