package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.fit.doan.Notification;
import vn.edu.hcmute.fit.doan.User;
import vn.edu.hcmute.fit.doan.database.NotificationDAO;
import vn.edu.hcmute.fit.doan.database.UserDAO;  // Để lấy user từ session

import java.io.IOException;
import java.util.List;

@WebServlet("/notifications")
public class NotificationServlet extends HttpServlet {
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final UserDAO userDAO = new UserDAO();  // Để lấy user từ session

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        User user = (User) session.getAttribute("user");
        boolean isAdmin = "Admin".equals(session.getAttribute("role"));

        if ("list".equals(action)) {
            List<Notification> notifications;
            if (isAdmin) {
                notifications = getAllNotifications();  // Placeholder; thêm findAll vào DAO
            } else {
                notifications = getNotificationsByUserId(user.getId());  // Placeholder; thêm method vào DAO
            }
            request.setAttribute("notifications", notifications);
            request.getRequestDispatcher("/views/notifications/list.jsp").forward(request, response);
        } else if ("view".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);  // Fix: cast String to int
                Notification notification = notificationDAO.findNotificationById(id);  // Gọi method, fix type int
                if (notification != null) {
                    request.setAttribute("notification", notification);
                    request.getRequestDispatcher("/views/notifications/view.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Không tìm thấy thông báo!");
                    response.sendRedirect("notifications?action=list");
                }
            }
        } else {
            response.sendRedirect("notifications?action=list");  // Default list
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !"Admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("auth?action=login");  // Chỉ admin add
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String message = request.getParameter("message");
            String userIdStr = request.getParameter("userId");  // Admin chọn user nhận thông báo

            if (message != null && userIdStr != null) {
                int userId = Integer.parseInt(userIdStr);
                User targetUser = userDAO.findUserById(userId);  // Giả sử method findById(int) ở UserDAO
                if (targetUser != null) {
                    Notification newNotification = new Notification(message, targetUser);
                    newNotification.sendNotification();  // Gọi entity method nếu cần
                    notificationDAO.addNotification(newNotification);
                    request.setAttribute("success", "Thêm thông báo thành công!");
                    response.sendRedirect("notifications?action=view&id=" + newNotification.getNotificationId());
                } else {
                    request.setAttribute("error", "Không tìm thấy người dùng!");
                    request.getRequestDispatcher("/views/notifications/add.jsp").forward(request, response);
                }
            }
        } else if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);  // Fix: cast to int
                notificationDAO.deleteNotification(id);  // Gọi method, fix type int
                request.setAttribute("success", "Xóa thông báo thành công!");
            }
            response.sendRedirect("notifications?action=list");
        }
    }

    // Placeholder cho findAll; thêm vào DAO: return em.createQuery("SELECT n FROM Notification n", Notification.class).getResultList();
    private List<Notification> getAllNotifications() {
        // Tạm return empty; thực tế gọi notificationDAO.getAllNotifications();
        return java.util.List.of();  // Import java.util.List nếu cần
    }

    // Placeholder cho getByUserId; thêm vào DAO: em.createQuery("SELECT n FROM Notification n WHERE n.user.id = :userId", Notification.class).setParameter("userId", userId).getResultList();
    private List<Notification> getNotificationsByUserId(int userId) {
        // Tạm return empty; thực tế gọi notificationDAO.getNotificationsByUserId(userId);
        return java.util.List.of();
    }
}