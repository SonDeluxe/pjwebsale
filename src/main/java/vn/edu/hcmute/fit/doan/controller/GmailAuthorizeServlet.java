package vn.edu.hcmute.fit.doan.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.fit.doan.service.GoogleAuthService;

import java.io.IOException;

@WebServlet("/authorize-gmail")
public class GmailAuthorizeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Đã truy cập vào GmailAuthorizeServlet.");

        try {
            GoogleAuthorizationCodeFlow flow = GoogleAuthService.getFlow();
            System.out.println("DEBUG: Đã lấy được 'flow' thành công.");

            // ✅ Tạo redirectUri đúng theo môi trường
            String redirectUri;
            if ("localhost".equals(request.getServerName())) {
                redirectUri = "http://localhost:8080/doan/oauth2callback";
            } else {
                redirectUri = "https://pjwebsale-388614816389.asia-southeast1.run.app/oauth2callback";
            }

            System.out.println("DEBUG: Redirect URI được tạo là: " + redirectUri);

            GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl().setRedirectUri(redirectUri);
            response.sendRedirect(url.build());
            System.out.println("DEBUG: Lệnh chuyển hướng đã được gửi.");

        } catch (Exception e) {
            System.err.println("### LỖI NGHIÊM TRỌNG TRONG GmailAuthorizeServlet ###");
            e.printStackTrace();
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().println("Gặp lỗi khi tạo URL xác thực. Vui lòng kiểm tra log của Tomcat.");
            e.printStackTrace(response.getWriter());
        }
    }
}