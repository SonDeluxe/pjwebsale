package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmute.fit.doan.Admin;
import vn.edu.hcmute.fit.doan.Customer;
import vn.edu.hcmute.fit.doan.User;
import vn.edu.hcmute.fit.doan.database.UserDAO;

import java.io.IOException;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"Admin".equals(session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập.");
            return;
        }
        String action = request.getParameter("action");
        action = (action == null) ? "list" : action;

        try {
            switch (action) {
                case "add":
                    request.getRequestDispatcher("/views/users/add.jsp").forward(request, response);
                    break;
                case "edit":
                    int id = Integer.parseInt(request.getParameter("id"));
                    User user = userDAO.findUserById(id);
                    request.setAttribute("userToEdit", user);
                    request.getRequestDispatcher("/views/users/edit.jsp").forward(request, response);
                    break;
                case "list":
                default:
                    request.setAttribute("users", userDAO.getAllUsers());
                    request.getRequestDispatcher("/views/users/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Đã xảy ra lỗi.");
            response.sendRedirect(request.getContextPath() + "/admin");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"Admin".equals(session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add":
                    addUser(request, response);
                    break;
                case "update":
                    updateUser(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Thao tác thất bại.");
            response.sendRedirect(request.getContextPath() + "/users?action=list");
        }
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Lấy thông tin từ form
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        // Băm mật khẩu
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        User user;
        if ("Admin".equals(role)) {
            user = new Admin();
        } else {
            user = new Customer();
        }
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRole(role);
        user.setAddress(request.getParameter("address"));
        user.setPhone(request.getParameter("phone"));

        userDAO.addUser(user);
        request.getSession().setAttribute("success", "Thêm người dùng mới thành công!");
        response.sendRedirect(request.getContextPath() + "/users?action=list");
    }

//    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        User user = userDAO.findUserById(id);
//        if (user != null) {
//            user.setEmail(request.getParameter("email"));
//            user.setAddress(request.getParameter("address"));
//            user.setPhone(request.getParameter("phone"));
//
//            // Chỉ cập nhật mật khẩu nếu người dùng nhập mật khẩu mới
//            String newPassword = request.getParameter("password");
//            if (newPassword != null && !newPassword.isEmpty()) {
//                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
//                user.setPassword(hashedPassword);
//            }
//            userDAO.updateUser(user);
//            request.getSession().setAttribute("success", "Cập nhật thông tin người dùng thành công!");
//        }
//        response.sendRedirect(request.getContextPath() + "/users?action=list");
//    }
private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
    HttpSession session = request.getSession(false);
    String currentRole = (String) session.getAttribute("role");

    int id = Integer.parseInt(request.getParameter("id"));
    User user = userDAO.findUserById(id);

    if (user == null) {
        session.setAttribute("error", "Người dùng không tồn tại!");
        response.sendRedirect(request.getContextPath() + "/users?action=list");
        return;
    }

    // Ngăn chỉnh sửa admin khác
    if ("Admin".equalsIgnoreCase(user.getRole()) && !"Admin".equalsIgnoreCase(currentRole)) {
        session.setAttribute("error", "❌ Bạn không có quyền chỉnh sửa quản trị viên khác!");
        response.sendRedirect(request.getContextPath() + "/users?action=list");
        return;
    }

    // Cập nhật thông tin cơ bản
    user.setEmail(request.getParameter("email"));
    user.setAddress(request.getParameter("address"));
    user.setPhone(request.getParameter("phone"));

    // Mật khẩu mới (nếu có)
    String newPassword = request.getParameter("password");
    if (newPassword != null && !newPassword.isEmpty()) {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        user.setPassword(hashedPassword);
    }

    // Vai trò mới (nếu có)
    String newRole = request.getParameter("role");

    if (newRole != null && !newRole.isEmpty()) {
        // ✅ Nếu là Customer và được nâng cấp lên Admin
        if ("Customer".equalsIgnoreCase(user.getRole()) && "Admin".equalsIgnoreCase(newRole)) {
            Admin newAdmin = new Admin();
            newAdmin.setUsername(user.getName());
            newAdmin.setPassword(user.getPassword());
            newAdmin.setEmail(user.getEmail());
            newAdmin.setAddress(user.getAddress());
            newAdmin.setPhone(user.getPhone());
            newAdmin.setRole("Admin");

            // Xóa user cũ và thêm user mới
            userDAO.deleteUser(user.getId());
            userDAO.addUser(newAdmin);

            session.setAttribute("success", "🎉 Đã thăng cấp người dùng thành quản trị viên!");
            response.sendRedirect(request.getContextPath() + "/users?action=list");
            return;
        }

        // ❌ Nếu là Admin mà bị chọn hạ xuống Customer
        if ("Admin".equalsIgnoreCase(user.getRole()) && "Customer".equalsIgnoreCase(newRole)) {
            session.setAttribute("error", "⚠️ Không thể hạ cấp quản trị viên!");
            response.sendRedirect(request.getContextPath() + "/users?action=list");
            return;
        }
    }

    // Nếu không thay đổi vai trò → cập nhật thông tin bình thường
    userDAO.updateUser(user);
    session.setAttribute("success", "Cập nhật thông tin người dùng thành công!");
    response.sendRedirect(request.getContextPath() + "/users?action=list");
}



    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);
        request.getSession().setAttribute("success", "Đã xóa người dùng #" + id);
        response.sendRedirect(request.getContextPath() + "/users?action=list");
    }
}