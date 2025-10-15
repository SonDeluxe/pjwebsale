package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmute.fit.doan.PasswordResetToken;
import vn.edu.hcmute.fit.doan.User;
import vn.edu.hcmute.fit.doan.database.PasswordResetTokenDAO;
import vn.edu.hcmute.fit.doan.database.UserDAO;
import vn.edu.hcmute.fit.doan.service.EmailService;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/forgot-password")
public class PasswordResetServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "request";

        String view = "/views/auth/forgot-password-request.jsp";
        switch (action) {
            case "verify":
                view = "/views/auth/verify-otp.jsp";
                break;
            case "reset":
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("resetAllowedForUser") == null) {
                    request.setAttribute("error", "Vui lòng xác thực OTP trước.");
                    view = "/views/auth/forgot-password-request.jsp";
                } else {
                    view = "/views/auth/reset-password.jsp";
                }
                break;
        }
        request.getRequestDispatcher(view).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        try {
            switch (action) {
                case "sendToken" -> handleSendToken(request, response, session);
                case "sendTokenForSelectedUser" -> handleSendTokenForSelectedUser(request, response, session);
                case "verifyToken" -> handleVerifyToken(request, response, session);
                case "resetPassword" -> handleResetPassword(request, response, session);
                default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Đã xảy ra lỗi không mong muốn.");
            response.sendRedirect(request.getContextPath() + "/forgot-password?action=request");
        }
    }

    private void handleSendToken(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ServletException {
        String identifier = request.getParameter("identifier");
        List<User> users = userDAO.findUsersByEmailOrPhone(identifier);

        String requestUrl = request.getContextPath() + "/forgot-password?action=request";

        if (users.isEmpty()) {
            session.setAttribute("error", "Không tìm thấy tài khoản nào với thông tin đã cung cấp.");
            response.sendRedirect(requestUrl);
        } else if (users.size() == 1) {
            User user = users.get(0);
            try {
                boolean isSent = sendOtpForUser(user);
                if (isSent) {
                    session.setAttribute("message", "Mã OTP đã được gửi đến email của bạn.");
                    response.sendRedirect(request.getContextPath() + "/forgot-password?action=verify&userId=" + user.getId());
                } else {
                    session.setAttribute("error", "Không thể gửi email. Vui lòng thử lại sau.");
                    response.sendRedirect(requestUrl);
                }
            } catch (RuntimeException e) {
                if (e.getMessage().contains("Gmail chưa được cấp quyền")) {
                    response.sendRedirect(request.getContextPath() + "/authorize-gmail");
                } else {
                    session.setAttribute("error", "Lỗi khi gửi email.");
                    response.sendRedirect(requestUrl);
                }
            }
        } else {
            request.setAttribute("users", users);
            request.getRequestDispatcher("/views/auth/select-account.jsp").forward(request, response);
        }
    }

    private void handleSendTokenForSelectedUser(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        User user = userDAO.findUserById(userId);

        if (user != null) {
            try {
                boolean isSent = sendOtpForUser(user);
                if (isSent) {
                    session.setAttribute("message", "Mã OTP đã được gửi đến email của bạn.");
                    response.sendRedirect(request.getContextPath() + "/forgot-password?action=verify&userId=" + user.getId());
                } else {
                    session.setAttribute("error", "Không thể gửi email. Vui lòng thử lại sau.");
                    response.sendRedirect(request.getContextPath() + "/forgot-password?action=request");
                }
            } catch (RuntimeException e) {
                if (e.getMessage().contains("Gmail chưa được cấp quyền")) {
                    response.sendRedirect(request.getContextPath() + "/authorize-gmail");
                } else {
                    session.setAttribute("error", "Lỗi khi gửi email.");
                    response.sendRedirect(request.getContextPath() + "/forgot-password?action=request");
                }
            }
        } else {
            session.setAttribute("error", "Tài khoản không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/forgot-password?action=request");
        }
    }

    private void handleVerifyToken(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String otp = request.getParameter("otp");
        int userId = Integer.parseInt(request.getParameter("userId"));
        PasswordResetToken token = tokenDAO.findByToken(otp);

        if (token == null || token.getUser().getId() != userId || token.isExpired()) {
            session.setAttribute("error", "Mã OTP không hợp lệ hoặc đã hết hạn.");
            response.sendRedirect(request.getContextPath() + "/forgot-password?action=verify&userId=" + userId);
        } else {
            session.setAttribute("resetAllowedForUser", userId);
            tokenDAO.deleteToken(token);
            response.sendRedirect(request.getContextPath() + "/forgot-password?action=reset");
        }
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ServletException {
        Integer userId = (Integer) session.getAttribute("resetAllowedForUser");
        if (userId == null) {
            session.setAttribute("error", "Phiên làm việc không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/forgot-password?action=request");
            return;
        }
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            response.sendRedirect(request.getContextPath() + "/forgot-password?action=reset");
            return;
        }

        User user = userDAO.findUserById(userId);
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        user.setPassword(hashedPassword);
        userDAO.updateUser(user);

        session.removeAttribute("resetAllowedForUser");
        request.setAttribute("success", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập.");
        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
    }

    private String generateOtp() {
        return String.valueOf(new SecureRandom().nextInt(900000) + 100000);
    }

    private boolean sendOtpForUser(User user) {
        try {
            String otp = generateOtp();
            PasswordResetToken token = new PasswordResetToken(otp, user, LocalDateTime.now().plusMinutes(15));
            tokenDAO.createToken(token);
            EmailService.sendOtpEmail(user.getEmail(), otp);
            return true;
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("Gmail chưa được cấp quyền")) {
                throw new RuntimeException("Gmail chưa được cấp quyền", e);
            }
            e.printStackTrace();
            return false;
        }
    }
}