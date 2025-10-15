package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.Review;

import java.util.List;

public class ReviewDAO {

    /**
     * Lấy tất cả các đánh giá của một sản phẩm dựa trên ID sản phẩm.
     * @param productId ID của sản phẩm cần tìm đánh giá.
     * @return Danh sách các đối tượng Review.
     */
    public List<Review> findByProductId(int productId) {
        EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM Review r " +
                                    "LEFT JOIN FETCH r.customer " +
                                    "WHERE r.product.id = :productId " +
                                    "ORDER BY r.date DESC",
                            Review.class)
                    .setParameter("productId", productId)
                    .getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }



    /**
     * Thêm một đánh giá mới vào cơ sở dữ liệu.
     * @param review Đối tượng Review cần lưu.
     */
    public void addReview(Review review) {
        EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // 🔹 Merge product & customer vào persistence context
            if (review.getProduct() != null) {
                review.setProduct(em.merge(review.getProduct()));
            }
            if (review.getCustomer() != null) {
                review.setCustomer(em.merge(review.getCustomer()));
            }

            em.persist(review);
            tx.commit();

            System.out.println("✅ Đã thêm review thành công cho sản phẩm ID = " +
                    review.getProduct().getId() + ", bởi khách hàng = " +
                    review.getCustomer().getName());

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("❌ Lỗi khi thêm review: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể thêm đánh giá", e);
        } finally {
            em.close();
        }
    }





    /**
     * Lấy tất cả các đánh giá có trong hệ thống (dùng cho trang admin).
     * @return Danh sách tất cả các Review.
     */
    public List<Review> findAll() {
        EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Review r", Review.class).getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}