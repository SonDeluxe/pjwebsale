package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmute.fit.doan.Customer;
import vn.edu.hcmute.fit.doan.User;
import vn.edu.hcmute.fit.doan.database.UserDAO;

import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "login";

        switch (action) {
            case "register":
                // SỬA LẠI: Chuyển thẳng đến trang register.jsp
                request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
                break;
            case "logout":
                HttpSession session = request.getSession(false);
                if (session != null) session.invalidate();
                response.sendRedirect(request.getContextPath() + "/auth?action=login");
                break;
            case "login":
            default:
                // SỬA LẠI: Chuyển thẳng đến trang login.jsp
                request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("login".equals(action)) handleLogin(request, response);
        else if ("register".equals(action)) handleRegister(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userDAO.findUserByUsername(username);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());

            if ("Admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin");
            } else {
                response.sendRedirect(request.getContextPath() + "/"); // Chuyển về trang chủ
            }
        } else {
            request.setAttribute("error", "Sai username hoặc password!");
            // SỬA LẠI: Khi có lỗi, forward thẳng về trang login.jsp
            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");

        if (userDAO.findUserByUsername(username) != null) {
            request.setAttribute("error", "Username đã tồn tại!");
            // SỬA LẠI: Khi có lỗi, forward thẳng về trang register.jsp
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        Customer newCustomer = new Customer(username, hashedPassword, email, address, phone);
        userDAO.addUser(newCustomer);

        request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        // SỬA LẠI: Forward thẳng về trang login.jsp với thông báo thành công
        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
    }
}