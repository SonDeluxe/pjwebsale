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

    // ✅ Kiểm tra và trả về Credential hợp lệ
    public static Credential getCredentials() throws IOException {
        try {
            Credential credential = GoogleAuthService.getFlow().loadCredential("user");

            if (credential == null || credential.getAccessToken() == null ||
                    (credential.getExpiresInSeconds() != null && credential.getExpiresInSeconds() <= 60)) {
                throw new IOException("Gmail chưa được cấp quyền.");
            }

            return credential;
        } catch (IOException e) {
            throw e; // ném lại để servlet xử lý
        } catch (Exception e) {
            throw new IOException("Lỗi khi lấy Credential Gmail.", e);
        }
    }

    // ✅ Tạo email MIME
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

    // ✅ Mã hóa email thành Message Gmail
    private static Message createMessageWithEmail(MimeMessage emailContent) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    // ✅ Gửi email OTP — ném IOException nếu Gmail chưa xác thực
    public static void sendOtpEmail(String recipientEmail, String otp) throws IOException {
        try {
            Credential credential = getCredentials();

            Gmail service = new Gmail.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            String subject = "Yêu cầu đặt lại mật khẩu Honda";
            String bodyText = "Xin chào,\n\n"
                    + "Bạn vừa yêu cầu đặt lại mật khẩu cho tài khoản Honda của mình.\n"
                    + "🔐 Mã OTP của bạn là: " + otp + "\n\n"
                    + "⏰ Mã này sẽ hết hạn sau 15 phút.\n"
                    + "Vui lòng không chia sẻ mã này với bất kỳ ai.\n\n"
                    + "Trân trọng,\nĐội ngũ Honda.";

            MimeMessage mimeMessage = createEmail(recipientEmail, subject, bodyText);
            Message message = createMessageWithEmail(mimeMessage);
            service.users().messages().send("me", message).execute();

            System.out.println("✅ Gửi email API thành công đến " + recipientEmail);

        } catch (IOException e) {
            throw e; // Gmail chưa xác thực → để servlet xử lý
        } catch (Exception e) {
            throw new IOException("Lỗi khi gửi email qua Gmail API.", e);
        }
    }
}