package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.fit.doan.*;
import vn.edu.hcmute.fit.doan.database.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.time.LocalDate;


@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final CartDAO cartDAO = new CartDAO();
    private final LineItemDAO lineItemDAO = new LineItemDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final WarrantyDAO warrantyDAO = new WarrantyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";
        User user = (User) session.getAttribute("user");

        switch (action) {
            case "list":
                showOrderList(request, response, user);
                break;

            // BỔ SUNG CASE MỚI
            case "view":
                showOrderDetail(request, response, user);
                break;

            case "create":
                request.getRequestDispatcher("/views/orders/create.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("orders?action=list");
                break;
        }
    }

    private void showOrderList(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        List<Order> orders;
        if ("Admin".equals(user.getRole())) {
            orders = orderDAO.getAllOrders();
        } else {
            orders = orderDAO.findByCustomerId(user.getId());
        }
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/views/orders/list.jsp").forward(request, response);
    }

    // BỔ SUNG PHƯƠNG THỨC MỚI
    private void showOrderDetail(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            // Gọi phương thức DAO mới để lấy đầy đủ chi tiết
            Order order = orderDAO.findOrderByIdWithDetails(orderId);

            if (order == null) {
                request.getSession().setAttribute("error", "Không tìm thấy đơn hàng.");
                response.sendRedirect("orders?action=list");
                return;
            }

            // KIỂM TRA BẢO MẬT: Đảm bảo người dùng chỉ xem được đơn hàng của chính mình (trừ Admin)
            if (!"Admin".equals(user.getRole()) && order.getCustomer().getId() != user.getId()) {
                request.getSession().setAttribute("error", "Bạn không có quyền xem đơn hàng này.");
                response.sendRedirect("orders?action=list");
                return;
            }

            request.setAttribute("order", order);
            request.setAttribute("pageTitle", "Chi tiết đơn hàng #" + order.getId());
            request.setAttribute("bodyContent", "/views/orders/view.jsp");
            request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID đơn hàng không hợp lệ.");
            response.sendRedirect("orders?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        request.setCharacterEncoding("UTF-8");
        if ("checkout".equals(request.getParameter("action"))) {
            handleCheckout(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/orders?action=list");
        }
    }

    private void handleCheckout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Customer customer = (Customer) session.getAttribute("user");

        Cart cart = cartDAO.findCartByCustomerId(customer.getId());
        if (cart == null) {
            session.setAttribute("error", "Không tìm thấy giỏ hàng của bạn.");
            response.sendRedirect(request.getContextPath() + "/carts?action=view");
            return;
        }
        List<LineItem> cartItems = lineItemDAO.findByCartId(cart.getCartId());

        if (cartItems.isEmpty()) {
            session.setAttribute("error", "Giỏ hàng của bạn đang trống!");
            response.sendRedirect(request.getContextPath() + "/carts?action=view");
            return;
        }

        try {
            for (LineItem item : cartItems) {
                Product productInDb = productDAO.findProductById(item.getProduct().getId());
                if (productInDb.getStock() < item.getQuantity()) {
                    session.setAttribute("error", "Sản phẩm '" + productInDb.getName() + "' không đủ hàng.");
                    response.sendRedirect(request.getContextPath() + "/carts?action=view");
                    return;
                }
            }

            Order order = new Order();
            order.setCustomer(customer);
            order.setDeliveryAddress(request.getParameter("deliveryAddress"));
            order.setStatus("Chờ xử lý");

            for (LineItem cartItem : cartItems) {
                LineItem orderItem = new LineItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setUnitPrice(cartItem.getUnitPrice());
                orderItem.setTotalPrice(cartItem.getTotalPrice());
                orderItem.setOrder(order);
                order.getLineItems().add(orderItem);
            }
            order.calculateTotal();
            orderDAO.addOrder(order);

            for (LineItem orderedItem : order.getLineItems()) {
                for (int i = 0; i < orderedItem.getQuantity(); i++) {
                    Warranty warranty = new Warranty();
                    warranty.setUser(customer); // customer lấy từ session
                    warranty.setProduct(orderedItem.getProduct());

                    LocalDate startDate = LocalDate.now();
                    LocalDate endDate = startDate.plusMonths(12); // Mặc định 12 tháng

                    warranty.setStartDate(Date.valueOf(startDate).toLocalDate());
                    warranty.setEndDate(Date.valueOf(endDate).toLocalDate());
                    warranty.setStatus("Còn hạn");
                    warranty.setNotes("Bảo hành cho đơn hàng #" + order.getId());

                    // GỌI PHƯƠNG THỨC MỚI CỦA BẠN
                    warrantyDAO.add(warranty);
                }
            }

            // ==========================================================
            // SỬA LẠI: GỌI PHƯƠNG THỨC MỚI ĐỂ TRỪ TỒN KHO
            // ==========================================================
            productDAO.updateStockForOrder(order.getLineItems());

            lineItemDAO.deleteItemsByCartId(cart.getCartId());

            session.setAttribute("success", "Đặt hàng thành công! Đơn hàng của bạn là #" + order.getId());
            response.sendRedirect(request.getContextPath() + "/orders?action=list");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi khi đặt hàng: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/carts?action=view");
        }
    }
}