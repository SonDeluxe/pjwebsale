//package vn.edu.hcmute.fit.doan.database;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityTransaction;
//import vn.edu.hcmute.fit.doan.Warranty;
//import java.util.List;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//public class WarrantyDAO {
//
//    private <T> T executeWithEntityManager(Function<EntityManager, T> operation) {
//        EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
//        try {
//            return operation.apply(em);
//        } finally {
//            em.close();
//        }
//    }
//
//    private void executeTransaction(Consumer<EntityManager> operation) {
//        EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
//        EntityTransaction tx = em.getTransaction();
//        try {
//            tx.begin();
//            operation.accept(em);
//            tx.commit();
//        } catch (Exception e) {
//            if (tx.isActive()) tx.rollback();
//            throw e;
//        } finally {
//            em.close();
//        }
//    }
//
//    /**
//     * Thêm một bản ghi bảo hành mới vào cơ sở dữ liệu.
//     * @param warranty Đối tượng Warranty cần thêm.
//     */
//    public void add(Warranty warranty) {
//        executeTransaction(em -> em.persist(warranty));
//    }
//
//    /**
//     * Lấy tất cả các bản ghi bảo hành (dành cho Admin).
//     * Dữ liệu được sắp xếp theo ngày hết hạn giảm dần.
//     * @return Danh sách tất cả các phiếu bảo hành.
//     */
//    public List<Warranty> getAll() {
//        return executeWithEntityManager(em ->
//                em.createQuery("SELECT w FROM Warranty w JOIN FETCH w.user JOIN FETCH w.product ORDER BY w.endDate DESC", Warranty.class)
//                        .getResultList()
//        );
//    }
//
//    /**
//     * Tìm một bản ghi bảo hành theo ID.
//     * @param id ID của phiếu bảo hành.
//     * @return Đối tượng Warranty hoặc null nếu không tìm thấy.
//     */
//    public Warranty findById(int id) {
//        return executeWithEntityManager(em -> em.find(Warranty.class, id));
//    }
//
//    /**
//     * Tìm tất cả các phiếu bảo hành của một người dùng cụ thể.
//     * Dữ liệu được sắp xếp theo ngày hết hạn giảm dần.
//     * @param userId ID của người dùng cần tra cứu.
//     * @return Danh sách các phiếu bảo hành của người dùng đó.
//     */
//    public List<Warranty> findByUserId(int userId) {
//        return executeWithEntityManager(em ->
//                em.createQuery("SELECT w FROM Warranty w JOIN FETCH w.product WHERE w.user.id = :userId ORDER BY w.endDate DESC", Warranty.class)
//                        .setParameter("userId", userId)
//                        .getResultList()
//        );
//    }
//}
package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.Warranty;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class WarrantyDAO {

    private <T> T executeWithEntityManager(Function<EntityManager, T> operation) {
        EntityManager em = null;
        try {
            em = ConnectionPool.getEntityManagerFactory().createEntityManager();
            return operation.apply(em);
        } finally {
            if (em != null) em.close();
        }
    }

    private void executeTransaction(Consumer<EntityManager> operation) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = ConnectionPool.getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            operation.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("❌ Lỗi khi thực hiện transaction trong WarrantyDAO", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public void add(Warranty warranty) {
        executeTransaction(em -> {
            if (warranty.getUser() == null || warranty.getProduct() == null) {
                throw new IllegalArgumentException("User or Product cannot be null for Warranty");
            }
            if (!em.contains(warranty.getUser())) {
                warranty.setUser(em.merge(warranty.getUser()));
            }
            if (!em.contains(warranty.getProduct())) {
                warranty.setProduct(em.merge(warranty.getProduct()));
            }
            em.persist(warranty);
        });
        System.out.println("✅ Đã thêm Warranty: " + warranty);
    }

    public List<Warranty> getAll() {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT w FROM Warranty w JOIN FETCH w.user JOIN FETCH w.product ORDER BY w.endDate DESC", Warranty.class)
                        .getResultList()
        );
    }
    // Thêm vào file WarrantyDAO.java
    public void deleteByUserId(int userId) {
        executeTransaction(em -> {
            em.createQuery("DELETE FROM Warranty w WHERE w.user.id = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();
        });
    }

    public Warranty findById(int id) {
        return executeWithEntityManager(em -> em.find(Warranty.class, id));
    }

    // Trong file WarrantyDAO.java

    public List<Warranty> findByUserId(long userId) { // <-- Nên dùng long cho userId
        return executeWithEntityManager(em -> {
            List<Warranty> warranties = em.createQuery(
                            "SELECT w FROM Warranty w JOIN FETCH w.user JOIN FETCH w.product WHERE w.user.id = :userId ORDER BY w.endDate DESC",
                            Warranty.class
                    )
                    .setParameter("userId", userId)
                    .getResultList();
            System.out.println("✅ WarrantyDAO: Tìm thấy " + warranties.size() + " phiếu bảo hành cho user ID: " + userId);
            return warranties;
        });
    }
}