package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.LineItem;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class LineItemDAO {

    // ==========================================================
    // 🧩 PHẦN TIỆN ÍCH CHUNG
    // ==========================================================
    private <T> T executeWithEntityManager(Function<EntityManager, T> operation) {
        try (EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager()) {
            return operation.apply(em);
        }
    }

    private void executeTransaction(Consumer<EntityManager> operation) {
        EntityTransaction tx = null;
        try (EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            operation.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            throw new RuntimeException("❌ Lỗi khi thực hiện transaction trong LineItemDAO", e);
        }
    }

    // ==========================================================
    // 🟢 CRUD CHÍNH
    // ==========================================================

    /** ➕ Thêm mới LineItem vào DB */
    public void addLineItem(LineItem lineItem) {
        executeTransaction(em -> {
            // Nếu cart hoặc product đang ở trạng thái detached, ta merge trước để tránh lỗi
            if (!em.contains(lineItem.getCart())) {
                lineItem.setCart(em.merge(lineItem.getCart()));
            }
            if (!em.contains(lineItem.getProduct())) {
                lineItem.setProduct(em.merge(lineItem.getProduct()));
            }
            em.persist(lineItem);
        });
        System.out.println("✅ Đã thêm LineItem: " + lineItem.getProduct().getName() +
                " (SL: " + lineItem.getQuantity() + ")");
    }

    /** 🔍 Tìm LineItem theo ID */
    public LineItem findLineItemById(int id) {
        return executeWithEntityManager(em -> em.find(LineItem.class, id));
    }

    /** 📋 Lấy tất cả LineItem */
    public List<LineItem> getAllLineItems() {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT li FROM LineItem li", LineItem.class).getResultList()
        );
    }

    /** 🛒 Lấy danh sách LineItem theo CartId (chuẩn JPA property) */
    public List<LineItem> findByCartId(int cartId) {
        return executeWithEntityManager(em ->
                em.createQuery(
                                "SELECT li FROM LineItem li " +
                                        "JOIN FETCH li.product " +
                                        "WHERE li.cart.cartId = :cartId", LineItem.class)
                        .setParameter("cartId", cartId)
                        .getResultList()
        );
    }

    /** ♻️ Cập nhật LineItem */
    public void updateLineItem(LineItem lineItem) {
        executeTransaction(em -> em.merge(lineItem));
        System.out.println("♻️ Đã cập nhật LineItem ID = " + lineItem.getId());
    }

    public void deleteItemsByCartId(int cartId) {
        executeTransaction(em -> {
            // Sử dụng một câu lệnh JPQL DELETE để xóa hàng loạt
            em.createQuery("DELETE FROM LineItem li WHERE li.cart.cartId = :cartId")
                    .setParameter("cartId", cartId)
                    .executeUpdate();
            System.out.println("🗑️ Đã xóa tất cả LineItem từ giỏ hàng ID = " + cartId);
        });
    }

    /** 🗑️ Xóa LineItem */
    public void deleteLineItem(int id) {
        executeTransaction(em -> {
            LineItem li = em.find(LineItem.class, id);
            if (li != null) {
                em.remove(li);
                System.out.println("🗑️ Đã xóa LineItem ID = " + id);
            }
        });
    }
}
