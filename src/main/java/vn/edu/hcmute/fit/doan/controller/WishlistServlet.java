package vn.edu.hcmute.fit.doan.controller;

import vn.edu.hcmute.fit.doan.Wishlist;
import vn.edu.hcmute.fit.doan.User;
import vn.edu.hcmute.fit.doan.Product;
import vn.edu.hcmute.fit.doan.database.WishlistDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.fit.doan.database.ConnectionPool;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/wishlist/*")
public class WishlistServlet extends HttpServlet {

    private EntityManagerFactory entityManagerFactory;
    private WishlistDAO wishlistDAO;

    @Override
    public void init() throws ServletException {
        entityManagerFactory = ConnectionPool.getEntityManagerFactory();
        if (entityManagerFactory == null) {
            throw new ServletException("EntityManagerFactory is null. Check ConnectionPool configuration.");
        }
        wishlistDAO = new WishlistDAO(); // Khởi tạo WishlistDAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getPathInfo();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            if (action == null || action.equals("/")) {
                showWishlist(request, response, user, em);
            } else if (action.equals("/add")) {
                addToWishlist(request, response, user, em);
            } else if (action.equals("/remove")) {
                removeFromWishlist(request, response, user, em);
            } else if (action.equals("/check")) {
                checkProductInWishlist(request, response, user, em);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace(out); // In lỗi chi tiết ra response
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi server: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void showWishlist(HttpServletRequest request, HttpServletResponse response, User user, EntityManager em)
            throws ServletException, IOException {

        try {
            if (user == null || user.getId() <= 0) {
                throw new IllegalStateException("User ID is invalid");
            }

            // Giả sử Customer có userId khớp với user.id
            Wishlist wishlist = wishlistDAO.findWishlistByCustomerId(user.getId());
            List<Wishlist> wishlists = (wishlist != null) ? List.of(wishlist) : List.of();

            request.setAttribute("wishlists", wishlists);
            request.getRequestDispatcher("/views/wishlist/view.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải danh sách: " + e.getMessage());
        }
    }

    private void addToWishlist(HttpServletRequest request, HttpServletResponse response, User user, EntityManager em)
            throws IOException {

        String productIdStr = request.getParameter("productId");
        PrintWriter out = response.getWriter();

        if (productIdStr == null || productIdStr.isEmpty()) {
            out.write("ERROR: Product ID is required");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdStr);
            em.getTransaction().begin();

            Product product = em.find(Product.class, productId);
            if (product == null) {
                out.write("ERROR: Product not found");
                em.getTransaction().rollback();
                return;
            }

            Wishlist wishlist = wishlistDAO.findWishlistByCustomerId(user.getId());
            if (wishlist == null) {
                wishlist = new Wishlist(); // Cần liên kết với Customer nếu có
                // Giả sử Customer cần được ánh xạ (xem phần mở rộng)
                wishlistDAO.addWishlist(wishlist);
            }

            if (!wishlist.getProducts().contains(product)) {
                wishlist.addProduct(product);
                em.merge(wishlist);
                em.getTransaction().commit();

                response.setContentType("application/json");
                out.write("{\"status\":\"success\",\"message\":\"Đã thêm vào danh sách yêu thích\"}");
            } else {
                em.getTransaction().rollback();
                out.write("{\"status\":\"info\",\"message\":\"Sản phẩm đã có trong danh sách yêu thích\"}");
            }

        } catch (NumberFormatException e) {
            em.getTransaction().rollback();
            out.write("{\"status\":\"error\",\"message\":\"Invalid product ID\"}");
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace(out);
            out.write("{\"status\":\"error\",\"message\":\"Có lỗi xảy ra: \" + e.getMessage()}");
        }
    }

    private void removeFromWishlist(HttpServletRequest request, HttpServletResponse response, User user, EntityManager em)
            throws IOException {

        String productIdStr = request.getParameter("productId");
        PrintWriter out = response.getWriter();

        if (productIdStr == null || productIdStr.isEmpty()) {
            out.write("ERROR: Product ID is required");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdStr);
            em.getTransaction().begin();

            Product product = em.find(Product.class, productId);
            if (product == null) {
                out.write("ERROR: Product not found");
                em.getTransaction().rollback();
                return;
            }

            Wishlist wishlist = wishlistDAO.findWishlistByCustomerId(user.getId());
            if (wishlist != null && wishlist.getProducts().contains(product)) {
                wishlist.removeProduct(product);
                em.merge(wishlist);
                em.getTransaction().commit();

                response.setContentType("application/json");
                out.write("{\"status\":\"success\",\"message\":\"Đã xóa khỏi danh sách yêu thích\"}");
            } else {
                em.getTransaction().rollback();
                out.write("{\"status\":\"error\",\"message\":\"Sản phẩm không có trong danh sách yêu thích\"}");
            }

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace(out);
            out.write("{\"status\":\"error\",\"message\":\"Có lỗi xảy ra: \" + e.getMessage()}");
        }
    }

    private void checkProductInWishlist(HttpServletRequest request, HttpServletResponse response, User user, EntityManager em)
            throws IOException {

        String productIdStr = request.getParameter("productId");
        PrintWriter out = response.getWriter();

        if (productIdStr == null || productIdStr.isEmpty()) {
            out.write("false");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdStr);
            Wishlist wishlist = wishlistDAO.findWishlistByCustomerId(user.getId());

            boolean exists = (wishlist != null && wishlist.getProducts().stream()
                    .anyMatch(p -> p.getId() == productId));

            response.setContentType("application/json");
            out.write("{\"inWishlist\": " + exists + "}");

        } catch (Exception e) {
            e.printStackTrace(out);
            out.write("{\"inWishlist\": false}");
        }
    }

    @Override
    public void destroy() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}