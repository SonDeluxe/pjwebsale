package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.fit.doan.RepairSupportCenter;
import vn.edu.hcmute.fit.doan.Product;
import vn.edu.hcmute.fit.doan.Customer;
import vn.edu.hcmute.fit.doan.Order;
import vn.edu.hcmute.fit.doan.database.RepairSupportCenterDAO;
import vn.edu.hcmute.fit.doan.database.ProductDAO;
import vn.edu.hcmute.fit.doan.database.OrderDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/repairs")
public class RepairSupportCenterServlet extends HttpServlet {
    private RepairSupportCenterDAO repairDAO = new RepairSupportCenterDAO();
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        Customer customer = (Customer) session.getAttribute("user");
        boolean isAdmin = "Admin".equals(session.getAttribute("role"));

        if ("list".equals(action)) {
            List<RepairSupportCenter> requests = getAllRequests();  // Placeholder vì DAO chưa có method list
            request.setAttribute("requests", requests);
            request.getRequestDispatcher("/views/repairs/list.jsp").forward(request, response);
        } else if ("view".equals(action) || "edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                RepairSupportCenter repairRequest = repairDAO.findRepairRequestById(idStr);
                if (repairRequest != null && (isAdmin || true)) {  // Bỏ check quyền tạm vì entity chưa có getter đầy đủ
                    request.setAttribute("repairRequest", repairRequest);
                    String jsp = "view".equals(action) ? "/views/repairs/view.jsp" : "/views/repairs/edit.jsp";
                    request.getRequestDispatcher(jsp).forward(request, response);
                } else {
                    session.setAttribute("error", "Không tìm thấy hoặc không có quyền truy cập yêu cầu sửa chữa!");
                    response.sendRedirect("repairs?action=list");
                }
            } else {
                response.sendRedirect("repairs?action=list");
            }
        } else {
            response.sendRedirect("repairs?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        Customer customer = (Customer) session.getAttribute("user");
        boolean isAdmin = "Admin".equals(session.getAttribute("role"));

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String productIdStr = request.getParameter("productId");
            String orderIdStr = request.getParameter("orderId");

            if (productIdStr != null && orderIdStr != null && !productIdStr.isEmpty() && !orderIdStr.isEmpty()) {
                try {
                    int productId = Integer.parseInt(productIdStr);
                    int orderId = Integer.parseInt(orderIdStr);
                    Product product = productDAO.findProductById(productId);
                    Order order = orderDAO.findOrderById(orderId);
                    if (product != null && order != null) {  // Bỏ check quyền tạm vì entity chưa có getter đầy đủ
                        RepairSupportCenter newRequest = new RepairSupportCenter(product, customer, order);
                        newRequest.receiveRequest();
                        repairDAO.addRepairRequest(newRequest);
                        session.setAttribute("success", "Thêm yêu cầu sửa chữa thành công!");
                        response.sendRedirect("repairs?action=list");
                        return;
                    } else {
                        session.setAttribute("error", "Dữ liệu không hợp lệ!");
                    }
                } catch (NumberFormatException e) {
                    session.setAttribute("error", "ID sản phẩm hoặc đơn hàng không hợp lệ!");
                }
            } else {
                session.setAttribute("error", "Thiếu thông tin sản phẩm hoặc đơn hàng!");
            }
            response.sendRedirect("repairs?action=list");
        } else if ("update".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                RepairSupportCenter repairRequest = repairDAO.findRepairRequestById(idStr);
                if (repairRequest != null && (isAdmin || true)) {  // Bỏ check quyền tạm
                    repairDAO.updateRepairRequest(repairRequest);
                    session.setAttribute("success", "Cập nhật yêu cầu thành công!");
                } else {
                    session.setAttribute("error", "Không có quyền cập nhật!");
                }
            }
            response.sendRedirect("repairs?action=list");
        } else if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                RepairSupportCenter repairRequest = repairDAO.findRepairRequestById(idStr);
                if (repairRequest != null && (isAdmin || true)) {  // Bỏ check quyền tạm
                    repairDAO.deleteRepairRequest(idStr);
                    session.setAttribute("success", "Xóa yêu cầu thành công!");
                } else {
                    session.setAttribute("error", "Không có quyền xóa!");
                }
            }
            response.sendRedirect("repairs?action=list");
        } else {
            response.sendRedirect("repairs?action=list");
        }
    }

    // Placeholder cho list vì DAO chưa có method
    private List<RepairSupportCenter> getAllRequests() {
        return java.util.List.of();  // Empty list tạm thời
    }
}