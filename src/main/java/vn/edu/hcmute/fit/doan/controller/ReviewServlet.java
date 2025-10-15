package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmute.fit.doan.*;
import vn.edu.hcmute.fit.doan.database.*;

import java.io.IOException;
import java.util.Date;

@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {

    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("list".equals(action)) {
            request.setAttribute("reviews", reviewDAO.findAll());
            request.getRequestDispatcher("/views/reviews/list.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/reviews?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        // ✅ Không ép buộc đăng nhập (guest cũng được đánh giá)
        Customer customer = null;
        if (session != null && session.getAttribute("user") instanceof Customer) {
            customer = (Customer) session.getAttribute("user");
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment").trim();

            Product product = productDAO.findProductById(productId);

            if (product != null && rating >= 1 && rating <= 5) {
                Review review = new Review();
                review.setProduct(product);
                review.setCustomer(customer); // có thể null nếu là guest
                review.setRating(rating);
                review.setComment(comment);
                review.setDate(new Date());

                reviewDAO.addReview(review);
                if (session != null)
                    session.setAttribute("success", "🎉 Cảm ơn bạn đã gửi đánh giá!");
            } else {
                if (session != null)
                    session.setAttribute("error", "Dữ liệu đánh giá không hợp lệ!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (session != null)
                session.setAttribute("error", "❌ Gửi đánh giá thất bại: " + e.getMessage());
        }

        // ✅ Luôn redirect về lại trang sản phẩm chi tiết
        String productId = request.getParameter("productId");
        response.sendRedirect(request.getContextPath() + "/products?action=view&id=" + productId);
    }
}
