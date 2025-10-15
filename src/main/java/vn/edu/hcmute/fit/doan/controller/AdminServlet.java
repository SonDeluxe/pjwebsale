package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.fit.doan.database.OrderDAO;
import vn.edu.hcmute.fit.doan.database.ProductDAO;
import vn.edu.hcmute.fit.doan.database.UserDAO;

import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"Admin".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        // Lấy số liệu thật từ DAO
        long userCount = userDAO.countAll();
        long productCount = productDAO.countAll();
        long newOrderCount = orderDAO.countByStatus("Pending"); // Hoặc "Chờ xử lý"

        // Gửi số liệu qua JSP
        request.setAttribute("userCount", userCount);
        request.setAttribute("productCount", productCount);
        request.setAttribute("newOrderCount", newOrderCount);

        // PHẦN QUAN TRỌNG: Phải forward đến template.jsp
        request.setAttribute("pageTitle", "Bảng điều khiển Admin");
        request.setAttribute("bodyContent", "/views/admin/dashboard.jsp"); // Chỉ định nội dung
        // Trang admin có thể dùng CSS riêng hoặc không
        // request.setAttribute("customCss", "admin.css");
        request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response); // Luôn trỏ đến template
    }
}