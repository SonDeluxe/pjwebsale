package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.fit.doan.Customer;
import vn.edu.hcmute.fit.doan.Order;
import vn.edu.hcmute.fit.doan.Payment;
import vn.edu.hcmute.fit.doan.User;
import vn.edu.hcmute.fit.doan.database.OrderDAO;
import vn.edu.hcmute.fit.doan.database.PaymentDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/payments")
public class PaymentServlet extends HttpServlet {
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        String action = request.getParameter("action");
        User user = (User) session.getAttribute("user");

        if ("list".equals(action)) {
            List<Payment> payments;
            if ("Admin".equals(user.getRole())) {
                payments = paymentDAO.findAll();
            } else {
                payments = paymentDAO.findByCustomerId(user.getId());
            }
            request.setAttribute("payments", payments);
            request.getRequestDispatcher("/views/payments/list.jsp").forward(request, response);
        } else if ("create".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                Order order = orderDAO.findOrderById(orderId);
                // Security check: Customer can only pay for their own order
                if (!"Admin".equals(user.getRole()) && order.getCustomer().getId() != user.getId()) {
                    session.setAttribute("error", "Bạn không có quyền thanh toán cho đơn hàng này.");
                    response.sendRedirect("orders?action=list");
                    return;
                }
                request.setAttribute("order", order);
                request.getRequestDispatcher("/views/payments/create.jsp").forward(request, response);
            } catch (Exception e) {
                session.setAttribute("error", "Đơn hàng không hợp lệ.");
                response.sendRedirect("orders?action=list");
            }
        } else {
            response.sendRedirect("payments?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String method = request.getParameter("method");
                Order order = orderDAO.findOrderById(orderId);

                if (order != null) {
                    Payment payment = new Payment(order, method, order.getTotalAmount());
                    payment.processPayment(); // Set status to "Đã thanh toán"
                    paymentDAO.addPayment(payment);

                    // Update order status
                    order.setStatus("Đã thanh toán");
                    orderDAO.updateOrder(order);

                    session.setAttribute("success", "Thanh toán cho đơn hàng #" + orderId + " thành công!");
                    response.sendRedirect("payments?action=list");
                } else {
                    throw new Exception("Không tìm thấy đơn hàng.");
                }
            } catch (Exception e) {
                session.setAttribute("error", "Thanh toán thất bại: " + e.getMessage());
                response.sendRedirect("orders?action=list");
            }
        }
    }
}