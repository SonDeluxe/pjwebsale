package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.Order;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class OrderDAO {

    // Tiện ích: xử lý truy vấn không cần transaction
    private <T> T executeWithEntityManager(Function<EntityManager, T> operation) {
        EntityManager em = null;
        try {
            em = ConnectionPool.getEntityManagerFactory().createEntityManager();
            return operation.apply(em);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // Tiện ích: xử lý truy vấn có transaction
    private void executeTransaction(Consumer<EntityManager> operation) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = ConnectionPool.getEntityManagerFactory().createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            operation.accept(em);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            throw new RuntimeException("❌ Lỗi giao dịch khi xử lý OrderDAO", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // === Các phương thức CRUD ===

    public void addOrder(Order order) {
        executeTransaction(em -> em.persist(order));
        System.out.println("✅ Đã thêm đơn hàng mới với ID: " + order.getId());
    }

    public Order findOrderById(int id) {
        return executeWithEntityManager(em -> em.find(Order.class, id));
    }



    // Thêm phương thức này vào file: OrderDAO.java

    /**
     * Tìm một đơn hàng theo ID và tải ngay lập tức danh sách các LineItem và Product liên quan.
     * @param orderId ID của đơn hàng cần tìm.
     * @return Đối tượng Order với đầy đủ chi tiết, hoặc null nếu không tìm thấy.
     */
    public Order findOrderByIdWithDetails(int orderId) {
        return executeWithEntityManager(em -> {
            try {
                return em.createQuery(
                                "SELECT o FROM Order o " +
                                        "LEFT JOIN FETCH o.lineItems li " +
                                        "LEFT JOIN FETCH li.product " + // Lấy cả thông tin sản phẩm
                                        "WHERE o.id = :orderId", Order.class)
                        .setParameter("orderId", orderId)
                        .getSingleResult();
            } catch (jakarta.persistence.NoResultException e) {
                return null; // Trả về null nếu không có kết quả
            }
        });
    }


    public void updateOrder(Order order) {
        executeTransaction(em -> em.merge(order));
        System.out.println("🔄 Đã cập nhật đơn hàng với ID: " + order.getId());
    }

    public List<Order> getAllOrders() {
        return executeWithEntityManager(em ->
                // Thêm LEFT JOIN FETCH o.lineItems li LEFT JOIN FETCH li.product
                em.createQuery("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.lineItems li LEFT JOIN FETCH li.product", Order.class)
                        .getResultList()
        );
    }

    // Thêm vào file OrderDAO.java
    public void deleteByCustomerId(int customerId) {
        executeTransaction(em -> {
            em.createQuery("DELETE FROM Order o WHERE o.customer.id = :customerId")
                    .setParameter("customerId", customerId)
                    .executeUpdate();
        });
    }

    public List<Order> findByCustomerId(int customerId) {
        return executeWithEntityManager(em ->
                // Thêm LEFT JOIN FETCH o.lineItems li LEFT JOIN FETCH li.product
                em.createQuery("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.lineItems li LEFT JOIN FETCH li.product WHERE o.customer.id = :customerId ORDER BY o.orderDate DESC", Order.class)
                        .setParameter("customerId", customerId)
                        .getResultList()
        );
    }

    public long countByStatus(String status) {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT COUNT(o) FROM Order o WHERE o.status = :status", Long.class)
                        .setParameter("status", status)
                        .getSingleResult()
        );
    }

    public void deleteOrder(int id) {
        executeTransaction(em -> {
            Order order = em.find(Order.class, id);
            if (order != null) {
                em.remove(order);
                System.out.println("🗑️ Đã xóa đơn hàng với ID: " + id);
            } else {
                System.out.println("⚠️ Không tìm thấy đơn hàng với ID: " + id);
            }

        });
    }
}