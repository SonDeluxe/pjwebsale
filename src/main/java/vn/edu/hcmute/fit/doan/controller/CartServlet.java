
package vn.edu.hcmute.fit.doan.controller;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.fit.doan.*;
import vn.edu.hcmute.fit.doan.database.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/carts")
public class CartServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final LineItemDAO lineItemDAO = new LineItemDAO();
    private final Gson gson = new Gson();

    // ========================
    // 🧾 GET: Xem giỏ hàng
    // ========================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || action.equals("view")) {
            showCart(request, response);
        }// else if ("addFromWishlist".equals(action)) { // Thêm hành động mới
          //  handleAddFromWishlist(request, response); }
        else {
            response.sendRedirect(request.getContextPath() + "/carts?action=view");
        }
    }

    // ========================
    // ⚙️ POST: Xử lý hành động
    // ========================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) return;

        switch (action) {
            case "addItem" -> handleAddItemAjax(request, response);
            case "updateItem" -> handleUpdateItem(request, response);
            case "removeItem" -> handleRemoveItem(request, response);
            case "buyNow" -> handleBuyNow(request, response);
        }
    }

    // ========================
    // 🛒 AJAX: Thêm sản phẩm vào giỏ
    // ========================
    private void handleAddItemAjax(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        System.out.println("=== 🧩 [DEBUG] handleAddItemAjax ===");
        System.out.println("Session = " + session);
        System.out.println("User = " + session.getAttribute("user"));
        System.out.println("Role = " + session.getAttribute("role"));
        System.out.println("ProductId = " + request.getParameter("productId"));
        System.out.println("Quantity = " + request.getParameter("quantity"));

        try {
            String productParam = request.getParameter("productId");
            String quantityParam = request.getParameter("quantity");

            if (productParam == null || quantityParam == null) {
                response.getWriter().write(gson.toJson(Map.of("status", "error", "message", "Thiếu dữ liệu productId hoặc quantity")));
                return;
            }
            int productId = Integer.parseInt(productParam);
            int quantity = Integer.parseInt(quantityParam);
            Product product = productDAO.findProductById(productId);

            if (product == null) {
                response.getWriter().write(gson.toJson(Map.of("status", "error", "message", "Không tìm thấy sản phẩm ID " + productId)));
                return;
            }
            if (product.getStock() < quantity) {
                response.getWriter().write(gson.toJson(Map.of("status", "error", "message", "Mặt hàng này đang tạm hết hoặc không đủ số lượng")));
                return;
            }

            Object userObj = session.getAttribute("user");
            if (userObj instanceof Customer customer) {
                // ---- Thêm vào DB ----
                Cart cart = cartDAO.findCartByCustomerId(customer.getId());
                if (cart == null) {
                    cart = new Cart(customer);
                    cartDAO.addCart(cart);
                    cart = cartDAO.findCartByCustomerId(customer.getId());
                }

                LineItem item = new LineItem(product, quantity, product.getPrice(), 0);
                item.setCart(cart);
                lineItemDAO.addLineItem(item);

                int totalItems = lineItemDAO.findByCartId(cart.getCartId()).size();
                response.getWriter().write(gson.toJson(Map.of("status", "success", "message", "Đã thêm vào giỏ hàng (User)", "totalItems", totalItems)));
            } else {
                // ---- Giỏ hàng cho Guest ----
                List<LineItem> guestCart = (List<LineItem>) session.getAttribute("guestCart");
                if (guestCart == null) guestCart = new ArrayList<>();

                LineItem existing = guestCart.stream()
                        .filter(i -> i.getProduct().getId() == product.getId())
                        .findFirst().orElse(null);

                if (existing != null) {
                    existing.setQuantity(existing.getQuantity() + quantity);
                    existing.setTotalPrice(existing.getQuantity() * existing.getUnitPrice());
                } else {
                    LineItem item = new LineItem(product, quantity, product.getPrice(), 0);
                    guestCart.add(item);
                }

                session.setAttribute("guestCart", guestCart);
                response.getWriter().write(gson.toJson(Map.of(
                        "status", "success",
                        "message", "Đã thêm vào giỏ hàng (Guest)",
                        "totalItems", guestCart.size()
                )));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of(
                    "status", "error",
                    "message", "Lỗi khi thêm sản phẩm vào giỏ hàng: " + e.getMessage()
            )));
        }
    }

    // ========================
    // 💳 Mua ngay
    // ========================
    private void handleBuyNow(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute("user") instanceof Customer)) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        try {
            int userId = ((User) session.getAttribute("user")).getId();
            EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
            Customer customer = em.find(Customer.class, userId);
            em.close();

            int productId = Integer.parseInt(request.getParameter("productId"));
            Product product = productDAO.findProductById(productId);

            if (product != null) {
                Cart cart = cartDAO.findCartByCustomerId(customer.getId());
                if (cart == null) {
                    cart = new Cart(customer);
                    cartDAO.addCart(cart);
                    cart = cartDAO.findCartByCustomerId(customer.getId());
                }

                LineItem item = new LineItem(product, 1, product.getPrice(), 0);
                item.setCart(cart);
                lineItemDAO.addLineItem(item);

                response.sendRedirect(request.getContextPath() + "/orders?action=create");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/products?action=list");
        }
    }

    // ========================
    // 👀 Hiển thị giỏ hàng
    // ========================
    private void showCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Object userObj = session != null ? session.getAttribute("user") : null;

        List<LineItem> items = new ArrayList<>();
        double total = 0.0;

        try {
            // 🧩 Nếu là Customer → đọc từ DB
            if (userObj instanceof Customer customer) {
                Cart cart = cartDAO.findCartByCustomerId(customer.getId());
                if (cart != null) {
                    items = lineItemDAO.findByCartId(cart.getCartId());
                    total = items.stream().mapToDouble(LineItem::getTotalPrice).sum();
                }
            }
            // 🧩 Nếu là Guest → đọc từ session
            else {
                List<LineItem> guestCart = (List<LineItem>) (session != null ? session.getAttribute("guestCart") : null);
                if (guestCart != null) {
                    items = guestCart;
                    total = guestCart.stream().mapToDouble(LineItem::getTotalPrice).sum();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Gửi dữ liệu cho view JSP
        request.setAttribute("items", items);
        request.setAttribute("total", total);
        request.setAttribute("pageTitle", "Giỏ Hàng");
        request.setAttribute("bodyContent", "/views/carts/view.jsp");
        request.setAttribute("customCss", "cart.css");

        request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);
    }

    // ========================
    // ✏️ Cập nhật & Xóa
    // ========================
    private void handleUpdateItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int lineItemId = Integer.parseInt(request.getParameter("lineItemId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            LineItem item = lineItemDAO.findLineItemById(lineItemId);
            if (item != null && quantity > 0) {
                item.setQuantity(quantity);
                item.setTotalPrice(item.getUnitPrice() * quantity);
                lineItemDAO.updateLineItem(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/carts?action=view");
    }

    private void handleRemoveItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int lineItemId = Integer.parseInt(request.getParameter("lineItemId"));
            lineItemDAO.deleteLineItem(lineItemId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/carts?action=view");
    }

    // ========================
    // 🛒 Thêm từ Wishlist
    // ========================
//    private void handleAddFromWishlist(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        if (session == null || !(session.getAttribute("user") instanceof Customer)) {
//            response.sendRedirect(request.getContextPath() + "/auth?action=login");
//            return;
//        }
//
//        Customer customer = (Customer) session.getAttribute("user");
//        String productIdStr = request.getParameter("productId");
//
//        if (productIdStr == null) {
//            response.sendRedirect(request.getContextPath() + "/wishlist/");
//            return;
//        }
//
//        try {
//            int productId = Integer.parseInt(productIdStr);
//            Product product = productDAO.findProductById(productId);
//
//            if (product != null && product.isStockAvailable(1)) {
//                Cart cart = cartDAO.findCartByCustomerId(customer.getId());
//                if (cart == null) {
//                    cart = new Cart(customer);
//                    cartDAO.addCart(cart);
//                    cart = cartDAO.findCartByCustomerId(customer.getId());
//                }
//
//                LineItem item = new LineItem(product, 1, product.getPrice(), 0);
//                item.setCart(cart);
//                lineItemDAO.addLineItem(item);
//
//                session.setAttribute("success", "Đã thêm sản phẩm từ wishlist vào giỏ hàng!");
//            } else {
//                session.setAttribute("error", "Sản phẩm không có sẵn hoặc không tìm thấy!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            session.setAttribute("error", "Lỗi khi thêm từ wishlist: " + e.getMessage());
//        }
//
//        response.sendRedirect(request.getContextPath() + "/carts?action=view");
//    }
}
