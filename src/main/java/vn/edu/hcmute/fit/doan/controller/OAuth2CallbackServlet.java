package vn.edu.hcmute.fit.doan.controller;

import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.fit.doan.service.GoogleAuthService;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/oauth2callback")
public class OAuth2CallbackServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            StringBuffer buf = request.getRequestURL();
            if (request.getQueryString() != null) {
                buf.append('?').append(request.getQueryString());
            }

            AuthorizationCodeResponseUrl responseUrl = new AuthorizationCodeResponseUrl(buf.toString());
            String code = responseUrl.getCode();

            if (responseUrl.getError() != null) {
                out.println("<h3>Lỗi khi xác thực: " + responseUrl.getError() + "</h3>");
                return;
            }

            if (code == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("<h3>Thiếu mã xác thực.</h3>");
                return;
            }

            GoogleAuthorizationCodeFlow flow = GoogleAuthService.getFlow();

            // ✅ Tạo redirectUri đúng theo môi trường
            String redirectUri;
            if ("localhost".equals(request.getServerName())) {
                redirectUri = "http://localhost:8080/doan/oauth2callback";
            } else {
                redirectUri = "https://pjwebsale-388614816389.asia-southeast1.run.app/oauth2callback";
            }

            flow.createAndStoreCredential(
                    flow.newTokenRequest(code).setRedirectUri(redirectUri).execute(),
                    "user"
            );

            out.println("<h1>Xác thực thành công!</h1>");
            out.println("<p>Ứng dụng đã được cấp quyền gửi email. Bạn có thể đóng tab này.</p>");

        } catch (Exception e) {
            throw new ServletException("Lỗi trong quá trình callback OAuth2", e);
        }
    }
}