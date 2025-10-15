package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.SparePart;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SparePartDAO {

    private <T> T executeWithEntityManager(Function<EntityManager, T> operation) {
        EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
        try {
            return operation.apply(em);
        } finally {
            em.close();
        }
    }

    private void executeTransaction(Consumer<EntityManager> operation) {
        EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            operation.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void add(SparePart part) {
        executeTransaction(em -> em.persist(part));
    }
    // Thêm 2 phương thức này vào file: src/main/java/vn/edu/hcmute/fit/doan/database/SparePartDAO.java

    public void update(SparePart part) {
        executeTransaction(em -> em.merge(part));
        System.out.println("Đã cập nhật phụ tùng: " + part.getName());
    }

    public void delete(int id) {
        executeTransaction(em -> {
            SparePart part = em.find(SparePart.class, id);
            if (part != null) {
                em.remove(part);
                System.out.println("Đã xóa phụ tùng với ID: " + id);
            }
        });
    }

    public List<SparePart> getAll() {
        return executeWithEntityManager(em ->
                // Sử dụng LEFT JOIN FETCH để tải luôn thông tin category
                em.createQuery("SELECT s FROM SparePart s LEFT JOIN FETCH s.category", SparePart.class)
                        .getResultList()
        );
    }
    public List<SparePart> findByName(String keyword) {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT s FROM SparePart s LEFT JOIN FETCH s.category WHERE s.name LIKE :keyword", SparePart.class)
                        .setParameter("keyword", "%" + keyword + "%")
                        .getResultList()
        );
    }

    public SparePart findById(int id) {
        return executeWithEntityManager(em -> em.find(SparePart.class, id));
    }
}
